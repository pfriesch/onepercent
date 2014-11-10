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
*
* @author Patrick Mariot, Florian Willich
**/
object App {
  
	def main(args: Array[String]) {

		val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
		val sc = new SparkContext(conf)

		val ta = new TweetAnalyser(sc)			
		val hashtagsTop10 = ta.hashtagsTopOfThePops(args(0), "tweets", 10)
		
		hashtagsTop10.foreach(println)

  	}

}