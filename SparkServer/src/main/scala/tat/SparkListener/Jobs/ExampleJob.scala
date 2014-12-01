package tat.SparkListener.Jobs

import tat.SparkListener.JobExecutor


// Ergebnis des Spark Jobs
case class ExampleResult(didItWork: String)


class ExampleJob extends JobExecutor{

  override def executeJob(params: Array[String]): AnyRef = {

    // Hier Spark Analyse Einfügen

    //Ergebnis zurück geben
    ExampleResult("Yes! Input: " + params(0))
  }
}
