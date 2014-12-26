package htwb.onepercent.SparkListener.Jobs

import java.text.SimpleDateFormat
import java.util.Calendar

import htwb.onepercent.SparkListener.utils.Types.TypeCreator
import htwb.onepercent.SparkListener.utils._
import htwb.onepercent.SparkListener.{JobExecutor, JobResult}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkContext, SparkConf}

import scala.util.{Failure, Success, Try}

/**
 * Job to search for a specific word in the Tweet texts.
 * @author Patrick Mariot
 */
class WordSearchJob extends JobExecutor with Logging {

  /**
   * This Method analysis the last 24 Hours to find a specific word in the Tweet texts.
   *
   * @param params List element 0: Word to look for
   *
   * @return  The result of the analysis that looks like follow:
   *          WordSearch @see { TweetAnalyser }
   *
   *          Or errors if there has been something going wrong:
   *
   *          If the timestamp between start- and enddate is not valid:
   *          ErrorMessage("No Data available between X and Y", 100)
   *
   *          If start- or enddate doesn't match a valid date:
   *          ErrorMessage("Parameter X is not a valid path!", 100)
   *
   *          If there was something going wrong in the analysis:
   *          ErrorMessage("WordSearch analyses failed!", 101)
   */
  override def executeJob(params: List[String]): JobResult = {

    val timeFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentCalendar: Calendar = Calendar.getInstance()
    val startCalendar: Calendar = currentCalendar
    startCalendar.set(Calendar.HOUR_OF_DAY, startCalendar.get(Calendar.HOUR_OF_DAY) - 24)
    val endTime: String = timeFormatter.format(currentCalendar.getTime())
    val startTime: String = timeFormatter.format(startCalendar.getTime())

    TypeCreator.createGregorianCalendar(startTime, timeFormatter) match {
      case Success(startGregCalendar) =>

        TypeCreator.createGregorianCalendar(endTime, timeFormatter) match {
          case Success(endGregCalendar) =>

            TypeCreator.createMultipleClusterPath(Config.get.tweetsPrefixPath, startGregCalendar, endGregCalendar, "*.data") match {
              case Success(path) =>

                val conf = new SparkConf().setAppName("Twitter WordSearch").set("spark.executor.memory", "8G").set("spark.cores.max", "24")
                val sc = new SparkContext(conf)
                val hc = new HiveContext(sc)
                val ta = new TweetAnalyser(sc, hc)

                log("executeJob", "Starting Anaylsis with keyword: " + params(0))

                Try(ta.wordSearchAnalyser(new TweetJSONFileReader(sc, hc).readFile(path), params(0))) match {
                  case Success(result) =>
                    //stop the spark context, otherwise its stuck in this context...
                    sc.stop()
                    log("executeJob", "End Anaylsis with word: " + params(0))
                    result
                  case Failure(_) =>
                    //stop the spark context, otherwise its stuck in this context...
                    sc.stop()
                    log("executeJob", "WordSearch analyses failed! word[" + params(0) + "]")
                    ErrorMessage("WordSearch analyses failed!", 101);
                }

              case Failure(wrongPath) =>
                ErrorMessage("No Data available between " + startTime + " and " + endTime, 100)
            }
          case Failure(wrongEndTime) =>
            ErrorMessage("Parameter [" + wrongEndTime + "] is not a valid path!", 100)
        }
      case Failure(wrongStartTime) =>
        ErrorMessage("Parameter [" + wrongStartTime + "] is not a valid path!", 100)

    }

  }

}
