/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent

//Scala imports

import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp.{PeerClosed, Received, Register, Write}
import akka.util.ByteString

import scala.util.Try

//Own imports

import org.onepercent.utils._


/**
 * Reads a single connection and handles the incomming data.
 * The incomming data is expected to be a JSON with the structure of a JobSignature.
 * This starts a SparkJob based on the JobSignature and writes the Result to the connection in the structure of a Result
 * @author pFriesch
 */
class JobHandler extends Actor with Logging {

  /**
   * The connection object, handled by this handler.
   */
  var connection: ActorRef = null

  /**
   * When data arrives at the connection and the data is parsable and fitting to a existing Job, a Job is started.
   * Otherwise it returns a ErrorMessage.
   * When a Result is received it will be written to the connection.
   * @return
   */
  def receive = {
    case Received(data) => {
      //tries to parse a JobSignature from received data
      Try(JsonTools.parseClass[JobSignature](data.decodeString("UTF-8"))) match {
        case util.Success(job) =>
          log("receive", "New Job: " + job)
          //builds fully qualified class name of requested hob
          val fullJobName = Config.get.JobsPackageString + job.name
          //tries to start a new Actor for the requested job
          Try(context.actorOf(Props(Class.forName(fullJobName).asInstanceOf[Class[JobExecutor]]))) match {
            case util.Success(jobActor) => jobActor ! ExecuteJob(job.jobID, job.params)
            case util.Failure(ex) =>
              self ! Result(job.jobID, ErrorMessage("Job not known! Job name: " + job.name, 400))
          }
        case util.Failure(ex) =>
          self ! Result("", ErrorMessage("Unable to resolve request! Parse exception: " + ex.getMessage, 404))
      }
    }
    //sends jobresult back
    case r@Result(_, jobResult: AnyRef) =>
      connection ! Write(ByteString.apply(JsonTools.toJsonString(r) + "\n"))
    case PeerClosed => context stop self
    case Register(connection: ActorRef, _, _) => this.connection = connection
    case _ => log("receive", "JobHanlder default case triggered")

  }


}
