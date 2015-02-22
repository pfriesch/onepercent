/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.Jobs

import java.text.SimpleDateFormat
import java.util.Calendar

import org.onepercent.utils.Types.TypeCreator
import org.onepercent.utils._
import org.onepercent.{Env, JobExecutor, JobResult}
import org.apache.spark.sql.hive.HiveContext

import scala.util.{Failure, Success, Try}

/**
 * Job to group tweets by there local time.
 * @author Patrick Mariot
 */
class TweetsAtDaytimeJob extends JobExecutor with Logging {

  /**
   * This Method analysis tweets to group them by there local time.
   *
   * @param params List element 0: Timestamp of the Day to calculate for.
   *
   * @return  The result of the analysis that looks like follow:
   *          TweetsAtDaytime @see { TweetAnalyser }
   *
   *          Or errors if there has been something going wrong:
   *
   *          If the timestamp in params(0) is not valid:
   *          ErrorMessage("Paramter [X] is not a valid date!", 100)
   *
   *          If the timestamp between start- and enddate is not valid:
   *          ErrorMessage("No Data available between X and Y", 100)
   *
   *          If start- or enddate doesn't match a valid date:
   *          ErrorMessage("Parameter X is not a valid path!", 100)
   *
   *          If there was something going wrong in the analysis:
   *          ErrorMessage("TweetsAtDaytime analyses failed!", 101)
   */
  override def executeJob(params: List[String]): JobResult = {

    val timeFormatter: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    Try(TypeCreator.gregorianCalendar(params(0), timeFormatter)) match {
      case Success(mainGregCalendar) =>
        val startTempCalender: Calendar = Calendar.getInstance()
        startTempCalender.setTime(mainGregCalendar.getTime)
        startTempCalender.set(Calendar.HOUR_OF_DAY, 0)
        startTempCalender.set(Calendar.HOUR_OF_DAY, startTempCalender.get(Calendar.HOUR_OF_DAY) - 13)
        val startTime: String = timeFormatter.format(startTempCalender.getTime)

        Try(TypeCreator.gregorianCalendar(startTime, timeFormatter)) match {
          case Success(startGregCalendar) =>
            val endTempCalender: Calendar = Calendar.getInstance()
            endTempCalender.setTime(mainGregCalendar.getTime)
            endTempCalender.set(Calendar.HOUR_OF_DAY, 23)
            endTempCalender.set(Calendar.HOUR_OF_DAY, endTempCalender.get(Calendar.HOUR_OF_DAY) + 12)
            val endTime: String = timeFormatter.format(endTempCalender.getTime)

            Try(TypeCreator.gregorianCalendar(endTime, timeFormatter)) match {
              case Success(endGregCalendar) =>

                Try(TypeCreator.multipleClusterPath(Config.get.tweetsPrefixPath, startGregCalendar, endGregCalendar, "*.data")) match {
                  case Success(path) =>
                    val hc = new HiveContext(Env.sc)
                    val ta = new TweetAnalyser(Env.sc, hc)
                    log("executeJob", "Starting Anaylsis for : " + params(0))
                    Try(ta.tweetsAtDaytime(new TweetJSONFileReader(Env.sc, hc).readFile(path), params(0))) match {
                      case Success(result) =>
                        //stop the spark context, otherwise its stuck in this context...
                        log("executeJob", "End Anaylsis for: " + params(0))
                        result
                      case Failure(_) =>
                        //stop the spark context, otherwise its stuck in this context...
                        log("executeJob", "TweetsAtDaytime analyses failed! timestamp[" + params(0) + "]")
                        ErrorMessage("TweetsAtDaytime analyses failed!", 101)
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
      case Failure(wrongDay) =>
        ErrorMessage("Paramter [" + wrongDay + "] is not a valid date!", 100)
    }
  }
}
