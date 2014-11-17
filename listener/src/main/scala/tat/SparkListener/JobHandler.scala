package tat.SparkListener


import akka.actor.{ActorRef, Props, Actor}
import akka.io.Tcp
import akka.io.Tcp.{Register, Write, PeerClosed, Received}
import akka.util.ByteString
import org.json4s._
import org.json4s.native.JsonMethods._
import tat.SparkListener.Jobs.{Do, Result, DefaultJob}

//Job JSON representation
case class Bsp(id: Int, name: String)

/**
 * Created by plinux on 12/11/14.
 */
class JobHandler extends Actor {

  var connection : ActorRef = null

  def receive = {
    case Received(data) =>
      connection = sender
      handleJSONJob(data.decodeString("UTF-8"))
    case Result(text) =>
      println(text)
      connection ! Write(ByteString.apply(text))
    case PeerClosed => context stop self
    case _ => println("JobHanlder default case triggered")

  }

  def handleJSONJob(jsonString: String) = {
    val json = parse(jsonString)
    implicit val formats = DefaultFormats
    val bsp = json.extract[Bsp]
    val params = Array(jsonString)
    val childJob = context.actorOf(Props[DefaultJob], name = "DefaultJobName")
    childJob ! Do(params)
  }

}
