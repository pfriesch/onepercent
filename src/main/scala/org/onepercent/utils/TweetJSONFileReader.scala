/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.utils

import org.apache.spark.SparkContext
import org.apache.spark.sql._
import org.apache.spark.sql.hive._

/**
* JSONFileReader to read JSON Files and extract data.
*
* Wegen des HiveContext ist mindestens Apache Spark 1.1.0 erforderlich.
* Quellen:
* - http://blog.cloudera.com/blog/2012/11/analyzing-twitter-data-with-hadoop-part-3-querying-semi-structured-data-with-hive/
* - http://apache-spark-user-list.1001560.n3.nabble.com/flattening-a-list-in-spark-sql-td13300.html
* - http://apache-spark-user-list.1001560.n3.nabble.com/Query-the-nested-JSON-data-With-Spark-SQL-1-0-1-td9544.html
*
* @author Patrick Mariot, Florian Willich
**/
class TweetJSONFileReader(sc: SparkContext, hiveContext: HiveContext) {

	/**
	* Reads the JSON File and returns a SchemaRDD.
	**/
	def readFile(pathToJSONFile: String) : SchemaRDD = {
		hiveContext.jsonFile(pathToJSONFile)
	}

	/**
	 * Read a List of T_Path and returns the result in one SchemaRDD.
	 * @param pathListToJSONFile List of validate Paths
	 * @return SchemaRDD that contains Data from all Paths
	 */
	def readFile(pathListToJSONFile: List[Path]) : SchemaRDD = {
		var result: SchemaRDD = hiveContext.jsonFile(pathListToJSONFile.head.path)
		// setting Schema at hiveContext.jsonFile improves performance by a lot
		val dataSchema: StructType = result.schema
		for(i <- 1 to (pathListToJSONFile.length - 1)) {
			result = result.unionAll(hiveContext.jsonFile(pathListToJSONFile(i).path, dataSchema))
		}
		result
	}

}