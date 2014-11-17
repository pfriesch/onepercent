package tat.SparkListener.Jobs

import akka.actor.Actor
import org.json4s.JsonAST.JObject


case class Do(params: Array[String])
case class Result(result: String)

/**
 * Created by plinux on 17/11/14.
 */
class DefaultJob extends Actor {

  def receive = {
    case Do(params) => sender ! Result(params(0).filter(_ >= ' ') + " YAY! it worked!!\n")
  }

}
