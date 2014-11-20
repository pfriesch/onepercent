package tat.SparkListener


import akka.actor.{ActorSystem, Actor, Props}
import akka.io.{IO, Tcp}
import akka.io.Tcp._
import java.net.InetSocketAddress


/**
 * This class listens to a port for job requests and passes the requests to the RequestHandler
 * Created by plinux on 12/11/14.
 */
class Listener extends Actor {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 5555))

  def receive = {
    case Bound(localAddress) => //setup
    case CommandFailed(_: Bind) => context stop self
    case c @ Connected(remote, local) =>
      val handler = context.actorOf(Props[JobHandler], name = "JobHandler")
      val connection = sender
      connection ! Register(handler)
  }
}

object App {
  def main(args: Array[String]) {
    val system = ActorSystem("system")
    val listener = system.actorOf(Props[Listener], "listenerActor")
  }
}
