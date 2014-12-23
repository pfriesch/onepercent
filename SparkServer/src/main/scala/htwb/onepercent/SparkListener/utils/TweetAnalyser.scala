package htwb.onepercent.SparkListener.utils

//Spark imports
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.hive._



import java.io.File
import java.util.Date
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
   * @param searchWord  Word to look for in the tweet texts.
   *
   * @return            the searchWord, distribution of this word, example tweet ids
   */
  def wordSearchAnalyser(scheme: SchemaRDD, searchWord: String): WordSearch = {

    val timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:00:00")

    scheme.registerTempTable("tweets")

    val table: SchemaRDD = hiveContext.sql("SELECT timestamp_ms, id_str, text FROM tweets")
    val filteredTable: SchemaRDD = table.filter(row => row.apply(2).toString.toLowerCase.contains(searchWord))
    val mappedTable: RDD[(String, Int)] = filteredTable.map(row => (timestampFormatter.format(new Date(row.apply(0).toString.toLong)), 1))
    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)

    val wordDistribution: Array[WordDistribution] = reducedTable.collect.map { case (a, b) => WordDistribution(a, b) }

    val sampleIds: Array[String] = filteredTable.map(row => row.apply(1).toString).takeSample(true, 10, 3)

    new WordSearch(searchWord, wordDistribution, sampleIds)
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
case class WordDistribution(timestamp: String, count: Long) extends JobResult

/**
 * Type representing the distribution of a word used in Tweet texts as an analysis result including
 * the word to search for, the distribution over time including the timestamp and the count. Furthermore
 * in contains up to 10 tweet ids, that contain the word to search for.
 *
 * @param searchWord    The word to search for in the tweet texts.
 * @param countedTweets All timestamps and the counts, where tweet texts contain the searchWord.
 * @param tweetIds      Up to 10 ids of tweet that contain the searchWord.
 */
case class WordSearch(searchWord: String, countedTweets: Array[WordDistribution], tweetIds: Array[String]) extends JobResult