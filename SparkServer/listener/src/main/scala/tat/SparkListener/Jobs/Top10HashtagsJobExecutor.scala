package tat.SparkListener.Jobs


/**
 * Created by plinux on 18/11/14.
 */
class Top10HashtagsJobExecutor extends JobExecutor {

  override def executeJob(params: Array[String]): Result = {
    //TODO: return real results
    import org.json4s._
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val topHashtags = TopHashtags(List(("MTVStars", 100), ("Lahm", 91), ("Stock Update", 88), ("Vorlesung", 54), (params(1), 2), (params(0), 1)))
    val json = topHashtags.hashtags
    Result(compact(render(json)))
  }

}

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.hive._
import org.apache.spark.rdd._
import tat.SparkListener.Jobs.Types.T_HashtagFrequency
import tat.SparkListener.Jobs.Types.T_Path
import tat.SparkListener.Jobs.Types.T_TopHashtag
import tat.SparkListener.Jobs.Types.T_TopHashtags
import tat.SparkListener.Jobs.Types.T_TopHashtagsResult

class RealTopOfThePops extends JobExecutor {

  override def executeJob(params: Array[String]): Result = {
    //TODO: return real results


    import Types.T_Path

    val conf = new SparkConf().setAppName("Twitter Hashtags Top 10")
    val sc = new SparkContext(conf)
    val hc = new HiveContext(sc)

    val ta = new TweetAnalyser(sc, hc)

    val topHashtags: T_TopHashtags = ta.hashtagsTopOfThePops(new T_Path(params(0)), params(1).toInt)

    val result: T_TopHashtagsResult = T_TopHashtagsResult("**jobidhash*", topHashtags)

   // Result(compact(render(result)))

    //    val result: T_TopHashtag = hashtagsTopOfThePops(new T_Path(params(0)), params(1).toInt)
    //	    val topHashtags: String = compact(render(result))

    Result(renderResult(result))
  }

  def renderResult(jobResult: T_TopHashtagsResult) : String = {
    import org.json4s._
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val json = (("jobID" -> jobResult.jobID) ~ ("topHashtags" -> (jobResult.jobResult.hashtags.)) ~ ("countAllHashtags" -> jobResult.jobResult.countAllHashtags))
    write(json)
  }

}