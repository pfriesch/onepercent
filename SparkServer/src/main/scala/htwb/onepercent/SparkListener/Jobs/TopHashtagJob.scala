package htwb.onepercent.SparkListener.Jobs

//Scala imports

import scala.util.{Failure, Success, Try}

//JAVA imports

import java.text.SimpleDateFormat

//Spark imports

import org.apache.spark.sql.hive._
import org.apache.spark.{SparkConf, SparkContext}

//Own imports

import htwb.onepercent.SparkListener.utils.Types.TypeCreator
import htwb.onepercent.SparkListener.utils._
import htwb.onepercent.SparkListener.{JobResult, JobExecutor}

/**
 * This class is a job for calculating the TopHashtags.
 *
 * @author Florian Willich
 */

class TopHashtagJob extends JobExecutor with Logging {

  /**
   * This method analysis one hour of tweets to extract the top hashtags.
   *
   *
   * @param     params        Have to be as follows:
   *                          List element 0: Timestamp <yyyy-mm-dd hh:mm:ss>
   *                          List element 1: Natural Number (Integer) defining the top X
   *
   * @return    The result of the analysis that looks like follow:
   *            TopHashtags @see { TweetAnalyser }
   *
   *            Or errors if there has been something going wrong:
   *
   *            If the timestamp is not a valid date:
   *            ErrorMessage("Parameter X is not a valid date!", 100)
   *
   *            If the created path is not valid:
   *            ErrorMessage("Parameter X i not a valid path!", 100)
   *
   *            If top X can not be cast to an Integer:
   *            ErrorMessage("Parameter X is not an Integer!", 100)
   *
   *            If there was something going wrong in the analysis:
   *            ErrorMessage("TopHashtag analyses failed!", 100)
   *
   * @author    Florian Willich
   */
  override def executeJob(params: List[String]): JobResult = {


    Try(TypeCreator.createGregorianCalendar(params(0), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))) match {
      case Success(gregCalendar) =>

        Try(TypeCreator.createClusterPath(Config.get.tweetsPrefixPath, gregCalendar, "*.data")) match {
          case Success(path) =>
            val topX = params(1).toInt
            val conf = new SparkConf().setAppName("Twitter Hashtags Top 10").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
            val sc = new SparkContext(conf)
            val hc = new HiveContext(sc)
            val ta = new TweetAnalyser(sc, hc)

            log("executeJob", "Starting Anaylsis with path: " + path.path + " and topX: " + topX)

            //Please notice the JSONFileReader which is used to create a schema for the topHashtagAnalyser
            //method
            Try(ta.topHashtagAnalyser(new TweetJSONFileReader(sc, hc).readFile(path.path), topX)) match {
              case Success(result) =>
                //stop the spark context, otherwise its stuck in this context...
                sc.stop()
                log("executeJob", "End Anaylsis with path: " + path.path + " and topX: " + topX)
                result
              case Failure(_) =>
                //stop the spark context, otherwise its stuck in this context...
                sc.stop()
                log("executeJob", "TopHashtag analyses failed! path[" + path.path + "] topX[" + topX + "]")
                ErrorMessage("TopHashtag analyses failed!", 101);
            }
          case Failure(wrongPath) =>
            ErrorMessage("Parameter [" + wrongPath + "] i not a valid path!", 100)
        }
      case Failure(wrongDate) =>
        ErrorMessage("Parameter [" + wrongDate + "] is not a valid date!", 100)
    }
  }
}
