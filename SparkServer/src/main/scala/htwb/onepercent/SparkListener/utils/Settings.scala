/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package htwb.onepercent.SparkListener.utils


import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
 * Holding configuration for the App
 * @param configVersion The iteration of this class
 * @param hostname the hostname of this app to bind a socket
 * @param port the port of this app to bind a socket
 * @param JobsPackageString absolute path to the Job classes
 * @param tweetsPrefixPath the absolute path to the tweets on the hdfs
 * @param scoring_TrainingDataPath the relative path where the scoring learner can find training files
 * @param scoring_TrainedDataPath the relative path where the scoring learner saves its trained data
 * @param scoring_OtherCategoryName the name of the category in scoring which is used when no category fits above the threshold
 * @param scoring_Threshold the threshold percentage where no category is fitted and the other catesgory is used
 */
// !!!!!!!! If signature is changed configVersion needs to be counted up !!!!!!!!!
//The reason is that the json string is not checked if it has the right structure while parsing
case class Settings(configVersion: Int,
                    hostname: String,
                    port: Int,
                    JobsPackageString: String,
                    tweetsPrefixPath: String,
                    scoring_TrainingDataPath: String,
                    scoring_TrainedDataPath: String,
                    scoring_OtherCategoryName: String,
                    scoring_Threshold: Double)


/**
 *
 * Provides a serializable configuration class.
 * Either creates a text file with a JSON string of default values as content or reads a config from the application
 * path and creats a Settings object with its content.
 *
 * @author pFriesch
 */
object Config extends Serializable {

  // !!!!!!!!!! Count up every time you change the signature Settings case class !!!!!!!!!
  val configVersion = 6
  val settingsFileName = "config.cfg"
  val defaultHostname = "hadoop03.f4.htw-berlin.de"
  val defaultPort = 5555
  val defaultJobsPackage = "htwb.onepercent.SparkListener.Jobs."
  val defaultTweetsPrefixPath = "hdfs://hadoop03.f4.htw-berlin.de:8020/studenten/s0540031/tweets/"
  val defaultScoringTrainingDataPath: String = "scoring/trainingData"
  val defaultScoringTrainedDataPath: String = "scoring/trainedData"
  val defaultClassificationOtherCategoryName: String = "other"
  val defaultClassificationThreshold: Double = 0.20


  var settings = Settings(configVersion,
    defaultHostname,
    defaultPort,
    defaultJobsPackage,
    defaultTweetsPrefixPath,
    defaultScoringTrainingDataPath,
    defaultScoringTrainedDataPath,
    defaultClassificationOtherCategoryName,
    defaultClassificationThreshold)

  {
    val file = new File(settingsFileName)
    if (file.exists() && !file.isDirectory()) {
      Try(JsonTools.parseClass[Settings](Source.fromFile(settingsFileName).mkString)) match {
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

  /**
   * Writes the default values to the file
   */
  private def setDefaultSettings = {
    val writer = new PrintWriter(new File(settingsFileName))
    writer.write(JsonTools.toJsonString(Settings(configVersion,
      defaultHostname,
      defaultPort,
      defaultJobsPackage,
      defaultTweetsPrefixPath,
      defaultScoringTrainingDataPath,
      defaultScoringTrainedDataPath,
      defaultClassificationOtherCategoryName,
      defaultClassificationThreshold
    )))
    writer.close()
  }

  /**
   * Returns the current Settings
   * @return
   */
  def get: Settings = settings

}
