package tat.SparkListener

import akka.actor.{ActorSystem, Actor, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._
import java.net.InetSocketAddress

import tat.SparkListener.utils.{Config, Settings}

/**
 * This class listens to a port for job requests and passes the requests to the RequestHandler
 * Created by plinux on 12/11/14.
 */
class Listener extends Actor {

  import context.system
  IO(Tcp) ! Bind(self, new InetSocketAddress(Config.get.hostname, Config.get.port))

  def receive = {
    case Bound(localAddress) => //setup
    case CommandFailed(_: Bind) => context stop self
    case Connected(remote, local) =>
      val handler = context.actorOf(Props[JobHandler], name = "JobHandler")
      val connection = sender
      connection ! Register(handler)
      handler ! Register(connection)
    case _ => println("DEBUGG: Listener defualt case")
  }
}

object App {
  def main(args: Array[String]) {
    val system = ActorSystem()
    val listener = system.actorOf(Props[Listener], name = "Listener")
  }
}
