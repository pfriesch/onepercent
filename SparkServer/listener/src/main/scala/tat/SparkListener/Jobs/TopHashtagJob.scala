package tat.SparkListener.Jobs

import tat.SparkListener.{JsonConverter, JobExecutor}
import tat.SparkListener.Jobs.Types.{T_HashtagFrequency, T_TopHashtags}


import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive._

class TopHashtagJob extends JobExecutor {

  override def executeJob(params: Array[String]): String= {
    //TODO: return real results

    import tat.SparkListener.Jobs.Types.T_Path

    val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
    val sc = new SparkContext(conf)
    val hc = new HiveContext(sc)
    val ta = new TweetAnalyser(sc, hc)

    JsonConverter.jobResultToJson(ta.topHashtagAnalyser(new T_Path(params(0)), params(1).toInt))
  }


}