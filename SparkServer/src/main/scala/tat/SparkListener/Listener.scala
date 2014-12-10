package tat.SparkListener

import akka.actor.{ActorSystem, Actor, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._
import java.net.InetSocketAddress

import tat.SparkListener.utils.{Config, Settings, Debug}

/**
 * This class listens to a port for job requests and passes the requests to the RequestHandler
 * Created by plinux on 12/11/14.
 */
class Listener extends Actor {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(Config.get.hostname, Config.get.port))

  def receive = {
    case Bound(localAddress) =>
      Debug.log("Listener", "receive", "Bound Port on: " + Config.get.hostname + ":" + Config.get.port)
    //TODO setup???
    case CommandFailed(_: Bind) =>
      Debug.log("Listener", "receive", "Failed to bind Port on: " + Config.get.hostname + ":" + Config.get.port)
      context stop self
      System.exit(-1)
    case Connected(remote, local) =>
      val handler = context.actorOf(Props[JobHandler], name = "JobHandler")
      val connection = sender
      connection ! Register(handler)
      handler ! Register(connection)
    case _ => Debug.log("Listener", "receive", "Listener default case triggered")
  }

}

object App {

  def main(args: Array[String]) {
    val system = ActorSystem()
    val listener = system.actorOf(Props[Listener], name = "Listener")
  }

}
