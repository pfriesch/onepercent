package tat.SparkListener.Jobs

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.hive._
import tat.SparkListener.Jobs.Types._

import java.io.File;

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
   * @param tweetFile
   * @param topX
   * @return
   */
  def topHashtagAnalyser(tweetFile: File, topX: Int): T_TopHashtags = {

    val scheme: SchemaRDD = fileReader.readFile(tweetFile.getPath())

    scheme.registerTempTable("tweets")

    val hashtagsScheme: SchemaRDD = hiveContext.sql("SELECT entities.hashtags FROM tweets")

    //Temp Table exists as long as the Spark/Hive Context
    hashtagsScheme.registerTempTable("hashtags")
    
    val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM hashtags LATERAL VIEW EXPLODE(hashtags) t1 AS hashtags")
    val mappedTable: RDD[(String, Int)] = table.map(word => (word.apply(0).toString().toLowerCase(), 1))
    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)
    val topHashtags: Array[T_HashtagFrequency] = reducedTable.map { case (a, b) => (b, a)}.top(topX).map { case (a, b) => T_HashtagFrequency(b, a)}

    //table.count() -> All unique hashtags
    new T_TopHashtags(topHashtags, table.count())
  }

}
