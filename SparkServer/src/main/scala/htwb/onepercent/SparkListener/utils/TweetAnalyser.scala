/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package htwb.onepercent.SparkListener.utils

//Spark imports
import java.text.SimpleDateFormat
import java.util.{Calendar, Date, TimeZone}

import htwb.onepercent.SparkListener.JobResult
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.hive._


//Make a trait out of this class


/**
 * This class shall be the the analyser which does the job of mapping and
 * reducing for the results.
 *
 * @author Patrick Mariot, Florian Willich
 **/
class TweetAnalyser(sc: SparkContext, hiveContext: HiveContext) {

  /**
   * This method returns the top X hashtags of the transfered tweetFile
   *
   * @param scheme  The scheme on which the analysis is processed.
   * @param topX    The top X hashtags you want to extract.
   *
   * @return        the top X hashtags.
   */
  def topHashtagAnalyser(scheme: SchemaRDD, topX: Int): TopHashtags = {

    scheme.registerTempTable("tweets")

    val hashtagsScheme: SchemaRDD = hiveContext.sql("SELECT entities.hashtags FROM tweets")

    //Temp Table exists as long as the Spark/Hive Context
    hashtagsScheme.registerTempTable("hashtags")
    
    val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM hashtags LATERAL VIEW EXPLODE(hashtags) t1 AS hashtags")
    val mappedTable: RDD[(String, Int)] = table.map(word => (word.apply(0).toString.toLowerCase, 1))
    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)

    //Well this is some hack to change the tuple order to get the top hashtags and then
    //map it back again to get a HashtagFrequency
    val topHashtags: Array[HashtagFrequency] = reducedTable.map { case (a, b) => (b, a)}.top(topX).map { case (a, b) => HashtagFrequency(b, a)}

    //table.count() -> All unique hashtags
    new TopHashtags(topHashtags, table.count())
  }

  /**
   * This method calculates the distribution of tweets that contain a given word.
   *
   * @param scheme            The scheme on which the analysis is processed.
   * @param inputSearchWord   Word to look for in the tweet texts.
   *
   * @return                  the searchWord, distribution of this word, example tweet ids
   */
  def wordSearchAnalyser(scheme: SchemaRDD, inputSearchWord: String): WordSearch = {
    val searchWord = inputSearchWord.toLowerCase

    // defines a SimpleDateFormat ignores minutes and seconds
    val timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:00:00")

    scheme.registerTempTable("tweets")

    val table: SchemaRDD = hiveContext.sql("SELECT timestamp_ms, id_str, text FROM tweets")
    // filter the text column, wether it contains the word to search for
    val filteredTable: SchemaRDD = table.filter(row => row.getString(2).toLowerCase.contains(searchWord))
    // format the timestamp_ms in a valid timestamp format, automatically use the timezone from the server on which spark is running
    val mappedTable: RDD[(String, Int)] = filteredTable.map(row => (timestampFormatter.format(new Date(row.getString(0).toLong)), 1))
    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)

    val wordDistribution: Array[TweetDistribution] = reducedTable.collect.map { case (a, b) => TweetDistribution(a, b) }

    val sampleIds: Array[String] = filteredTable.map(row => row.getString(1)).takeSample(true, 10, 3)

    new WordSearch(inputSearchWord, wordDistribution, sampleIds)
  }

  /**
   * This method calculates the distribution of tweets, based on their local time of creation
   *
   * @param scheme            The scheme on which the analysis is processed.
   * @param searchDateString  The timestamp that contains the date to filter.
   * @return                  the distribution of the search date
   */
  def tweetsAtDaytimeAnalyser(scheme: SchemaRDD, searchDateString: String): TweetsAtDaytime = {

    /**
     * Combines the timestamp and the offset from UTC to "normalize" the timestamps to there local time of creation
     * https://stackoverflow.com/questions/23732999/avoid-task-not-serialisable-with-nested-method-in-a-class
     */
    val convertToLocalTime = (timestamp: Long, offset: Int) => {
      val inputDate: Calendar = Calendar.getInstance()
      inputDate.setTime(new Date(timestamp))
      val offsetInHours: Int = offset / 3600
      inputDate.set(Calendar.HOUR, inputDate.get(Calendar.HOUR) + offsetInHours)
      inputDate.getTime
    }

    /**
     * Checks if two Date Objects contain the same date.
     */
    val checkForSameDay = (date1: Date, date2: Date) => {
      val cal1 = Calendar.getInstance()
      val cal2 = Calendar.getInstance()
      cal1.setTime(date1)
      cal2.setTime(date2)
      cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    val timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:00:00")
    timestampFormatter.setTimeZone(TimeZone.getTimeZone("GMT"))
    val searchDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val searchDate: Date = searchDateFormatter.parse(searchDateString)

    scheme.registerTempTable("tweets")

    val table: SchemaRDD = hiveContext.sql("SELECT timestamp_ms, user.utc_offset FROM tweets WHERE user.utc_offset IS NOT NULL")
    val mappedTable: RDD[(String, Int)] = table.map(row => (timestampFormatter.format(convertToLocalTime(row.getString(0).toLong, row.getInt(1))), 1))
    val filteredTable: RDD[(String, Int)] = mappedTable.filter { case (a,b) => checkForSameDay(timestampFormatter.parse(a), searchDate) }
    val reducedTable: RDD[(String, Int)] = filteredTable.reduceByKey(_ + _)
    val tweetDistribution: Array[TweetDistribution] = reducedTable.collect.map{ case (a, b) => TweetDistribution(a, b) }

    new TweetsAtDaytime(tweetDistribution)
  }

  /**
   * This method calculates how many origin and retweeted tweets exist in the given schema.
   * @param scheme    The scheme on which the analysis is processed.
   * @param timestamp Contains the time and date for which the calculations will be done.
   * @return          the timestamp, count of origin tweets and count of retweeted tweets
   */
  def originTweetsAnalyser(scheme: SchemaRDD, timestamp: String): OriginTweets = {

    scheme.registerTempTable("tweets")

    val table: SchemaRDD = hiveContext.sql("SELECT count(*) FROM tweets WHERE retweeted_status IS NOT NULL")
    val retweetedTweets: Long = table.first.getLong(0)
    val originTweets: Long = scheme.count - retweetedTweets

    new OriginTweets(timestamp,originTweets, retweetedTweets)
  }

  /**
   * This method calculates how the distribution of langauges in tweets is for a given schema.
   * @param scheme    The schema on which the analysis is processed.
   * @param timestamp Contains the time and date for which the calculations will be done.
   * @return          The language and there total appearance
   */
  def languageDistributionAnalyser(scheme: SchemaRDD, timestamp: String): LanguageDistributionResult = {

    scheme.registerTempTable("tweets")

    val table: SchemaRDD = hiveContext.sql("SELECT lang FROM tweets")
    val mappedTable: RDD[(String, Int)] = table.map(row => (row.getString(0), 1))
    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)
    // filter if language appears more than 100 times
    val filteredTable: RDD[(String, Int)] = reducedTable.filter{ case (a,b) => b > 100 }
    val languageDistribution: Array[LanguageDistribution] = filteredTable.collect.map{ case (a, b) => LanguageDistribution(a, b) }
    new LanguageDistributionResult(languageDistribution)
  }
}

/**
 * ==================================================================================
 * TYPES RELATED TO JOB RESULTS
 * ==================================================================================
 */

/**
 * Type representing one Twitter hashtag and its related counts.
 *
 * @param hashtag   The twitter hashtag.
 * @param count     The count of this twitter hashtag.
 */
case class HashtagFrequency(hashtag: String, count: Long) extends JobResult

/**
 * Type representing The Top twitter hashtags as an analysis result including
 * all hashtags and its frequency and the count of all hashtags counted whyle
 * analysing the twitter tweets.
 *
 * @param topHashtags       All hashtags and its related frequency.
 * @param countAllHashtags  The count of all hashtags counted whyle analysing the
 *                          twitter tweets.
 */
case class TopHashtags(topHashtags: Array[HashtagFrequency], countAllHashtags: Long) extends JobResult

/**
 * Type representing one Tweet timestamp and its related count.
 *
 * @param timestamp The Tweet timestamp.
 * @param count     The Count of this tweet timestamp.
 */
case class TweetDistribution(timestamp: String, count: Long) extends JobResult

/**
 * Type representing the distribution of a word used in Tweet texts as an analysis result including
 * the word to search for, the distribution over time including the timestamp and the count. Furthermore
 * in contains up to 10 tweet ids, that contain the word to search for.
 *
 * @param searchWord    The word to search for in the tweet texts.
 * @param countedTweets All timestamps and the counts, where tweet texts contain the searchWord.
 * @param tweetIds      Up to 10 ids of tweet that contain the searchWord.
 */
case class WordSearch(searchWord: String, countedTweets: Array[TweetDistribution], tweetIds: Array[String]) extends JobResult

/**
 * Type representing the distribution of tweets over time.
 *
 * @param countedTweets All timestamps and the counts, where tweets happened.
 */
case class TweetsAtDaytime(countedTweets: Array[TweetDistribution]) extends JobResult

/**
 * Type representing the count of origin and retweeted tweets, including the timestamp for which the caluclation is done.
 * @param timestamp         The timestamp that is a Job Parameter.
 * @param originTweetCount  The count of origin tweets.
 * @param retweetCount      The count of retweeted tweets.
 */
case class OriginTweets(timestamp: String, originTweetCount: Long, retweetCount: Long) extends JobResult

/**
 * Type representing one Tweet language and its related count.
 *
 * @param language  The Tweet language.
 * @param count     The Count of this tweet timestamp.
 */
case class LanguageDistribution(language: String, count: Long) extends JobResult

/**
 * Type representing the distribution of languages in tweets over time.
 *
 * @param languages All languages and the counts, that where used in tweets.
 */
case class LanguageDistributionResult(languages: Array[LanguageDistribution]) extends JobResult