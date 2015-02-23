/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import org.onepercent.utils.{Config, Logging}
import org.apache.log4j.Logger
import org.apache.log4j.Level


/**
 * Listens for connections and starts a JobHandler for every new connection.
 *
 * @author Pius Friesch
 */
class Listener extends Actor with Logging {

  import context.system

  // Binds a Socket to listen for new connections
  IO(Tcp) ! Bind(self, new InetSocketAddress(Config.get.hostname, Config.get.port))

  def receive = {
    // Socket successfully bound
    case Bound(localAddress) =>
      log("receive", "Bound Port on: " + Config.get.hostname + ":" + Config.get.port)
    // failed to bind socket
    case CommandFailed(_: Bind) =>
      log("receive", "Failed to bind Port on: " + Config.get.hostname + ":" + Config.get.port)
      context stop self
      System.exit(-1)
    // a new connection has been opened
    case Connected(remote, local) =>
      // a new JobHandler for the new connection is created
      val handler = context.actorOf(Props[JobHandler], name = "JobHandler$" + remote.getHostName + ":" + remote.getPort)
      // an akka tcp sockets needs to have a receiver for incomming data
      val connection = sender
      connection ! Register(handler)
      // the connection is registered at the handler so it can write back
      handler ! Register(connection)
    case _ => log("receive", "Listener default case triggered")
  }

}

/**
 * This program analyses twitter tweets with various analysis methods and returns the result as a JSON File.
 * The interaction with this program is also based on JSON Files. Each time a JSON File is send, the program will create
 * a new process (Akka), compute the result, returning the result and afterwards kills the process. This leads to a
 * highly scalable architecture where you do not have to take care about parallel requests for data.
 *
 * To create a "Job" the following signature of the JSON file is required:
 *
 * {
 *    "jobID":"?jobIDAsHash?",
 *    "name":"?name?",
 *    "params":["?value?","?value?",...],
 *    "time":"?yyyy-mm-dd hh:mm:ss?"
 * }
 *
 * jobID: A unique Job ID
 * name: Job Identifier
 * params: Various parameters for the Job
 * time: A timestamp where this JSON File was created or the Job was requested.
 *
 * When the program has computed the result successfully, the following JSON File will be returned:
 *
 * {
 *    "jobID":"?jobIDAsHash?",
 *    "jobResult":
 *    {
 *      ?result?
 *    }
 * }
 *
 * jobID: The unique Job ID originally set by the user.
 * jobResult: Result object of this Job.
 *
 * If there has been an error while computing or the JSON interface was used wrong the following JSON File will be
 * returned:
 *
 * {
 *    "jobID":"?jobIDAsHash?",
 *    "jobResult":
 *    {
 *      "errorMessage":"?message?",
 *      "errorCode": ?Code?
 *    }
 * }
 *
 * jobID: The unique Job ID originally set by the user.
 * error: The error message.
 *
 * The following error messages exists:
 *
 * When there is any job parameter that does not fit to the job itself or any other problems with a job parameter:
 * errorMessage: Wrong job parameter
 * errorCode: 100
 *
 * When there has been a error while executing the job e.g. in the analysis method or problems by allocating resources:
 * errorMessage: Job execution failed
 * errorCode: 101
 *
 * When the requested job does not exist:
 * errorMessage: Job not known or found
 * errorCode: 400
 *
 * When there is any problem with parsing the send JSON file:
 * errorMessage: Json not parseable
 * errorCode: 404
 *
 * The following jobs are implemented and can be used:
 *
 * TopHashtagJob (analysis the top hashtag of an hour):
 * param1: "?yyyy-mm-dd hh:mm:ss?" -> the date/hour on which the top hashtag shall be computed
 * param2: "?topX?" -> the top count e.g. 10, 15, 40 ...
 *
 * Result:
 * {
 *    "topHashtags":
 *    [
 *      {"hashtag":"?hashtag?","count":?count?}
 *      {"hashtag":"?hashtag?","count":?count?}
 *    ],
 *    countAllHashtags:?countAllCountedHashtags?
 * }
 *
 * ApacheFlumeJob:
 * param1: "?method?" -> the method you want to execute. Available: start, stop, restart, status, log
 *
 * Result:
 * {
 *    "output":"?method output?"
 * }
 *
 * ApacheSparkJob:
 * param1: "?method?" -> the method you want to execute. Available: restart, status, log, debug, update
 *
 * Result:
 * {
 *    "output":"?method output?"
 * }
 *
 *
 *
 */
object App {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.WARN)
    val system = ActorSystem()
    val listener = system.actorOf(Props[Listener])
  }

}
