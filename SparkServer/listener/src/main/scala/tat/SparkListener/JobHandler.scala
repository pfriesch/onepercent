package tat.SparkListener


import akka.actor.{ActorRef, Props, Actor}
import akka.io.Tcp
import akka.io.Tcp.{Register, Write, PeerClosed, Received}
import akka.util.ByteString
import org.apache.tools.ant.taskdefs.Execute
import org.json4s._
import org.json4s.native.JsonMethods._
import tat.SparkListener.Jobs._

//JobSignature JSON representation
case class JobSignature(job: String, params: Array[String], time: String, ip: String, port: Int)

/**
 * Created by plinux on 12/11/14.
 */
class JobHandler extends Actor {

  var connection: ActorRef = null

  def receive = {

    case Received(data) =>
      val jobSignature: JobSignature = evaluateJob(data.decodeString("UTF-8"))

      match {

        case jobSignature.job == "hashtagtop10" =>
          val jobActor = context.actorOf(Props[Jobs.Top10HashtagsJobExecutor], name = jobSignature.job)
          jobActor ! ExecuteJob(jobSignature.params)

        case jobSignature.job == "realTopOfThePops" =>
          val jobActor = context.actorOf(Props[Jobs.RealTopOfThePops], name = jobSignature.job)
          jobActor ! ExecuteJob(jobSignature.params)

        case _ =>
          println("ERROR: Job is not known!")

      }


    case Result(text) =>
      //TODO a "\n" is bad, alternative?
      connection ! Write(ByteString.apply(text + "\n"))
    case PeerClosed => context stop self
    case Register(connection: ActorRef) => this.connection => connection
    case Connected() =>
      connection = sender
    case _ => println("JobHanlder default case triggered")

  }

  def evaluateJob(jsonString: String) : JobSignature = {
    implicit val formats = DefaultFormats
    return parse(jsonString).extract[JobSignature]
  }

}
