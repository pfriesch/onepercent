package tat.SparkListener.Jobs

import java.text.SimpleDateFormat;

import tat.SparkListener.Jobs.Types.{TypeCreator, TypeValidatorOP, T_Path, T_Error}
import tat.SparkListener.{JsonConverter, JobExecutor}

import scala.util.{Try, Failure, Success}

import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, NoTypeHints, native}

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive._

class TopHashtagJob extends JobExecutor {

  //TODO: Returns should all be an JSON object right?
  override def executeJob(params: Array[String]): Unit = {

    TypeValidatorOP.validateTime(params(0), new SimpleDateFormat("yyyy-mm-dd hh")) match {
      case Success(Success(gregCalendar)) =>

        TypeCreator.createClusterFile(params(1), gregCalendar, "*.data") match {
          case Success(path) =>

            Try[Int] = Try(params(2).toInt) match {
              case Success(topX) =>
                val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
                val sc = new SparkContext(conf)
                val hc = new HiveContext(sc)
                val ta = new TweetAnalyser(sc, hc)
                JsonConverter.jobResultToJson(ta.topHashtagAnalyser(path, topX))

              case Failure(wrongTopX) =>
                //thats just an example how it could work with error codes!
                return T_Error("Parameter [" + wrongTopX + "] is not an Integer!", 11)

            }

          case Failure(wrongPath) =>
            //Not the right way i suppose
            return "ERROR: Parameter [" + wrongPath + "] is not a valid Path!"

        }

      case Failure(wrongParam) =>
        return "ERROR: Parameter [" + wrongParam + "] is not a valid date!"

    }

  }

}