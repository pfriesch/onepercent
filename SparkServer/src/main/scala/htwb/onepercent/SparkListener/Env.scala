package htwb.onepercent.SparkListener

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by Pius on 19.01.2015.
 */
object Env {

  val conf = new SparkConf().setAppName("onepercent").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
  val sc = new SparkContext(conf)


}