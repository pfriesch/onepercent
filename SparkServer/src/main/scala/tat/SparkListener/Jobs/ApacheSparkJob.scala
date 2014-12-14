package tat.SparkListener.Jobs

//TODO: Patrick please make some documentation!


/**
 * Own imports
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
