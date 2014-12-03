package tat.SparkListener

import java.io._
import java.net.Socket


/**
 * Created by plinux on 17/11/14.
 * params: "/home/plinux/workspace/Twitter-Analytics-Tool/SparkServer/model/twitter-analytics-tool/sample_data/test.json" "10"
 */
object TestClient {

  def main (args: Array[String]) {
    val socket = new Socket("localhost",5555)
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val out = new PrintWriter(socket.getOutputStream, true)

//    out.println("{\"job\": \"hashtagtop10\", \"params\":[\"Value1\",\"Value2\"], \"time\":\"0000-00-00 00:00:00\", \"ip\":\"<host-name>\", \"port\":5555}")
    //out.println("{\"job\": \"realTopOfThePops\", \"params\":[\"" + args(0) + "\",\"" + args(1) + "\"], \"time\":\"0000-00-00 00:00:00\", \"ip\":\"<host-name>\", \"port\":5555}")

    out.println("{\"jobID\":\"superJobID123\",\"name\":\"TopOfThePops\",\"params\":[\""+args(0)+"\","+args(1)+"\",\""+args(2)+"\"],\"time\":\"0000-00-00 00:00:00\"}")

    //Thread.sleep(1)
    //out.println("{\"jobID\":\"superJobID1234\",\"name\":\"TestyJob\",\"params\":[\"TestInput\"],\"time\":\"0000-00-00 00:00:00\"}")

    var exit = false
    while (!exit){
      var string = in.readLine()
      if (string == null){
        exit = true
      }
      println(string)

    }

  }







}
