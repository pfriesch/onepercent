package tat.SparkListener.Jobs

case class TopHashtags(hashtags: List[(String,Int)])


/**
 * Created by plinux on 18/11/14.
 */
class Top10HashtagsJobExecutor extends JobExecutor{

  override def executeJob(params: Array[String]): Result = {
    //TODO: return real results
    import org.json4s._
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val topHashtags = TopHashtags(List(("MTVStars", 100 ), ("Lahm", 91), ("Stock Update",88), ("Vorlesung",54),(params(1),2),(params(0),1)))
    val json = topHashtags.hashtags
    Result(compact(render(json)))
  }

}

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.hive._
import org.apache.spark.rdd._

class RealTopOfThePops extends JobExecutor {

	override def executeJob(params: Array[String]): Result = {
	    //TODO: return real results
	    import org.json4s._
	    import org.json4s.JsonDSL._
	    import org.json4s.native.JsonMethods._

	    val result: T_TopHashtag = hashtagsTopOfThePops(params(0), params(1))

	    val topHashtags: String = compact(render(result))
	    
	    Result(topHashtags)
  	}

  	def hashtagsTopOfThePops(path: T_Path, topX: Int) : T_TopHashtag = {

  		val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
    	val sc = new SparkContext(conf)
    	val hc = new HiveContext(sc)

    	val fileReader: TweetJSONFileReader = new TweetJSONFileReader(sc, hiveContext)

		val scheme: SchemaRDD = fileReader.readFile(path.toString())
		scheme.registerTempTable("tweets") 

		//Process Map->Reduce all hashtags
		//val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM tweets LATERAL VIEW EXPLODE(entities.hashtags) t1 AS hashtags")

		val hashtagsScheme: SchemaRDD = hiveContext.sql("SELECT entities.hashtags FROM tweets")
		hashtagsScheme.registerTempTable("hashtags")
		val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM hashtags LATERAL VIEW EXPLODE(hashtags) t1 AS hashtags")

		val mappedTable: RDD[(String, Int)] = table.map(word => (word.apply(0).toString().toLowerCase() , 1))

		val reducedTable: RDD[(String, Int)] = mappedTable.reduceByKey(_ + _)

		//All unique hashtags
		val countAllHashtags: Long = table.count()
		
		/** 
		* Old code:
		* val sortedTable = reducedTable.map{case (tag, count) => (count, tag)}.sortByKey(false) 
		* val resultA: Array[(Int, String)] = reducedTable.map((hashtag, count) => (count, hashtag)).top(topX) 
		**/
		val topHashtags: Array[(Int, String)] = reducedTable.map{ case (a, b) => (b, a) }.top(topX) //.map{ case (a, b) => (b, a)}

		val arrayOfAllHashtags: Array[T_HashtagFrequency] = new Array[T_HashtagFrequency](topHashtags.length)

	    for (i <- 0 to (topHashtags.length - 1)) {
	    	arrayOfAllHashtags(i) = new T_HashtagFrequency(topHashtags(i)._2, topHashtags(i)._1)
	    }

	    val topOfThePops: T_TopHashtag = new T_TopHashtag(countAllHashtags, arrayOfAllHashtags)

		return topOfThePops		
	}

}