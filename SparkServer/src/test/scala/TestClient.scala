
import java.io._
import java.net.Socket


/**
 * Created by plinux on 17/11/14.
 * params:
 *
 * "2015-11-20 05:00:00"  "/studenten/s0540031/tweets/" "10"
 *
 *
 **/
object TestClient {

  def main(args: Array[String]) {

    val socket = new Socket("localhost", 5556)
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val out = new PrintWriter(socket.getOutputStream, true)

    //out.println(genJobString2("1", "TopHashtagJob", List("2015-01-15 10:00:00", "9")))
    //out.println(genJobString1("2", "TweetsAtDaytimeJob", List("2015-01-15 10:00:00")))


    out.println(genJobString1("31", "WordSearchJob", List("twitter")))
    Thread.sleep(1)
    out.println(genJobString1("32", "WordSearchJob", List("twitter")))
    Thread.sleep(1)
    out.println(genJobString1("33", "WordSearchJob", List("twitter")))

    //    out.println(genJobString1("4", "OriginTweetsJob", List("2015-01-15 10:00:00")))
    //    out.println(genJobString1("5", "LanguageDistributionJob", List("2015-01-15 10:00:00")))
    //    out.println(genJobString0("6", "LearnClassifierJob", List()))
    //    out.println(genJobString1("7", "CategoryDistributionJob", List("2015-01-15 10:00:00")))


    //out.println("{\"jobID\":\"superJobID1234\",\"name\":\"TestyJob\",\"params\":[\"TestInput\"],\"time\":\"0000-00-00 00:00:00\"}")

    var exit = false
    while (!exit) {
      var string = in.readLine()
      if (string == null) {
        exit = true
      }
      println(string)

    }

  }

  private def genJobString0(id: String, name: String, params: List[String]) = {
    "{\"jobID\":\"" + id + "\",\"name\":\"" + name + "\",\"params\":[],\"time\":\"0000-00-00 00:00:00\"}"
  }

  private def genJobString1(id: String, name: String, params: List[String]) = {
    "{\"jobID\":\"" + id + "\",\"name\":\"" + name + "\",\"params\":[\"" + params(0) + "\"],\"time\":\"0000-00-00 00:00:00\"}"
  }

  private def genJobString2(id: String, name: String, params: List[String]) = {
    "{\"jobID\":\"" + id + "\",\"name\":\"" + name + "\",\"params\":[\"" + params(0) + "\",\"" + params(1) + "\" ],\"time\":\"0000-00-00 00:00:00\"}"
  }


}