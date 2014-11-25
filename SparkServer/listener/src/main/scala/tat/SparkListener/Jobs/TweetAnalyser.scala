package tat.SparkListener.Jobs

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.hive._
import tat.SparkListener.Jobs.Types._


/**
 * This class shall be the the analyser which does the job of mapping and
 * reducing for the results.
 *
 * @author Patrick Mariot, Florian Willich
 **/
class TweetAnalyser(sc: SparkContext, hiveContext: HiveContext) {

  val fileReader: TweetJSONFileReader = new TweetJSONFileReader(sc, hiveContext)


  def hashtagsTopOfThePops(path: T_Path, topX: Int): T_TopHashtags = {

    val scheme: SchemaRDD = fileReader.readFile(path.toString())
    scheme.registerTempTable("tweets")

    //Process Map->Reduce all hashtags
    //val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM tweets LATERAL VIEW EXPLODE(entities.hashtags) t1 AS hashtags")

    val hashtagsScheme: SchemaRDD = hiveContext.sql("SELECT entities.hashtags FROM tweets")
    hashtagsScheme.registerTempTable("hashtags")
    val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM hashtags LATERAL VIEW EXPLODE(hashtags) t1 AS hashtags")

    val mappedTable: RDD[(String, Int)] = table.map(word => (word.apply(0).toString().toLowerCase(), 1))

    val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)

    //All unique hashtags
    val countAllHashtags: Long = table.count()

    /**
     * Old code:
     * val sortedTable = reducedTable.map{case (tag, count) => (count, tag)}.sortByKey(false)
     * val resultA: Array[(Int, String)] = reducedTable.map((hashtag, count) => (count, hashtag)).top(topX)
     **/
    val topHashtags: Array[T_HashtagFrequency] = reducedTable.map { case (a, b) => (b, a)}.top(topX).map { case (a, b) => T_HashtagFrequency(b, a)} //.map{ case (a, b) => (b, a)}

    val topOfThePops: T_TopHashtags = new T_TopHashtags(topHashtags, countAllHashtags)

    return topOfThePops
  }

}
