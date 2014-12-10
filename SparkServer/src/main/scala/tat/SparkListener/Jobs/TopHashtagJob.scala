package tat.SparkListener.Jobs

import java.text.SimpleDateFormat

import org.apache.spark.sql.hive._
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.{Failure, Success, Try}

/**
 * Own imports
 */

import tat.SparkListener.utils._
import tat.SparkListener.JobExecutor

class TopHashtagJob extends JobExecutor {

  override def executeJob(params: List[String]): AnyRef = {

    TypeValidator.validateTime(params(0), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) match {
      case Success(gregCalendar) =>

        TypeCreator.createClusterPath(params(1), gregCalendar, "*.data") match {
          case Success(path) =>

            Try(params(2).toInt) match {
              case Success(topX) =>
                val conf = new SparkConf().setAppName("Twitter Hashtags Top 10").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
                val sc = new SparkContext(conf)
                val hc = new HiveContext(sc)
                val ta = new TweetAnalyser(sc, hc)
                Debug.log("TopHashtagJob", "executeJob", "Starting Anaylsis with path: " + path.path + " and topX: " + topX)
                ta.topHashtagAnalyser(path, topX) match {
                  case Success(result) =>
                    sc.stop()
                    Debug.log("TopHashtagJob", "executeJob", "End Anaylsis with path: " + path.path + " and topX: " + topX)
                    result;
                  case Failure(_) =>
                    ErrorMessage("TopHashtag analyses failed!", 101);
                }

              case Failure(wrongTopX) =>
                ErrorMessage("Parameter [" + wrongTopX + "] is not an Integer!", 100)

            }

          case Failure(wrongPath) =>
            ErrorMessage("Parameter [" + wrongPath + "] i not a valid path!", 100)

        }

      case Failure(wrongDate) =>
        ErrorMessage("Parameter [" + wrongDate + "] is not a valid date!", 100)

    }

  }

}
