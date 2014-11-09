package tat

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.hive._

class JSONFileReader(sc: SparkContext, pathToJSONFile: String) {
	
	val hiveContext: HiveContext = new HiveContext(sc)
	var data = (hiveContext.jsonFile(pathToJSONFile)).registerTempTable("data")

	def readFile(pathToJSONFile: String) {
		data = hiveContext.jsonFile(pathToJSONFile).registerTempTable("data")
	}

	def createTable(select: String, entities: String, tableName: String) : SchemaRDD = {
		return hiveContext.sql("SELECT " + select + " FROM data LATERAL VIEW EXPLODE(" + entities + ") t1 AS " + tableName)
	}

	//Not working yet, trying diffrent way
	/**def createTable(select: String, entities: String, tableName: String, mapFun: org.apache.spark.sql.Row => Unit) : SchemaRDD = {
		return createTable(select, entities, tableName).map(mapFun)
	}

	def createTable(select: String, entities: String, tableName: String, mapFun: Unit => Unit, reduceByKeyFun: Unit) : SchemaRDD = {
		return createTable(select, entities, tableName, mapFun).reduceByKey(reduceByKeyFun)
	}**/

}