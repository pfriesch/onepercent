package tat

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.hive._
import org.apache.spark.rdd._


/**
* This class shall be the the analyser which does the job of mapping and 
* reducing for the results.
*
* @author Patrick Mariot, Florian Willich
**/
class TweetAnalyser(sc: SparkContext, hiveContext: HiveContext) {
	
	val fileReader: TweetJSONFileReader = new TweetJSONFileReader(sc, hiveContext)

	/**
	* This Jobhandler 
	*
	* 
	def jobHandler(job: Job, prefixPath: String, params: Map) : Any {

		match job {

			case hashtagsTopOfThePops:
				return hashtagsTopOfThePops(prefixPath, params.get(topX), params.get(timestamp), true)

			case _:
				println("ERROR: Undefined Job!")
				return new Array((1, "nothing"))

		}

	}
	**/

	def hashtagsTopOfThePops(path: T_Path, topX: Int) : T_TopHashtag = {

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
