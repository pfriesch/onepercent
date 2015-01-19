package htwb.onepercent.SparkListener.Jobs

//Scala imports

import org.apache.spark.sql.hive.HiveContext

import scala.util.{Failure, Success, Try}

//JAVA imports

import java.text.SimpleDateFormat

//Spark imports

//Own imports

import htwb.onepercent.SparkListener.utils.Types.TypeCreator
import htwb.onepercent.SparkListener.utils._
import htwb.onepercent.SparkListener.{Env, JobExecutor, JobResult}

/**
 * This class is a job for calculating the TopHashtags.
 *
 * @author Patrick
 */

class LanguageDistributionJob extends JobExecutor with Logging {

  /**
   * This method analysis one hour of tweets to extract the top hashtags.
   *
   *
   * @param     params        Have to be as follows:
   *                          List element 0: Timestamp <yyyy-mm-dd hh:mm:ss>
   *
   * @return    The result of the analysis that looks like follow:
   *            LanguageDistrubution @see { TweetAnalyser }
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
   * @author    Florian Willich / Patrick Mariot
   */
  override def executeJob(params: List[String]): JobResult = {


    Try(TypeCreator.gregorianCalendar(params(0), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))) match {
      case Success(gregCalendar) =>

        Try(TypeCreator.clusterPath(Config.get.tweetsPrefixPath, gregCalendar, "*.data")) match {
          case Success(path) =>

            //            val conf = new SparkConf().setAppName("Twitter Language Distribution").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
            //            val sc = new SparkContext(conf)
            val hc = new HiveContext(Env.sc)
            val ta = new TweetAnalyser(Env.sc, hc)

            log("executeJob", "Starting Anaylsis with path: " + path.path)

            //method
            Try(ta.languageDistribution(new TweetJSONFileReader(Env.sc, hc).readFile(path.path), params(0))) match {
              case Success(result) =>
                //stop the spark context, otherwise its stuck in this context...
                log("executeJob", "End Anaylsis with path: " + path.path)
                result
              case Failure(_) =>
                //stop the spark context, otherwise its stuck in this context...
                log("executeJob", "LanguageDistribution analyses failed! path[" + path.path + "]")
                ErrorMessage("LanguageDistribution analyses failed!", 101);
            }

          case Failure(wrongPath) =>
            ErrorMessage("Parameter [" + wrongPath + "] i not a valid path!", 100)

        }

      case Failure(wrongDate) =>
        ErrorMessage("Parameter [" + wrongDate + "] is not a valid date!", 100)

    }

  }

}
