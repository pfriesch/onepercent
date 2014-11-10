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

	def hashtagsTopOfThePops(filePath: String, tempTableName: String, topX: Int) : Array[(Int, String)] = {

		val file = new JSONFileReader(sc, filePath, tempTableName)

		val table = file.createTable("hashtags.text", "entities.hashtags", "hashtags")

		val mappedTable = table.map(word => (word.apply(0).toString,1))
		val reducedTable = mappedTable.reduceByKey(_ + _)
		val sortedTable = reducedTable.map{case (tag, count) => (count, tag)}.sortByKey(false) 

		return sortedTable.top(topX).toArray
	}

}