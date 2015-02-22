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
 * @author pFriesch
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
 *
 */
object App {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.WARN)
    val system = ActorSystem()
    val listener = system.actorOf(Props[Listener])
  }

}
