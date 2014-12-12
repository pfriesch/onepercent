package tat.SparkListener.Jobs

import tat.SparkListener.{JobResult, JobExecutor}


// Ergebnis des Spark Jobs
case class ExampleResult(didItWork: String) extends JobResult


class ExampleJob extends JobExecutor{

  override def executeJob(params: List[String]): JobResult = {

    // Hier Spark Analyse Einfügen

    //Ergebnis zurück geben
    ExampleResult("Yes! Input: " + params(0))
  }
}
