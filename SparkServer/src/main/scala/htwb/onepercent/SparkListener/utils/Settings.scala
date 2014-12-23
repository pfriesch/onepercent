package htwb.onepercent.SparkListener.utils

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.{Failure, Success}


/**
 * Holding configuration for the App
 * @param hostname
 * @param port
 * @param JobsPackageString
 */
// !!!!!!!! If changed configVersion needs to be counted up !!!!!!!!!
case class Settings(configVersion: Int, hostname: String, port: Int, JobsPackageString: String, tweetsPrefixPath: String)


/**
 * Provides a Configuration Object based on a file. The files content is a UTF-8 Json String.
 *
 * @author pFriesch
 */
object Config {

  // !!!!!!!!!! Count up every time you change the Settings case class !!!!!!!!!
  val configVersion = 2
  val settingsFileName = "config.cfg"
  val defaultHostname = "hadoop03.f4.htw-berlin.de"
  val defaultPort = 5555
  val defaultJobsPackage = "htwb.onepercent.SparkListener.Jobs."
  val defaultTweetsPrefixPath = "hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/"

  var settings = Settings(configVersion, defaultHostname, defaultPort, defaultJobsPackage, defaultTweetsPrefixPath)

  // Constructor
  {
    val file = new File(settingsFileName)
    if (file.exists() && !file.isDirectory()) {
      JsonConverter.parseSettings(Source.fromFile(settingsFileName).mkString) match {
        case Success(settings) =>
          if (settings.configVersion != configVersion)
            setDefaultSettings
          else
            this.settings = settings
        case Failure(_) => setDefaultSettings
      }
    }
    else setDefaultSettings
  }

  private def setDefaultSettings = {
    val writer = new PrintWriter(new File(settingsFileName))
    writer.write(JsonConverter.toJsonString(Settings(configVersion, defaultHostname, defaultPort, defaultJobsPackage, defaultTweetsPrefixPath)))
    writer.close()
  }

  /**
   * Returns the current Settings
   * @return
   */
  def get: Settings = settings

  /**
   * Retruns the absolut Path where the Config file is expected/saved.
   * @return
   */
  def getAbsolutConfigFilePath: String = new File(settingsFileName).getAbsolutePath

}
