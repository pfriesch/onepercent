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

	def hashtagsTopOfThePops(prefixPath: String, timestamp: String, topX: Int, useTimeStamp: Boolean) /**: T_TopHashtag =**/ {

		//This was made with some RegEx: | is an or that means split with - or " " or :
		val timeSplitted: Array[String] = timestamp.split("-| |:") 
		var path: String = "";

		if (useTimeStamp) {

			//Concatenate the prefix path with the given timestamp for the Folders
			path = prefixPath + timeSplitted(0) + "/" + timeSplitted(1) + "/" + timeSplitted(2) + "/" + timeSplitted(3) + "/*.data"
			
		} else {
			path = prefixPath
		}

		val scheme: SchemaRDD = fileReader.readFile(path)
		
		//Amount of all Tweets
		val countAllTweets: Long = scheme.count()

		scheme.registerTempTable("tweets") 

		//Process Map->Reduce all hashtags
		val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM tweets LATERAL VIEW EXPLODE(entities.hashtags) t1 AS hashtags")
		val mappedTable /**: RDD **/ = table.map(word => (word.apply(0).toString, 1))
		val reducedTable /**: RDD **/ = mappedTable.reduceByKey(_ + _)
		val sortedTable /**: RDD **/ = reducedTable.map{case (tag, count) => (count, tag)}.sortByKey(false) 

		val resultA: Array[(Int, String)] = sortedTable.top(topX)

		/**
		for(i <- 0 until resultA.length){
		    println("i is: " + i);
		    println("i'th element is: " + myArray(i));
		}

		result.foreach()
		**/

		resultA.foreach(println) 
	}

}