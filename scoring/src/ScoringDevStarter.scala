package scoring

/**
 * Created by plinux on 30/12/14.
 */
object ScoringDevStarter {
  //  val conf = new SparkConf().setAppName("tweet scoring").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
  //  val sc = new SparkContext(conf)
  //  val hc = new HiveContext(sc)

  def main(args: Array[String]) {
    //    val tweetSchemaRDD : SchemaRDD = hc.jsonFile("/home/plinux/onepercent/SparkServer/tweetsSample")
    //
    //    tweetSchemaRDD.registerTempTable("tweets")
    //
    //    val tweetTexts: SchemaRDD = hc.sql("SELECT text FROM tweets")
    ////    tweetText.printSchema()

    val doc = Map(Category("test") -> List("aaaa aaaa aaaa aaaa aaaa aaaa aaaa aaaa aaaa das ist ein test document dass der test category angehört tests haben viele tests"),
      Category("sport") -> List("aaaa aaaa aaaa aaaa das ist ein sport document dass der sport category angehört sportarten sind fussball basketball hockey"))
    val tweetScoringLearner = new TweetScoringLearner
    val learnMenge = tweetScoringLearner.learn(doc)
    println(learnMenge._1)
    println(learnMenge._2)

  }

}
