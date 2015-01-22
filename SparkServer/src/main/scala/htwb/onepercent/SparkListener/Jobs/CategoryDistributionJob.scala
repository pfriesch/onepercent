package htwb.onepercent.SparkListener.Jobs

import java.io.File
import java.text.SimpleDateFormat

import htwb.onepercent.SparkListener.utils.Types.TypeCreator
import htwb.onepercent.SparkListener.utils._
import htwb.onepercent.SparkListener.utils.scoring._
import htwb.onepercent.SparkListener.{Env, JobExecutor, JobResult}
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SchemaRDD
import org.apache.spark.sql.hive.HiveContext

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * A single representation of a frequency of a singe category in the tweet set.
 * @param category
 * @param count
 */
case class CategoryCount(category: String, count: Int)

/**
 * List of the frequency of all categories and the total number of counted tweets.
 * @param distribution the category distribution.
 * @param totalCount the total count of counted tweets.
 */
case class CategoryDistribution(distribution: List[CategoryCount], totalCount: Int) extends JobResult

/**
 * Reads all tweets saved in the given hour and extracts all flaged as english. Classifies each tweet based
 * on the given trained data. Returns a List of frequencies for each categories and a number of total computed tweets.
 * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
 * @author pFriesch
 */
class CategoryDistributionJob extends JobExecutor with Logging {

  /**
   * Reads all tweets saved in the given hour and extracts all flaged as english. Classifies each tweet based
   * on the given trained data. Returns a List of frequencies for each categories and a number of total computed tweets.
   * @param params the hour the tweets are classified in.
   * @return a List of frequencies for each categories and a number of total computed tweets.
   */
  override def executeJob(params: List[String]): JobResult = {


    Try(TypeCreator.gregorianCalendar(params(0), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))) match {
      case Success(gregCalendar) =>

        Try(TypeCreator.clusterPath(Config.get.tweetsPrefixPath, gregCalendar, "*.data")) match {
          case Success(path) =>
            val hc = new HiveContext(Env.sc)

            Try(new TweetJSONFileReader(Env.sc, hc).readFile(path.path)) match {
              case Success(schmaRDD) =>
                val file = new File(Config.get.scoring_TrainedDataPath)
                Try(JsonTools.parseClass[TrainedData](Source.fromFile(Config.get.scoring_TrainedDataPath).mkString)) match {
                  case Success(trainedData) =>
                    val classifier = new TweetClassifier(trainedData)
                    schmaRDD.registerTempTable("tweets")
                    val tweetText: SchemaRDD = hc.sql("SELECT text FROM tweets WHERE lang = 'en'")
                    val categoryFreqency1 = tweetText.map(
                      tweetText => classifier.classifyVerbose(tweetText.toString()) /*match {
                        case x: (_, _) if x._2 < Config.get.scoring_Threshold => (Config.get.scoring_OtherCategoryName, x._2)
                        case x => x
                      }*/)
                    categoryFreqency1.takeSample(true, 10).foreach(println)
                    val categoryFreqency2 = categoryFreqency1.map(X => X._2.maxBy(_._2)).groupByKey().map(X => (X._1, X._2.toList.length))
                    val totalTweets: Int = categoryFreqency2.reduce((X, Y) => (X._1, X._2 + Y._2))._2
                    val result = CategoryDistribution(categoryFreqency2.collect().toList.map(X => CategoryCount(X._1, X._2)), totalTweets)
                    result
                  case Failure(ex) =>
                    ErrorMessage("Failed to read trained Data, data might not be learned yet.", 101)
                }
              case Failure(ex) =>
                ErrorMessage("Failed to read Tweets.", 101)
            }
          case Failure(wrongPath) =>
            ErrorMessage("Parameter [" + wrongPath + "] i not a valid path!", 100)
        }
      case Failure(wrongDate) =>
        ErrorMessage("Parameter [" + wrongDate + "] is not a valid date!", 100)
    }
  }
}
