package tat.SparkListener.Jobs

import scala.util.{Failure, Success, Try}

/**
 * Own imports
 */

import tat.SparkListener.utils.{TypeValidator, ErrorMessage, TypeCreator}
import tat.SparkListener.JobExecutor
import tat.SparkListener.utils.ApacheFlumeController

class ApacheFlumeJob extends JobExecutor {

  override def executeJob(params: List[String]): AnyRef = {

    Try(params(0).toString) match {
      case Success(method) =>
        val afc = new ApacheFlumeController();
        afc.execute(method);

      case Failure(wrong) =>
        ErrorMessage("Parameter [" + wrong + "] cannot be cast to String", 100)

    }
  }

}
