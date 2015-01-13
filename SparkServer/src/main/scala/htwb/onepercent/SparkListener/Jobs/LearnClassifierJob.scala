package htwb.onepercent.SparkListener.Jobs

import htwb.onepercent.SparkListener.utils.scoring.{TrainedData, TweetScoringLearner}
import htwb.onepercent.SparkListener.utils.{Config, ErrorMessage, JsonTools, _}
import htwb.onepercent.SparkListener.{JobExecutor, JobResult}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Failure, Success, Try}

case class TrainResult(msg: String) extends JobResult

/**
 *
 */
class LearnClassifierJob extends JobExecutor with Logging {

  type Category = String

  override def executeJob(params: List[String]): JobResult = {
    if (params.length > 0) ErrorMessage("Job does not accept parameters", 100)
    else {
      Try(fetchTrainingData()) match {
        case util.Success(data) => {
          val conf = new SparkConf().setAppName("tweet scoring").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
          val sc = new SparkContext(conf)
          val tweetScoringLearner = new TweetScoringLearner(sc)
          val trainedData: TrainedData = tweetScoringLearner.learn(data)
          Try(JsonTools.writeToFileAsJson(trainedData, Config.get.scoringTrainedDataPath)) match {
            case Success(_) =>
              sc.stop()
              TrainResult("Trained and written Data successfully")
            case Failure(_) =>
              sc.stop()
              log("executeJob", "Failed to write trained Data to: " + Config.get.scoringTrainedDataPath)
              ErrorMessage("Failed to write trained Data to: " + Config.get.scoringTrainedDataPath, 101)
          }
        }
        case Failure(ex) =>
          log("executeJob", "Failed to fetch training Data: " + ex)
          ErrorMessage("Failed to fetch training Data: " + ex, 101)
      }

    }
  }

  private def fetchTrainingData(): Map[Category, List[String]] = {
    ScoringTrainingSample.trainingSet()
  }
}
