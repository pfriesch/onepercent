package tat

/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.hive._

case class UniqueHashtag(indices: scala.collection.mutable.ArrayBuffer[Int], hashtag: String)
case class Hashtags(hashtags: scala.collection.mutable.ArrayBuffer[UniqueHashtag])

/**
* Gibt die Top10 Hashtags auf der Kommandozeile aus.
* Der einzulesende Pfad wird über das erste Argument angegeben.
*
* Use the following prefix path (Argument 0) for the clust:
* "hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/"
*
* @author Patrick Mariot, Florian Willich
**/
object App {
  
	def main(args: Array[String]) {

		val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
		val sc = new SparkContext(conf)
		val hc = new HiveContext(sc)

		val ta = new TweetAnalyser(sc, hc)	
		
		val hashtagsTop10 = ta.hashtagsTopOfThePops(new T_Path(args(0)), 10)

		println(TypeCreator.createPathToClusterData("hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/", TypeCreator.timestampToDate("2014-11-16 15:33"), "*.data"))
		
  	}

}