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

//    out.println("{\"job\": \"hashtagtop10\", \"params\":[\"Value1\",\"Value2\"], \"time\":\"0000-00-00 00:00:00\", \"ip\":\"<host-name>\", \"port\":5555}")
    //out.println("{\"job\": \"realTopOfThePops\", \"params\":[\"" + args(0) + "\",\"" + args(1) + "\"], \"time\":\"0000-00-00 00:00:00\", \"ip\":\"<host-name>\", \"port\":5555}")

    out.println("{\"jobID\":\"superJobID123\",\"name\":\"TopOfThePops\",\"params\":[\""+args(0)+"\",\""+args(1)+"\"],\"time\":\"0000-00-00 00:00:00\"}")

    while(true){
      println(in.readLine())
    }

  }







}
