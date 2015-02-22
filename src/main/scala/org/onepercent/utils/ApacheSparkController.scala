/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.utils


import org.onepercent.JobResult


import sys.process._

/**
 * Represents a message which is based on the initiated method.
 * @param output Message based on the initiated method.
 */
case class ApacheSparkResult(output: String) extends JobResult

/**
 *  Class to control the Apache Spark service.
 * @author Patrick Mariot
 *
 * http://alvinalexander.com/scala/scala-execute-exec-external-system-commands-in-scala
 **/
class ApacheSparkController() {

  val apacheSparkHome: String = "/home/05/40031/spark/"
  val apacheSparkInitSkript: String = "spark-agent.sh"
  val apacheSparkLogFile: String = "logs/org.onepercent.App.log"

  /**
   *  Decides which method to run and calls the assigned method.
   *
   *  @param method String that contains the desired method
   *  @return Output of the function
   */
  def execute(method: String): JobResult = {
    method match{
      case "restart" => restart()
      case "status" => status()
      case "log" => log()
      case "debug" => debug()
      case "update" => update()
      case _ => ErrorMessage("No Method " + method + " available!", 100)
    }
  }

  /**
   * Runs the start Method from the apacheSparkInitSkript, to start the Apache Spark Service.
   * @return Output of the start Method
   */
  private def start(): ApacheSparkResult ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " start").lines_!
    ApacheSparkResult(output.mkString)
  }

  /**
   * Runs the stop Method from the apacheSparkInitSkript, to stop the Apache Spark Service.
   * @return Output of the stop Method
   */
  private def stop(): ApacheSparkResult ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " stop").lines_!
    ApacheSparkResult(output.mkString)
  }

  /**
   * Runs the restart Method from the apacheSparkInitSkript, to restart the Apache Spark Service.
   * May return nothing, cause the Service gets restarted.
   * @return Output of the restart Method
   */
  private def restart(): ApacheSparkResult ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " restart").lines_!
    ApacheSparkResult(output.mkString)
  }

  /**
   *  Runs the status Method from the apacheSparkInitSkript, to list in which state the Apache Spark Service is.
   * @return Output of the status Method
   */
  private def status(): ApacheSparkResult ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " status").lines_!
    ApacheSparkResult(output.mkString)
  }

  /**
   *  Shows the Log of the Apache Spark Service.
   * @return Outputs the last 20 Lines of the status Method
   */
  private def log(): ApacheSparkResult ={
    val output = Process("tail -n 20 " + apacheSparkHome + apacheSparkLogFile).lines_!
    ApacheSparkResult(output.mkString)
  }

  /**
   * Runs the debug Method from the apacheSparkInitSkript, to show the debug messages of the Apache Spark Service.
   * @return Outputs the last 20 Lines that start with '### DEBUG ###'
   */
  private def debug(): ApacheSparkResult ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " debug").lines_!
    ApacheSparkResult(output.mkString)
  }

  /**
   * Runs the update Method from the apacheSparkInitSkript, to update the Apache Spark Service.
   * May return nothing, cause the Service gets restarted.
   * @return Output of the update Method
   */
  private def update(): ApacheSparkResult ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " update").lines_!
    ApacheSparkResult(output.mkString)
  }
}
