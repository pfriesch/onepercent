package htwb.onepercent.SparkListener.Jobs

import java.text.SimpleDateFormat
import java.util.Calendar

import htwb.onepercent.SparkListener.utils.Types.TypeCreator
import htwb.onepercent.SparkListener.utils.{ErrorMessage, TweetJSONFileReader, Logging, TweetAnalyser}
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
   * @return  to be discussed
   */
  override def executeJob(params: List[String]): JobResult = {

    val timeFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentTime: Calendar = Calendar.getInstance()
    val endTime: String = timeFormatter.format(currentTime.getTime())
    val startTime: String = timeFormatter.format(currentTime.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY) - 24))

    TypeCreator.createGregorianCalendar(startTime, timeFormatter) match {
      case Success(startGregCalendar) =>

        TypeCreator.createGregorianCalendar(endTime, timeFormatter) match {
          case Success(endGregCalendar) =>

            TypeCreator.createMultipleClusterPath("hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/", startGregCalendar, endGregCalendar, "*.data") match {
              case Success(path) =>

                val conf = new SparkConf().setAppName("Twitter WordSearch").set("spark.executor.memory", "6G").set("spark.cores.max", "24")
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
                ErrorMessage("No Data avaible between " + startTime + " and " + endTime, 100)
            }

          case Failure(wrongEndTime) =>
            ErrorMessage("Parameter [" + wrongEndTime + "] i not a valid path!", 100)

        }

      case Failure(wrongStartTime) =>
        ErrorMessage("Parameter [" + wrongStartTime + "] i not a valid path!", 100)

    }

  }
  
}
