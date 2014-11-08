package tat

/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._

case class UniqueHashtag(indices: scala.collection.mutable.ArrayBuffer[Int], hashtag: String)
case class Hashtags(hashtags: scala.collection.mutable.ArrayBuffer[UniqueHashtag])

/**
 * @author ${user.name}
 */
object App {
  
	def main(args: Array[String]) {

		val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
		val sc = new SparkContext(conf)

		val ta = new TweetAnalyser(sc)
		ta.readJSONFile(args(0))
		
		val hashtags = ta.createTable("hashtags.text", "entities.hashtags", "hashtags", (word) => (word.apply(0).toString, 1), _ + _)

		val hashtagsSorted = hashtags.map{case (tag, count) => (count, tag)}.sortByKey(false)
		val hashtagsTop10 = hashtagsSorted.top(10).toArray
		
		hashtagsTop10.foreach(println)

  	}

}