/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package htwb.onepercent.SparkListener.utils


import htwb.onepercent.SparkListener.JobResult

import sys.process._


case class ApacheFlumeResult(output: String) extends JobResult


/**
 *  Class to control the Apache Flume service.
 * @author Patrick Mariot
 *
 * http://alvinalexander.com/scala/scala-execute-exec-external-system-commands-in-scala
 **/
class ApacheFlumeController() {

  val apacheFlumeHome: String = "/home/05/40031/apache-flume/"
  val apacheFlumeInitScript: String = "flume-ng-agent.sh"

  /**
   *  Decides which method to run and calls the assigned method.
   *
   *  @param method String that contains the desired method
   *  @return Output of the function
   */
  def execute(method: String): JobResult = {
    method match{
      case "start" => start()
      case "stop" => stop()
      case "restart" => restart()
      case "status" => status()
      case "log" => log()
      case _ => ErrorMessage("No Method " + method + " available!", 100)
    }
  }

  /**
   *  Runs the start Method from the apacheFlumeInitScript, to start the Apache Flume Service.
   * @return Output of the start Method
   */
  private def start(): ApacheFlumeResult ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " start").lines_!
    ApacheFlumeResult(output.mkString)
  }

  /**
   *  Runs the stop Method from the apacheFlumeInitScript, to stop the Apache Flume Service.
   * @return Output of the stop Method
   */
  private def stop(): ApacheFlumeResult ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " stop").lines_!
    ApacheFlumeResult(output.mkString)
  }

  /**
   *  Runs the restart Method from the apacheFlumeInitScript, to restart the Apache Flume Service.
   * @return Output of the restart Method
   */
  private def restart(): ApacheFlumeResult ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " restart").lines_!
    ApacheFlumeResult(output.mkString)
  }

  /**
   *  Runs the status Method from the apacheFlumeInitScript, to list in which state the Apache Flume Service is.
   * @return Output of the status Method
   */
  private def status(): ApacheFlumeResult ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " status").lines_!
    ApacheFlumeResult(output.mkString)
  }

  /**
   *  Shows the Log of the Apache Flume Service.
   * @return Outputs the last 5 Lines of the status Method
   */
  private def log(): ApacheFlumeResult ={
    val output = Process("tail -n 5 " + apacheFlumeHome + "logs/flume.TwitterAgent.init.log").lines_!
    ApacheFlumeResult(output.mkString)
  }
}
