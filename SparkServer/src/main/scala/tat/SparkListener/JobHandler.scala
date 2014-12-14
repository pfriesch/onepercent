package tat.SparkListener

//Scala imports
import akka.actor.{ActorRef, Props, Actor}
import akka.io.Tcp.{Register, Write, PeerClosed, Received}
import akka.util.ByteString
import scala.util.Try

//Own imports
import tat.SparkListener.utils._



/**
 * Reads a single connection and handles the incomming data.
 * The incomming data is expected to be a JSON with the structure of a JobSignature.
 * This starts a SparkJob based on the JobSignature and writes the Result to the connection in the structure of a Result
 * @author pFriesch
 */
class JobHandler extends Actor with Logging {

  var connection: ActorRef = null

  private def evaluateJob(jsonString: String): Try[JobSignature] = {
    //TODO check params
    JsonConverter.parseJobJson(jsonString)
  }

  /**
   * When data arrives at the connection and the data is parsable and fitting to a existing Job, a Job is started.
   * Otherwise it returns a ErrorMessage.
   * When a Result is received it will be written to the connection.
   * @return
   */
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
      connection ! Write(ByteString.apply(JsonConverter.toJsonString(r) + "\n"))
    case PeerClosed => context stop self
    case Register(connection: ActorRef, _, _) => this.connection = connection
    case _ => log("receive", "JobHanlder default case triggered")

  }


}
