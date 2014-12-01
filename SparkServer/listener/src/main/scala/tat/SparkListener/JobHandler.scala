package tat.SparkListener


import akka.actor.Status.Success
import akka.actor.{ActorRef, Props, Actor}
import akka.io.Tcp
import akka.io.Tcp.{Register, Write, PeerClosed, Received}
import akka.util.ByteString
import org.apache.tools.ant.taskdefs.Execute
import org.json4s._
import org.json4s.native.JsonMethods._
import tat.SparkListener.Jobs.Types._
import tat.SparkListener.Jobs._
import org.json4s._
import org.json4s.native.JsonMethods._
import native.Serialization.write
import scala.util
import scala.util.{Failure, Try}
import tat.SparkListener.Jobs.TopHashtagJob


case class Error(error: String)

/**
 * Created by plinux on 12/11/14.
 */
class JobHandler extends Actor {

  var connection: ActorRef = null

  def evaluateJob(jsonString: String): Try[JobSignature] = {
    //TODO check params
    JsonConverter.parseJobJson(jsonString)
  }

  def receive = {
    case Received(data) => {
      evaluateJob(data.decodeString("UTF-8")) match {
        case util.Success(job) =>
          val fullJobName = "tat.SparkListener.Jobs." + job.name
          Try(context.actorOf(Props(Class.forName(fullJobName).asInstanceOf[Class[JobExecutor]]))) match {
            case util.Success(jobActor) => jobActor ! ExecuteJob(job.jobID, job.params)
            case util.Failure(ex) =>
              self ! Result(job.jobID, JsonConverter.jobResultToJson(
                Error("Job not known! Job name: " + job.name)))
          }
        case util.Failure(ex) =>
          //TODO What to do if json read failed?
          self ! Result("", JsonConverter.jobResultToJson(
            Error("Unable to resolve request! Parse exception: " + ex.getMessage)))
      }
    }
    case r@Result(_, jobResult: String) =>
      implicit val formats = native.Serialization.formats(NoTypeHints)
      //TODO a "\n" is bad, alternative? (why you need \n?)
      connection ! Write(ByteString.apply(write(r) + "\n"))
    case PeerClosed => context stop self
    case Register(connection: ActorRef, _, _) => this.connection = connection
    case _ => println("DEBUGG: JobHanlder default case triggered")

  }


}
