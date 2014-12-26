package htwb.onepercent.SparkListener.utils

//Spark imports
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.hive._



import java.io.File
import java.util.{Calendar, Date}
import java.text.SimpleDateFormat

import htwb.onepercent.SparkListener.JobResult

import scala.util.Try


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
   * @param scheme      The scheme on which the analysis is processed.
   * @param inputSearchWord  Word to look for in the tweet texts.
   *
   * @return            the searchWord, distribution of this word, example tweet ids
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
    val mappedTable: RDD[(String, Int)] = filteredTable.map(row => (timestampFormatter.format(new Date(row.getLong(0))), 1))
    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)

    val wordDistribution: Array[TweetDistribution] = reducedTable.collect.map { case (a, b) => TweetDistribution(a, b) }

    val sampleIds: Array[String] = filteredTable.map(row => row.getString(1)).takeSample(true, 10, 3)

    new WordSearch(inputSearchWord, wordDistribution, sampleIds)
  }

  def tweetsAtDaytimeAnalyser(scheme: SchemaRDD, searchDateString: String): TweetsAtDaytime = {

    def convertToLocalTime(timestamp: Long, offset: Int): Date = {
      val inputDate: Calendar = Calendar.getInstance()
      inputDate.setTime(new Date(timestamp))
      val offsetInHours: Int = offset / 3600
      inputDate.set(Calendar.HOUR, inputDate.get(Calendar.HOUR) + offsetInHours)
      return inputDate.getTime()
    }

    def checkForSameDay(date1: Date, date2: Date): Boolean = {
      val cal1 = Calendar.getInstance()
      val cal2 = Calendar.getInstance()
      cal1.setTime(date1)
      cal2.setTime(date2)
      cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    val timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:00:00")
    val searchDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val searchDate: Date = searchDateFormatter.parse(searchDateString)

    scheme.registerTempTable("tweets")

    val table: SchemaRDD = hiveContext.sql("SELECT timestamp_ms, user.utc_offset FROM tweets WHERE user.utc_offset IS NOT NULL")
    val mappedTable: RDD[(String, Int)] = table.map(row => (timestampFormatter.format(convertToLocalTime(row.apply(0).toString.toLong, row.apply(1).toString.toInt)), 1))
    val filteredTable: RDD[(String, Int)] = mappedTable.filter { case (a,b) => checkForSameDay(timestampFormatter.parse(a), searchDate) }
    val reducedTable: RDD[(String, Int)] = filteredTable.reduceByKey(_ + _)
    val tweetDistribution: Array[TweetDistribution] = reducedTable.collect.map{ case (a, b) => TweetDistribution(a, b) }

    new TweetsAtDaytime(tweetDistribution)
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