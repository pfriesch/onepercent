
import java.io._
import java.net.Socket


/**
 * Created by plinux on 17/11/14.
 * params:
 *
 * "2014-11-20 05:00:00"  "/studenten/s0540031/tweets/" "10"
 *
 *
 * */
object TestClient {

  def main (args: Array[String]) {

    val socket = new Socket("hadoop03.f4.htw-berlin.de",5555)
    println("test1")
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    println("test2")
    val out = new PrintWriter(socket.getOutputStream, true)

//    out.println("{\"job\": \"hashtagtop10\", \"params\":[\"Value1\",\"Value2\"], \"time\":\"0000-00-00 00:00:00\", \"ip\":\"<host-name>\", \"port\":5555}")
    //out.println("{\"job\": \"realTopOfThePops\", \"params\":[\"" + args(0) + "\",\"" + args(1) + "\"], \"time\":\"0000-00-00 00:00:00\", \"ip\":\"<host-name>\", \"port\":5555}")
    println("test3")
    println("gesendet: {\"jobID\":\"superJobID123\",\"name\":\"TopHashtagJob\",\"params\":[\""+args(0)+"\",\""+args(1)+"\",\""+args(2)+"\"],\"time\":\"0000-00-00 00:00:00\"}")
    out.println("{\"jobID\":\"superJobID123\",\"name\":\"TopHashtagJob\",\"params\":[\""+args(0)+"\",\""+args(1)+"\",\""+args(2)+"\"],\"time\":\"0000-00-00 00:00:00\"}")

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
