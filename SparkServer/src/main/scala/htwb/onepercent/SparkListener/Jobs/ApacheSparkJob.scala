package htwb.onepercent.SparkListener.Jobs

/**
 * Job to control the Apache Spark Service through the Scala Application
 */

import htwb.onepercent.SparkListener.utils.{TypeValidator, ErrorMessage}
import htwb.onepercent.SparkListener.{JobResult, JobExecutor}

import htwb.onepercent.SparkListener.utils.ApacheSparkController

class ApacheSparkJob extends JobExecutor {

  override def executeJob(params: List[String]): JobResult = {
    val asc = new ApacheSparkController()
    asc.execute(params(0))
  }
  
}
