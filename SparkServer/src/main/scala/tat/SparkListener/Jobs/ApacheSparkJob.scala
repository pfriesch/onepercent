package tat.SparkListener.Jobs

/**
 * Job to control the Apache Spark Service through the Scala Application
 */

import tat.SparkListener.utils.{TypeValidator, ErrorMessage}
import tat.SparkListener.{JobResult, JobExecutor}

import tat.SparkListener.utils.ApacheSparkController

class ApacheSparkJob extends JobExecutor {

  override def executeJob(params: List[String]): JobResult = {
    val asc = new ApacheSparkController()
    asc.execute(params(0))
  }
  
}
