package tat.SparkListener


import akka.actor.{ActorRef, Props, Actor}
import akka.io.Tcp
import akka.io.Tcp.{Register, Write, PeerClosed, Received}
import akka.util.ByteString
import org.apache.tools.ant.taskdefs.Execute
import org.json4s._
import org.json4s.native.JsonMethods._
import tat.SparkListener.Jobs._

//Job JSON representation
case class Job(job: String, params: List[String], time: String, ip: String, port: Int)

/**
 * Created by plinux on 12/11/14.
 */
class JobHandler extends Actor {

  var connection: ActorRef = null

  def receive = {
    case Received(data) =>
      connection = sender
      handleJSONJob(data.decodeString("UTF-8"))
    case Result(text) =>
      //TODO a "\n" is bad, alternative?
      connection ! Write(ByteString.apply(text + "\n"))
    case PeerClosed => context stop self
    case _ => println("JobHanlder default case triggered")

  }

  def handleJSONJob(jsonString: String) = {
    implicit val formats = DefaultFormats
    val job = parse(jsonString).extract[Job]
    //TODO: eval job request and find the right job
    val jobActor = context.actorOf(Props[Jobs.Top10HashtagsJobExecutor], name = job.job)
    jobActor ! ExecuteJob(job.params)
  }

}
