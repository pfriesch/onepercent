package tat.SparkListener.Jobs

/**
 * Job to control the Apache Flume Service through the Scala Application.
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