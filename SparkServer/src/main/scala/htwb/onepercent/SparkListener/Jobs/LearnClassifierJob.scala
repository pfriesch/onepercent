package htwb.onepercent.SparkListener.Jobs

import java.text.SimpleDateFormat
import java.util.Calendar

import htwb.onepercent.SparkListener.utils.Types.TypeCreator
import htwb.onepercent.SparkListener.utils.scoring.{ScoringTrainingSample, TrainedData, TweetScoringLearner}
import htwb.onepercent.SparkListener.utils.{Config, ErrorMessage, JsonTools, _}
import htwb.onepercent.SparkListener.{Env, JobExecutor, JobResult}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SchemaRDD
import org.apache.spark.sql.hive.HiveContext

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
          val conf = new SparkConf().setAppName("tweet scoring").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
          //          val sc = new SparkContext(conf)
          val tweetScoringLearner = new TweetScoringLearner(Env.sc)
          val trainedData: TrainedData = tweetScoringLearner.learn(data)
          Try(JsonTools.writeToFileAsJson(trainedData, Config.get.scoring_TrainedDataPath)) match {
            case Success(_) =>
              TrainResult("Trained and written Data successfully")
            case Failure(_) =>
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

  //TODO make private

  def fetchTweetTrainingData(): Map[Category, List[String]] = {
    val timeFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentCalendar: Calendar = Calendar.getInstance()
    //set current time 10 mins ago
    currentCalendar.add(Calendar.MINUTE, -10)
    val pastCalendar: Calendar = Calendar.getInstance()
    //get date 14 days ago
    pastCalendar.add(Calendar.DAY_OF_MONTH, -14)
    val startTime: String = timeFormatter.format(currentCalendar.getTime())
    val endTime: String = timeFormatter.format(pastCalendar.getTime())


    Try(TypeCreator.gregorianCalendar(startTime, timeFormatter)) match {
      case Success(startGregCalendar) =>
        Try(TypeCreator.gregorianCalendar(endTime, timeFormatter)) match {
          case Success(endGregCalendar) =>
            Try(TypeCreator.multipleClusterPath(Config.get.tweetsPrefixPath, startGregCalendar, endGregCalendar, "*.data")) match {
              case Success(path) =>
                val hc = new HiveContext(Env.sc)
                val tweetData: SchemaRDD = new TweetJSONFileReader(Env.sc, hc).readFile(path)
                tweetData.registerTempTable("tweets")
                val tweetsHashtags: SchemaRDD = hc.sql("SELECT text, entities.hashtags FROM tweets ")
                tweetsHashtags.printSchema()

                Map("", List(""))

              /*                //hashtags

                              scheme.registerTempTable("tweets")

                              val hashtagsScheme: SchemaRDD = hiveContext.sql("SELECT entities.hashtags FROM tweets")

                              //Temp Table exists as long as the Spark/Hive Context
                              hashtagsScheme.registerTempTable("hashtags")

                              val table: SchemaRDD = hiveContext.sql("SELECT hashtags.text FROM hashtags LATERAL VIEW EXPLODE(hashtags) t1 AS hashtags")

                              //hashtag*/

              case Failure(wrongPath) =>
                throw new IllegalArgumentException("No Data available between " + startTime + " and " + endTime)
            }
          case Failure(wrongEndTime) =>
            throw new IllegalArgumentException("Can not create past Calender")
        }
      case Failure(wrongStartTime) =>
        throw new IllegalArgumentException("Can not create current Calender")
    }
  }

  private def fetchTrainingData(): Map[Category, List[String]] = {
    ScoringTrainingSample.trainingSet()
  }
}

