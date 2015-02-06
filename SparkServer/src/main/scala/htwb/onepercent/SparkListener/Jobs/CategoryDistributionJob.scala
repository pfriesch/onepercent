package htwb.onepercent.SparkListener.Jobs

import java.io.File
import java.text.SimpleDateFormat

import htwb.onepercent.SparkListener.utils.Types.TypeCreator
import htwb.onepercent.SparkListener.utils._
import htwb.onepercent.SparkListener.utils.scoring._
import htwb.onepercent.SparkListener.{Env, JobExecutor, JobResult}
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
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

case class CategoryProb(category: String, prob: Double)

case class TweetWithProb(text: String, categoryProb: List[CategoryProb])

case class CategoryDistributionWithTweets(distribution: List[CategoryCount], totalCount: Int, tweets: Array[TweetWithProb]) extends JobResult

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
                    val TweetWithCondProb = tweetText.map(
                      tweetText => classifier.classifyVerbose(tweetText.toString()))

                    val tweetsWithOtherCategory: RDD[(String, Map[String, Double])] = TweetWithCondProb.map {
                      //tests if a probability in the list is below the threshold
                      // cumpute the average deviation
                      case x if (x._2.map(elem => Math.abs((1.0 / x._2.size.toDouble) - elem._2)).reduce(_ + _) / x._2.size) < Config.get.scoring_Threshold =>
                        (x._1, Map(Config.get.scoring_OtherCategoryName -> 1.0))
                      case x => x
                    }

                    val categoryDistribution = tweetsWithOtherCategory.map(_._2.maxBy(_._2)).groupByKey().map(X => (X._1, X._2.toList.length))
                    val totalTweets: Int = categoryDistribution.reduce((X, Y) => (X._1, X._2 + Y._2))._2
                    //convert for to case class to be able to render as JSON
                    val result = CategoryDistribution(categoryDistribution.collect().toList.map(X => CategoryCount(X._1, X._2)), totalTweets)

                    if (params.size >= 2) {
                      Try(params(1).toInt) match {
                        case Success(sampleSize) =>
                          //convert for to case class to be able to render as JSON
                          Try(tweetsWithOtherCategory.filter(X => !X._2.contains(Config.get.scoring_OtherCategoryName)).filter(X => X._2.exists(Y => Y._2 > Config.get.scoring_MinProbForResult)).takeSample(false, sampleSize).map(X => (X._1, X._2.toList))) match {
                            case Success(tweetSample) => CategoryDistributionWithTweets(result.distribution, result.totalCount, tweetSample.map(X => TweetWithProb(X._1, X._2.map(X => CategoryProb(X._1, X._2)))))
                            //no int parasble
                            case Failure(_) => ErrorMessage("Parameter [" + params(1) + "] is not a valid sampleSize!", 100)
                          }

                        case Failure(_) => ErrorMessage("Parameter [" + params(1) + "] is not a valid sampleSize!", 100)
                      }
                    } else {
                      result
                    }

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
