package tat.SparkListener.Jobs


import scala.util.{Failure, Success, Try}

/**
 * Own imports
 */

import tat.SparkListener.utils.{TypeValidator, ErrorMessage}
import tat.SparkListener.{JobResult, JobExecutor}

import tat.SparkListener.utils.ApacheFlumeController

class ApacheFlumeJob extends JobExecutor {

  override def executeJob(params: List[String]): JobResult = {
    val afc = new ApacheFlumeController()
    afc.execute(params(0))
  }

}

/**
 * Result of the ApacheFlume Job.
 *
 * TODO: Patrick please make some documentation.
 *
 * @param output
 */
case class ApacheFlumeResult(output: String)
