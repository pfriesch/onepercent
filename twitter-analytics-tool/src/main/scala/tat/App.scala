package tat

/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * @author ${user.name}
 */
object App {
  
	def main(args: Array[String]) {

		val sc: SparkContext // An existing SparkContext.
		val sqlContext = new org.apache.spark.sql.SQLContext(sc)

		// createSchemaRDD is used to implicitly convert an RDD to a SchemaRDD.
		//import sqlContext.createSchemaRDD

		// A JSON dataset is pointed to by path.
		// The path can be either a single text file or a directory storing text files.
		val path = "/home/flo/htw/TAT_rep/data.json"

		// Create a SchemaRDD from the file(s) pointed to by path
		val jsonTweets = sqlContext.jsonFile(path)

		// The inferred schema can be visualized using the printSchema() method.
		//people.printSchema()
		// root
		//  |-- age: IntegerType
		//  |-- name: StringType

		// Register this SchemaRDD as a table.
		jsonTweets.registerAsTable("jsonTweets")

		// SQL statements can be run by using the sql methods provided by sqlContext.
		val hashtags = sqlContext.sql("SELECT entities.hashtags FROM jsonTweets")

		// Alternatively, a SchemaRDD can be created for a JSON dataset represented by
		// an RDD[String] storing one JSON object per string.
		val anotherPeopleRDD = sc.parallelize(
		  """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
		val anotherPeople = sqlContext.jsonRDD(anotherPeopleRDD)




  	}

}