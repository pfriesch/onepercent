package htwb.onepercent.SparkListener.Jobs

import htwb.onepercent.SparkListener.utils.{Config, Settings, JsonTools, ErrorMessage}
import htwb.onepercent.SparkListener.utils.scoring.TweetScoringLearner
import htwb.onepercent.SparkListener.{JobResult, JobExecutor}
import org.apache.spark.{SparkContext, SparkConf}

import scala.util
import scala.util.{Failure, Success, Try}

case class TrainResult(msg: String) extends JobResult

/**
 *
 */
class LearnClassifierJob extends JobExecutor {

  type Category = String

  override def executeJob(params: List[String]): JobResult = {
    if (params.length > 0) ErrorMessage("Job does not accept parameters", 100)
    else {
      val conf = new SparkConf().setAppName("tweet scoring").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
      val sc = new SparkContext(conf)
      val tweetScoringLearner = new TweetScoringLearner(sc)

      Try(fetchTrainingData()) match {
        case util.Success(data) => {
          val trainedData = tweetScoringLearner.learn(data)
          Try(JsonTools.writeToFileAsJson(trainedData, Config.get.scoringTrainedDataPath)) match {
            case Success(_) => TrainResult("Trained and written Data successfully")
            case Failure(_) => ErrorMessage("Failed to write trained Data to: " + Config.get.scoringTrainedDataPath, 101)
          }
        }
        case Failure(ex) => ErrorMessage("Failed to fetch training Data: " + ex, 101)
      }

    }
  }

  private def fetchTrainingData(): Map[Category, List[String]] = {
    //TODO fetch real data
    //read from Config.get.scoringTrainingDataPath
    Map("lorem" -> List("Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua At vero eos et accusam et justo duo dolores et ea rebum Stet clita kasd gubergren no sea takimata sanctus est Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet consetetur  sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua At vero eos et accusam et justo duo dolores et ea rebum Stet clita kasd gubergren no sea takimata sanctus est Lorem ipsum dolor sit amet Lorem ipsum dolor sit\namet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat sed diam voluptua At vero eos et accusam et justo duo dolores et ea rebum Stet clita kasd gubergren no sea\ntakimata sanctus est Lorem ipsum dolor sit amet\nDuis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi Lorem ipsum dolor sit amet "),
      "liquor" -> List("Kilchoman spritzer; sloe gin  tobermory sake screwdriver craigellachie slammer royale gin and tonic gummy and coke  Bunnahabhain pepe lopez french 75 sake screwdriver choking hazard madras spritzer Brass monkey painkiller wolfschmitt wild turkey wild turkey dewar scotch whisky aberfeldy tequila slammer  glendronach loch lomond amaretto di saronno  Paddy courvoisier cointreau pink gin  knockdhu chopin paralyzer cosmopolitan ectoplasm courvoisier zombie glengoyne the last word; colorado bulldog \n\nInchgower  jose cuervo; hayride aberfeldy sea breeze  brandy manhattan vodka mcgovern batida chocolate soldier leroux royal bermuda cocktail toro rojo auchentoshan  Speyside bengal vesper martini piscola pisco sour blue hawaii pimm cup  fleischmann tinto de verano rum swizzle  harrogate nights? Bull shot brandy sour kensington court special benromach critty bo  Knockando two fingers southern comfort oban caipirinha kilchoman aberfeldy glogg haig haig  pinch  Tequila sunrise piscola  white horse springbank white horse four score kamikaze  bowmore gordon dailuaine bay breeze  Sake screwdriver farnell polish martini staten island ferry gin and tonic "))
  }
}

class FetchDataFailedException extends Exception
