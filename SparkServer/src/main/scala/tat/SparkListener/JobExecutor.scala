package tat.SparkListener

import akka.actor.Actor
import org.json4s._
import org.json4s.native.JsonMethods._
import native.Serialization.write


/**
 * Result of any Job including a unique jobID and the Result als a String.
 *
 * @param jobID         A unique jobID.
 * @param jobResult     The Result of the Job.
 */
case class Result(jobID: String, jobResult: AnyRef)

case class JobSignature(jobID: String, name: String, params: Array[String], time: String)

case class ExecuteJob(jobID: String, params: Array[String])

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
   * This method is called to run a Spark Job and expected to return a formated json as job result.
   * @see JsonConverter.jobResultToJson
   * @param params
   * @return formated json stirng
   */
  def executeJob(params: Array[String]): AnyRef

}
