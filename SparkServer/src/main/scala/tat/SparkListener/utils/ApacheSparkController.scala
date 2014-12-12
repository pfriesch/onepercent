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


  private def start(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " start").lines_!
    output.mkString
  }

  private def stop(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " stop").lines_!
    output.mkString
  }

  private def restart(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " restart").lines_!
    output.mkString
  }

  private def status(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " status").lines_!
    output.mkString
  }

  private def log(): String ={
    val output = Process("tail -n 20 " + apacheSparkHome + apacheSparkLogFile).lines_!
    output.mkString
  }

  private def debug(): String ={
    val output = Process(apacheSparkHome + apacheSparkInitSkript + " debug").lines_!
    output.mkString
  }
}
