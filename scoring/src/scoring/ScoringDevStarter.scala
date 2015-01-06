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

    val doc = Map("lorem" -> List("Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua At vero eos et accusam et justo duo dolores et ea rebum Stet clita kasd gubergren no sea takimata sanctus est Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet consetetur  sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua At vero eos et accusam et justo duo dolores et ea rebum Stet clita kasd gubergren no sea takimata sanctus est Lorem ipsum dolor sit amet Lorem ipsum dolor sit\namet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua At vero eos et accusam et justo duo dolores et ea rebum Stet clita kasd gubergren no sea\ntakimata sanctus est Lorem ipsum dolor sit amet\nDuis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi Lorem ipsum dolor sit amet "),
      "liquor" -> List("Kilchoman spritzer; sloe gin  tobermory sake screwdriver craigellachie slammer royale gin and tonic gummy and coke  Bunnahabhain pepe lopez french 75 sake screwdriver choking hazard madras spritzer Brass monkey painkiller wolfschmitt wild turkey wild turkey dewar scotch whisky aberfeldy tequila slammer  glendronach loch lomond amaretto di saronno  Paddy courvoisier cointreau pink gin  knockdhu chopin paralyzer cosmopolitan ectoplasm courvoisier zombie glengoyne the last word; colorado bulldog \n\nInchgower  jose cuervo; hayride aberfeldy sea breeze  brandy manhattan vodka mcgovern batida chocolate soldier leroux royal bermuda cocktail toro rojo auchentoshan  Speyside bengal vesper martini piscola pisco sour blue hawaii pimm cup  fleischmann tinto de verano rum swizzle  harrogate nights? Bull shot brandy sour kensington court special benromach critty bo  Knockando two fingers southern comfort oban caipirinha kilchoman aberfeldy glogg haig haig  pinch  Tequila sunrise piscola  white horse springbank white horse four score kamikaze  bowmore gordon dailuaine bay breeze  Sake screwdriver farnell polish martini staten island ferry gin and tonic "))
    val tweetScoringLearner = new TweetScoringLearner
    val learned = tweetScoringLearner.learn(doc)
//    println(learned._1)
//    println(learned._2)

    val testString = "Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore"
    val classifier = new TweetClassifier(learned._1,learned._2)
    val classified = classifier.classify(testString)
    println(classified)
    println(classified.maxBy(_._2))


  }

}
