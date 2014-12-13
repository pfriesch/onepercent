package tat.SparkListener.Jobs

//TODO: Patrick please make some documentation!

//Own imports
import tat.SparkListener.JobExecutor
import tat.SparkListener.utils.ApacheSparkController

class ApacheSparkJob extends JobExecutor {

  override def executeJob(params: List[String]): AnyRef = {
    val asc = new ApacheSparkController();
    asc.execute(params(0));
  }
  
}
