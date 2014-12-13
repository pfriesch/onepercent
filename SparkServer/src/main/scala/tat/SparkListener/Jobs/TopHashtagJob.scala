package tat.SparkListener.Jobs

//Scala imports
import scala.util.{Failure, Success, Try}

//JAVA imports
import java.text.SimpleDateFormat

//Spark imports
import org.apache.spark.sql.hive._
import org.apache.spark.{SparkConf, SparkContext}

//Own imports
import tat.SparkListener.utils.Types.TypeCreator
import tat.SparkListener.utils.{ErrorMessage, Logging, TweetAnalyser}
import tat.SparkListener.JobExecutor

class TopHashtagJob extends JobExecutor with Logging {

  override def executeJob(params: List[String]): AnyRef = {

    TypeCreator.createGregorianCalendar(params(0), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")) match {
      case Success(gregCalendar) =>

        TypeCreator.createClusterPath(params(1), gregCalendar, "*.data") match {
          case Success(path) =>

            Try(params(2).toInt) match {
              case Success(topX) =>
                val conf = new SparkConf().setAppName("Twitter Hashtags Top 10").set("spark.executor.memory", "2G").set("spark.cores.max", "12")
                val sc = new SparkContext(conf)
                val hc = new HiveContext(sc)
                val ta = new TweetAnalyser(sc, hc)
                log("executeJob", "Starting Anaylsis with path: " + path.path + " and topX: " + topX)
                Try(ta.topHashtagAnalyser(path, topX)) match {
                  case Success(result) =>
                    //stop the spark context, otherwise its stuck in this context...
                    sc.stop()
                    log("executeJob", "End Anaylsis with path: " + path.path + " and topX: " + topX)
                    result;
                  case Failure(_) =>
                    log("executeJob", "TopHashtag analyses failed! path[" + path.path + "] topX[" + topX + "]")
                    ErrorMessage("TopHashtag analyses failed!", 101);
                }

              case Failure(_) =>
                ErrorMessage("Parameter [" + params(2) + "] is not an Integer!", 100)

            }

          case Failure(wrongPath) =>
            ErrorMessage("Parameter [" + wrongPath + "] i not a valid path!", 100)

        }

      case Failure(wrongDate) =>
        ErrorMessage("Parameter [" + wrongDate + "] is not a valid date!", 100)

    }

  }

}
