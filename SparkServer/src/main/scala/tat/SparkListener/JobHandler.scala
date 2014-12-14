package tat.SparkListener

//Scala imports
import akka.actor.{ActorRef, Props, Actor}
import akka.io.Tcp.{Register, Write, PeerClosed, Received}
import akka.util.ByteString
import scala.util.Try

//Own imports
import tat.SparkListener.utils._

//TODO: Please make some documentation Pius

/**
 * Created by plinux on 12/11/14.
 */
class JobHandler extends Actor with Logging {

  var connection: ActorRef = null

  def evaluateJob(jsonString: String): Try[JobSignature] = {
    //TODO check params
    JsonConverter.parseJobJson(jsonString)
  }

  def receive = {
    case Received(data) => {
      evaluateJob(data.decodeString("UTF-8")) match {
        case util.Success(job) =>
          log("receive", "New Job: " + job)
          val fullJobName = Config.get.JobsPackageString + job.name
          Try(context.actorOf(Props(Class.forName(fullJobName).asInstanceOf[Class[JobExecutor]]))) match {
            case util.Success(jobActor) => jobActor ! ExecuteJob(job.jobID, job.params)
            case util.Failure(ex) =>
              self ! Result(job.jobID, ErrorMessage("Job not known! Job name: " + job.name, 400))
          }
        case util.Failure(ex) =>
          //TODO What to do if json read failed?
          self ! Result("", ErrorMessage("Unable to resolve request! Parse exception: " + ex.getMessage, 404))
      }
    }

    case r@Result(_, jobResult: AnyRef) =>
      //TODO a "\n" is bad, alternative? (\n => end of Message)
      connection ! Write(ByteString.apply(JsonConverter.caseClassToJson(r) + "\n"))
    case PeerClosed => context stop self
    case Register(connection: ActorRef, _, _) => this.connection = connection
    case _ => log("receive", "JobHanlder default case triggered")

  }


}
