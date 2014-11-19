package tat.SparkListener.Jobs



case class TopHashtags(hashtags: List[(String,Int)])


/**
 * Created by plinux on 18/11/14.
 */
class Top10HashtagsJobExecutor extends JobExecutor{

  override def executeJob(params: List[String]): Result = {
    //TODO: return real results
    import org.json4s._
    import org.json4s.JsonDSL._
    import org.json4s.native.JsonMethods._

    val topHashtags = TopHashtags(List(("MTVStars", 100 ), ("Lahm", 91), ("Stock Update",88), ("Vorlesung",54),(params(1),2),(params(0),1)))
    val json = topHashtags.hashtags
    Result(compact(render(json)))
  }
}
