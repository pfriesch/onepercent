package tat.SparkListener

import akka.actor.Actor
import org.json4s._
import org.json4s.native.JsonMethods._
import native.Serialization.write

//TODO: Please make some documentation Pius

case class Result(jobID: String, jobResult: JobResult)

case class JobSignature(jobID: String, name: String, params: List[String], time: String)

case class ExecuteJob(jobID: String, params: List[String])

trait JobResult

/**
 * Each Job needs to implement this trait and the executeJob method.
 */
trait JobExecutor extends Actor {

  def receive = {

    case ExecuteJob(jobID, params) =>
      val result: Result = Result(jobID, executeJob(params))
      sender ! result
  }


  /**
   * Runs a spark Job and returns the result in a case class structure
   * @param params the params of the specified job
   * @return the result as a case class
   */
  def executeJob(params: List[String]): JobResult

}
