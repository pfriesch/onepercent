package tat

/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._

case class UniqueHashtag(indices: scala.collection.mutable.ArrayBuffer[Int], hashtag: String)
case class Hashtags(hashtags: scala.collection.mutable.ArrayBuffer[UniqueHashtag])

/**
* Gibt die Top10 Hashtags auf der Kommandozeile aus.
* Der einzulesende Pfad wird über das erste Argument angegeben.
* Wegen des HiveContext ist mindestens Apache Spark 1.1.0 erforderlich.
* Quellen:
* - http://blog.cloudera.com/blog/2012/11/analyzing-twitter-data-with-hadoop-part-3-querying-semi-structured-data-with-hive/
* - http://apache-spark-user-list.1001560.n3.nabble.com/flattening-a-list-in-spark-sql-td13300.html
* - http://apache-spark-user-list.1001560.n3.nabble.com/Query-the-nested-JSON-data-With-Spark-SQL-1-0-1-td9544.html
*
* @author Patrick Mariot, Florian Willich
**/
object App {
  
	def main(args: Array[String]) {

		val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
		val sc = new SparkContext(conf)

		val ta = new JSONFileReader(sc, args(0))
		
		val hashtags = ta.createTable("hashtags.text", "entities.hashtags", "hashtags")

		val hashtagsMap = hashtags.map(word => (word.apply(0).toString,1))

		val hashtagsReduced = hashtagsMap.reduceByKey(_ + _)
		
		val hashtagsSorted = hashtagsReduced.map{case (tag, count) => (count, tag)}.sortByKey(false)
		
		val hashtagsTop10 = hashtagsSorted.top(10).toArray
		hashtagsTop10.foreach(println)

  	}

}