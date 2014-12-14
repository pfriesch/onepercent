package tat.SparkListener.Jobs

//TODO: Patrick please make some documentation!

<<<<<<< HEAD
/**
 * Own imports
 */

import tat.SparkListener.utils.{TypeValidator, ErrorMessage, TypeCreator}
import tat.SparkListener.{JobResult, JobExecutor}
=======
//Own imports
import tat.SparkListener.JobExecutor
>>>>>>> 8eb2d5232009fc226d90076015edc48d78e13f4f
import tat.SparkListener.utils.ApacheSparkController

class ApacheSparkJob extends JobExecutor {

  override def executeJob(params: List[String]): JobResult = {
    val asc = new ApacheSparkController()
    asc.execute(params(0))
  }
  
}
