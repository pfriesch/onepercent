/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package htwb.onepercent.SparkListener

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import htwb.onepercent.SparkListener.utils.{Config, Logging}
import org.apache.log4j.Logger
import org.apache.log4j.Level



/**
 * Listens for connections and starts a JobHandler for every connection.
 *
 * @author pFriesch
 */
class Listener extends Actor with Logging {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(Config.get.hostname, Config.get.port))

  def receive = {
    case Bound(localAddress) =>
      log("receive", "Bound Port on: " + Config.get.hostname + ":" + Config.get.port)
    //TODO setup???
    case CommandFailed(_: Bind) =>
      log("receive", "Failed to bind Port on: " + Config.get.hostname + ":" + Config.get.port)
      context stop self
      System.exit(-1)
    case Connected(remote, local) =>
      val handler = context.actorOf(Props[JobHandler], name = "JobHandler$" + remote.getHostName + ":" + remote.getPort)
      val connection = sender
      connection ! Register(handler)
      handler ! Register(connection)
    case _ => log("receive", "Listener default case triggered")
  }

}

/**
 * Main
 */
object App {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.WARN)
    val system = ActorSystem()
    val listener = system.actorOf(Props[Listener])


  }

}
