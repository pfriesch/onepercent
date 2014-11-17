package tat.SparkListener

import java.io._
import java.net.Socket


/**
 * Created by plinux on 17/11/14.
 */
object TestClient {

  def main (args: Array[String]) {
    val socket = new Socket("localhost",5555)
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val out = new PrintWriter(socket.getOutputStream, true)

    out.println("{\"id\":1,\"name\":\"A green door\"}")

    while(true){
      println(in.readLine())
    }


}



}
