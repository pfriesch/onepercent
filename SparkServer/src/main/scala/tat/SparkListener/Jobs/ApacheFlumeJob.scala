package tat.SparkListener.Jobs

//Own imports
import tat.SparkListener.JobExecutor
import tat.SparkListener.utils.ApacheFlumeController

class ApacheFlumeJob extends JobExecutor {

  override def executeJob(params: List[String]): AnyRef = {
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
