package htwb.onepercent.SparkListener.Jobs

import htwb.onepercent.SparkListener.utils.scoring.{TrainedData, TweetScoringLearner}
import htwb.onepercent.SparkListener.utils.{Config, ErrorMessage, JsonTools, _}
import htwb.onepercent.SparkListener.{JobExecutor, JobResult}
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Failure, Success, Try}

case class TrainResult(msg: String) extends JobResult

/**
 * Learns a given set of training data for scoring tweets.
 * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
 * @author pFriesch
 */
class LearnClassifierJob extends JobExecutor with Logging {

  type Category = String

  /**
   * Fetches given training data, computes a category probability and a term probability of the training data.
   * The trained data is saved as json to the scoring_TrainedDataPath as given in the config.
   * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
   * @param params the params of the specified job.
   * @return a positive jobResult or an ErrorMessage if an error occurred while executing
   */
  override def executeJob(params: List[String]): JobResult = {
    if (params.length > 0) ErrorMessage("Job does not accept parameters", 100)
    else {
      Try(fetchTrainingData()) match {
        case util.Success(data) => {
          val conf = new SparkConf().setAppName("tweet scoring").set("spark.executor.memory", "2G").set("spark.cores.max", "12").set("spark.driver.allowMultipleContexts", "true")
          val sc = new SparkContext(conf)
          val tweetScoringLearner = new TweetScoringLearner(sc)
          val trainedData: TrainedData = tweetScoringLearner.learn(data)
          Try(JsonTools.writeToFileAsJson(trainedData, Config.get.scoring_TrainedDataPath)) match {
            case Success(_) =>
              sc.stop()
              TrainResult("Trained and written Data successfully")
            case Failure(_) =>
              sc.stop()
              log("executeJob", "Failed to write trained Data to: " + Config.get.scoring_TrainedDataPath)
              ErrorMessage("Failed to write trained Data to: " + Config.get.scoring_TrainedDataPath, 101)
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

