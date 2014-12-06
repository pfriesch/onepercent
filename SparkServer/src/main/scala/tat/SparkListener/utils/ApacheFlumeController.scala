package tat.SparkListener.utils

import sys.process._;

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
  def execute(method: String): String = {
    method match{
      case "start" => start()
      case "stop" => stop()
      case "restart" => restart()
      case "status" => status()
      case "log" => log()
      case _ => "No Method " + method + " available!"
    }
  }


  private def start(): String ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " start").lines_!
    output.mkString
  }

  private def stop(): String ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " stop").lines_!
    output.mkString
  }

  private def restart(): String ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " restart").lines_!
    output.mkString
  }

  private def status(): String ={
    val output = Process(apacheFlumeHome + apacheFlumeInitScript + " status").lines_!
    output.mkString
  }

  private def log(): String ={
    val output = Process("tail -n 5 " + apacheFlumeHome + "logs/flume.TwitterAgent.init.log").lines_!
    output.mkString
  }
}
