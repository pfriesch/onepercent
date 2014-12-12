package tat.SparkListener.utils

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.hive._


import java.io.File

import scala.util.Try
;

//Make a trait out of this class

/**
 * This class shall be the the analyser which does the job of mapping and
 * reducing for the results.
 *
 * @author Patrick Mariot, Florian Willich
 **/
class TweetAnalyser(sc: SparkContext, hiveContext: HiveContext) {

  val fileReader: TweetJSONFileReader = new TweetJSONFileReader(sc, hiveContext)

  /**
   * This method returns the top X hashtags of the transfered tweetFile
   * @param path
   * @param topX
   * @return
   */
  def topHashtagAnalyser(path: T_Path, topX: Int): TopHashtags = {

    val scheme: SchemaRDD = fileReader.readFile(path.path)

    scheme.registerTempTable("tweets")

    val hashtagsScheme: SchemaRDD = hiveContext.sql("SELECT entities.hashtags FROM tweets")

    //Temp Table exists as long as the Spark/Hive Context
    hashtagsScheme.registerTempTable("hashtags")
    
    val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM hashtags LATERAL VIEW EXPLODE(hashtags) t1 AS hashtags")
    val mappedTable: RDD[(String, Int)] = table.map(word => (word.apply(0).toString().toLowerCase(), 1))
    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)
    val topHashtags: Array[HashtagFrequency] = reducedTable.map { case (a, b) => (b, a)}.top(topX).map { case (a, b) => HashtagFrequency(b, a)}

    //table.count() -> All unique hashtags
    new TopHashtags(topHashtags, table.count())
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
case class HashtagFrequency(hashtag: String, count: Long)

/**
 * Type representing The Top twitter hashtags as an analysis result including
 * all hashtags and its frequency and the count of all hashtags counted whyle
 * analysing the twitter tweets.
 *
 * @param topHashtags       All hashtags and its related frequency.
 * @param countAllHashtags  The count of all hashtags counted whyle analysing the
 *                          twitter tweets.
 */
case class TopHashtags(topHashtags: Array[HashtagFrequency], countAllHashtags: Long)
