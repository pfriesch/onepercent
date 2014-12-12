package tat.SparkListener

import akka.actor.{ActorSystem, Actor, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._
import java.net.InetSocketAddress

import tat.SparkListener.utils.{Logging, Config, Settings}

/**
 * This class listens to a port for job requests and passes the requests to the RequestHandler
 * Created by plinux on 12/11/14.
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

object App {

  def main(args: Array[String]) {
    val system = ActorSystem()
    val listener = system.actorOf(Props[Listener])
  }

}
