package htwb.onepercent.SparkListener.Jobs

import htwb.onepercent.SparkListener.{JobResult, JobExecutor}


case class CategoryCount(category: String, count: Int)
case class CategoryDistribution(distribution: List[CategoryCount], totalCount: Int) extends JobResult

class ClassifyJob extends JobExecutor {

  //TODO

  /*


    val testString = "Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt ut labore"
    val classifier = new TweetClassifier(learned._1, learned._2)
    val classified = classifier.classify(testString)
    println(classified)
    println(classified.maxBy(_._2))

*/

  override def executeJob(params: List[String]): JobResult = {
    CategoryDistribution(List(("others", 100)), 100)
  }
}
