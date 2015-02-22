/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent

import org.apache.spark.{SparkConf, SparkContext}

/**
 * The Environment wich holds the SparkContext which is needed to make use of spark RDDs.
 */
object Env {

  /**
   * The used spark configuration.
   */
  val conf = new SparkConf().setAppName("onepercent").set("spark.executor.memory", "24G").set("spark.cores.max", "90")
  /**
   * The global spark context.
   * @todo make a single spark context for every executed job.
   */
  val sc = new SparkContext(conf)


}
