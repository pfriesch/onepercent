package tat.SparkListener.utils

import sys.process._;

/**
 *  Class to control the Apache Spark service.
 * @author Patrick Mariot
 *
 * http://alvinalexander.com/scala/scala-execute-exec-external-system-commands-in-scala
 **/
class ApacheSparkController() {

  val apacheSparkHome: String = "/home/05/40031/spark/"
  val apacheSparkInitSkript: String = "spark-agent.sh"
  val apacheSparkLogFile: String = "logs/tat.SparkListener.App.log"

  /**
   *  Decides which method to run and calls the assigned method.
   *
   *  @param method String that contains the desired method
   *  @return Output of the function
   */
  def execute(method: String): String = {
    method match{
      case "restart" => restart()
      case "status" => status()
      case "log" => log()
      case "debug" => debug()
      case _ => "No Method " + method + " available!"
    }
  }

  /**
   * Runs the start Method from the apacheSparkInitSkript, to start the Apache Spark Service.
   * @return Output of the start Method
   */
  private def start(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " start").lines_!
    output.mkString
  }

  /**
   * Runs the stop Method from the apacheSparkInitSkript, to stop the Apache Spark Service.
   * @return Output of the stop Method
   */
  private def stop(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " stop").lines_!
    output.mkString
  }

  /**
   * Runs the restart Method from the apacheSparkInitSkript, to restart the Apache Spark Service.
   * @return Output of the restart Method
   */
  private def restart(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " restart").lines_!
    output.mkString
  }

  /**
   *  Runs the status Method from the apacheSparkInitSkript, to list in which state the Apache Spark Service is.
   * @return Output of the status Method
   */
  private def status(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " status").lines_!
    output.mkString
  }

  /**
   *  Shows the Log of the Apache Spark Service.
   * @return Outputs the last 20 Lines of the status Method
   */
  private def log(): String ={
    val output = Process("tail -n 20 " + apacheSparkHome + apacheSparkLogFile).lines_!
    output.mkString
  }

  /**
   * Runs the debug Method from the apacheSparkInitSkript, to show the debug messages of the Apache Spark Service.
   * @return Outputs the last 20 Lines that start with '### DEBUG ###'
   */
  private def debug(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " debug").lines_!
    output.mkString
  }
}
