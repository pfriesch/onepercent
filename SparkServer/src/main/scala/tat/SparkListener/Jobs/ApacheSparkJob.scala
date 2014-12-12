package tat.SparkListener.Jobs

import scala.util.{Failure, Success, Try}

/**
 * Own imports
 */

import tat.SparkListener.utils.{TypeValidator, ErrorMessage, TypeCreator}
import tat.SparkListener.JobExecutor
import tat.SparkListener.utils.ApacheSparkController

case class ApacheSparkResult(output: String)

class ApacheSparkJob extends JobExecutor {

  override def executeJob(params: List[String]): AnyRef = {
    val asc = new ApacheSparkController();
    asc.execute(method);
  }
  
}
