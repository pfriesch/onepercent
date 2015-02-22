/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.utils


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
 * @param scoring_TrainedDataPath the relative path where the scoring learner saves its trained data
 * @param scoring_OtherCategoryName the name of the category in scoring which is used when no category fits above the threshold
 * @param scoring_Threshold the threshold percentage where no category is fitted and the other catesgory is used
 */
// !!!!!!!! If signature is changed configVersion needs to be counted up !!!!!!!!!
//The reason is that the json string is not checked if it has the right structure while parsing
case class Configuration(configVersion: Int,
                         hostname: String,
                         port: Int,
                         JobsPackageString: String,
                         tweetsPrefixPath: String,
                         scoring_TrainedDataPath: String,
                         scoring_OtherCategoryName: String,
                         scoring_Threshold: Double,
                         scoring_MinProbForResult: Double)


/**
 *
 * Provides a serializable configuration class.
 * Either creates a text file with a JSON string of default values as content or reads a config from the application
 * path and creats a Configuration object with its content.
 *
 * @author Pius Friesch, Florian Willich
 */
object Config extends Serializable {

  // !!!!!!!!!! Count up every time you change the signature Configuration case class !!!!!!!!!

  /**
   * Version number making sure your using the current configuration when parsing from JSON.
   * Default initialisation: 8
   */
  val configVersion = 8

  /**
   * Name of the config file.
   * Default: config.cfg
   */
  val configFileName = "config.cfg"

  /**
   * The hostname.
   * Default: hadoop03.f4.htw-berlin.de
   */
  val defaultHostname = "hadoop03.f4.htw-berlin.de"

  /**
   * The port.
   * Default: 5555
   */
  val defaultPort = 5555

  /**
   * The package where all the jobs are located in.
   * Default: org.onepercent.Jobs.
   */
  val defaultJobsPackage = "org.onepercent.Jobs."

  /**
   * The tweets prefix path.
   */
  val defaultTweetsPrefixPath = ""

  /**
   * Scoring trained data path:
   * Default: scoring/trainedData
   */
  val defaultScoringTrainedDataPath: String = "scoring/trainedData"

  /**
   * Name of the 'other' category for classification
   * Default: other
   */
  val defaultClassificationOtherCategoryName: String = "other"

  /**
   * The classification threshold which represents the minimum score, which is needed to get into a specific category.
   * Default: 0.16
   */
  val defaultClassificationThreshold: Double = 0.16

  /**
   * The minimum threshold needed for an example tweet.
   * Default: 0.55
   */
  val defaultScoringMinProbForSamples: Double = 0.55

  /**
   * This configuration object is equal to the JSON configuration object.
   */
  val defaultConfig = Configuration(
    configVersion,
    defaultHostname,
    defaultPort,
    defaultJobsPackage,
    defaultTweetsPrefixPath,
    defaultScoringTrainedDataPath,
    defaultClassificationOtherCategoryName,
    defaultClassificationThreshold,
    defaultScoringMinProbForSamples
  )

  /**
   * The default config.
   */
  var config: Configuration = defaultConfig

  {
    val file = new File(configFileName)
    if (file.exists() && !file.isDirectory()) {
      Try(JsonTools.parseClass[Configuration](Source.fromFile(configFileName).mkString)) match {
        case Success(newConfig) =>
          if (newConfig.configVersion != configVersion)
            setDefaultConfiguration
          else
            this.config = newConfig
        case Failure(_) => setDefaultConfiguration
      }
    }
    else setDefaultConfiguration
  }

  /**
   * Writes the default values to the file.
   */
  private def setDefaultConfiguration = {
    val writer = new PrintWriter(new File(configFileName))
    writer.write(JsonTools.toJsonString(defaultConfig))
    writer.close()
  }

  /**
   * Returns the current Configuration
   *
   * @return the current Configuration
   */
  def get: Configuration = config

}
