package tat.SparkListener.Jobs


import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive._
import tat.SparkListener.JobExecutor
import tat.SparkListener.utils.{T_Path, JsonConverter, TweetAnalyser}

class TopOfThePops extends JobExecutor {

  override def executeJob(params: Array[String]): String= {
    //TODO: return real results

    val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
    val sc = new SparkContext(conf)
    val hc = new HiveContext(sc)

    val ta = new TweetAnalyser(sc, hc)

    //    val topHashtags: T_TopHashtags =

    JsonConverter.jobResultToJson(ta.hashtagsTopOfThePops(new T_Path(params(0)), params(1).toInt))


    // Result(compact(render(result)))

    //    val result: T_TopHashtag = hashtagsTopOfThePops(new T_Path(params(0)), params(1).toInt)
    //	    val topHashtags: String = compact(render(result))


  }


}