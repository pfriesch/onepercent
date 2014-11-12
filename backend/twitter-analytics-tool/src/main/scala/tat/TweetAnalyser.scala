package tat

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._

/**
* This class shall be the the analyser which does the job of mapping and 
* reducing for the results.
*
* @author Patrick Mariot, Florian Willich
**/
class TweetAnalyser(sc: SparkContext) {
	
	val hiveContext: HiveContext = new HiveContext(sc)
	val fileReader: TweetJSONFileReader = new TweetJSONFileReader(sc)

	def jobHandler(job: Job, params: Map) : Array[(Int, String)] {

		match job {

			case hashtagsTopOfThePops:
				return hashtagsTopOfThePops(params.get(topX), params.get(timestamp))

			case _:
				println("ERROR: Undefined Job!")
				return new Array((1, "nothing"))

		}

	}

	def hashtagsTopOfThePops(timestamp: String, topX: Int) : Array[(Int, String)] = {

		//This was made with some RegEx: | is an or that means split with - or " " or :

		val prefixPath: String = "hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/"

		val time = timestamp.split("-| |:") 

		val path: String = prefixPath + time[0] + "/" + time[1] + "/" + time[2] + "/" + time[3] + "/*.data"

		val tempTable = fileReader.readFile(path, "tweets")

		val table = hiveContext.sql("SELECT hashtags.text FROM tweets LATERAL VIEW EXPLODE(entities.hashtags) t1 AS hashtags")
		val mappedTable = table.map(word => (word.apply(0).toString,1))
		val reducedTable = mappedTable.reduceByKey(_ + _)
		val sortedTable = reducedTable.map{case (tag, count) => (count, tag)}.sortByKey(false) 

		return sortedTable.top(topX).toArray
	}

}