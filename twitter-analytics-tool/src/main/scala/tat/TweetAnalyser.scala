package tat

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._

class TweetAnalyser(sc: SparkContext) {
	
	val hiveContext: HiveContext = new org.apache.spark.sql.hive.HiveContext(sc)
	var tweets: SchemaRDD
	var table: SchemaRDD

	def readJSONFile(path: String) {
		tweets = hiveContext.jsonFile(path)
		tweets.registerAsTable("tweets")
	}

	def createTable(select: String, entities: String, tableName: String) : SchemaRDD = {
		return hiveContext.sql("SELECT " + select + " FROM tweets LATERAL VIEW EXPLODE(" + entities + ") t1 AS " + tableName)
	}

	def createTable(select: String, entities: String, tableName: String, mapFun: Unit => Unit) : SchemaRDD = {
		return createTable(select, entities, tableName).map(mapFun)
	}

	def createTable(select: String, entities: String, tableName: String, mapFun: Unit => Unit, reduceByKeyFun: Unit) : SchemaRDD = {
		return createTable(select, entities, tableName, mapFun).reduceByKey(reduceByKeyFun)
	}

}