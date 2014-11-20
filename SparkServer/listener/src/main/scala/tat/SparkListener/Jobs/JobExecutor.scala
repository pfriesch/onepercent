package tat.SparkListener.Jobs

import akka.actor.{ActorRef, Actor}
import org.json4s.JsonAST.JObject


//case class Do(params: Array[String])
case class ExecuteJob(params: Array[String])
case class Result(result: String)

/**
 * Created by plinux on 17/11/14.
 */
trait JobExecutor extends Actor {

  def receive = {
    
    case ExecuteJob(params) =>

      val result: Result = executeJob(params)
      sender ! result
  }

  def executeJob(params: Array[String]): Result

}
