package htwb.onepercent.SparkListener.Jobs

/**
 * Job to control the Apache Flume Service through the Scala Application.
 */

import htwb.onepercent.SparkListener.utils.ApacheFlumeController
import htwb.onepercent.SparkListener.{JobExecutor, JobResult}

class ApacheFlumeJob extends JobExecutor {

  override def executeJob(params: List[String]): JobResult = {
    val afc = new ApacheFlumeController()
    afc.execute(params(0))
  }

}