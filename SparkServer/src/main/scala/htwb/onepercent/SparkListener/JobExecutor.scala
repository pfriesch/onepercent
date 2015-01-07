/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package htwb.onepercent.SparkListener

import akka.actor.Actor
import org.json4s._
import org.json4s.native.JsonMethods._
import native.Serialization.write


/**
 *
 * @param jobID the jobID given by the WebServer to uniquely identify a Job.
 * @param jobResult the result structure of the Job.
 */
case class Result(jobID: String, jobResult: JobResult)

/**
 *
 * @param jobID the jobID given by the WebServer to uniquely identify a Job.
 * @param name unique name of the Job, needs to be exactly the same as the class name.
 * @param params parameters for the job execute method.
 * @param time the time the job got issued.
 */
case class JobSignature(jobID: String, name: String, params: List[String], time: String)

/**
 *
 * @param jobID the jobID given by the WebServer to uniquely identify a Job.
 * @param params parameters for the job execute method.
 */
case class ExecuteJob(jobID: String, params: List[String])

trait JobResult

/**
 * Each Job needs to implement this trait and the executeJob method.
 * When a ExecuteJob Message is received it the Job will be executed and the Result is Returned to the JobHandler.
 */
trait JobExecutor extends Actor {

  def receive = {

    case ExecuteJob(jobID, params) =>
      val result: Result = Result(jobID, executeJob(params))
      sender ! result
  }


  /**
   * Runs a spark Job and returns the result in a case class structure.
   * @param params the params of the specified job.
   * @return the result as a case class.
   */
  def executeJob(params: List[String]): JobResult

}
