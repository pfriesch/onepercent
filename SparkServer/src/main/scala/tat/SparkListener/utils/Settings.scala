package tat.SparkListener.utils

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.{Failure, Success}

/**
 * Created by plinux on 03/12/14.
 */


case class Settings(hostname: String, port: Int, JobsPackageString: String)

object Config {

  val settingsFileName = "config.cfg"
  val defaultHostname = "hadoop03.f4.htw-berlin.de"
  val defaultPort = 5555
  val defaultJobsPackage = "tat.SparkListener.Jobs."
  var settings = Settings(defaultHostname, defaultPort, defaultJobsPackage)

  // Constructor
  {
    val file = new File(settingsFileName)
    if (file.exists() && !file.isDirectory()) {
      JsonConverter.parseSettings(Source.fromFile(settingsFileName).mkString) match {
        case Success(settings) => this.settings = settings
        case Failure(_) =>
          setDefaultSettings
      }
    }
    else setDefaultSettings
  }

  private def setDefaultSettings = {
    val writer = new PrintWriter(new File(settingsFileName))
    writer.write(JsonConverter.caseClassToJson(Settings(defaultHostname, defaultPort, defaultJobsPackage)))
    writer.close()
  }

  def get: Settings = settings

  def getAbsolutConfigFilePath : String = new File(settingsFileName).getAbsolutePath

}
