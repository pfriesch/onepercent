package tat.SparkListener.Jobs

import scala.util.{Failure, Success, Try}

/**
 * Own imports
 */

import tat.SparkListener.utils.{TypeValidator, ErrorMessage, TypeCreator}
import tat.SparkListener.{JobResult, JobExecutor}
import tat.SparkListener.utils.ApacheSparkController

class ApacheSparkJob extends JobExecutor {

  override def executeJob(params: List[String]): JobResult = {
    val asc = new ApacheSparkController()
    asc.execute(params(0))
  }
  
}
