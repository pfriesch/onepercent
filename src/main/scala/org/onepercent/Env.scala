/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by Pius on 19.01.2015.
 */
object Env {

  val conf = new SparkConf().setAppName("onepercent").set("spark.executor.memory", "24G").set("spark.cores.max", "90")
  val sc = new SparkContext(conf)


}
