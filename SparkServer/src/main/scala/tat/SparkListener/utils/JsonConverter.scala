package tat.SparkListener.utils

import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, NoTypeHints, native}
import tat.SparkListener.JobSignature

import scala.util.Try

/**
 * Provides methods to parse Objects out of Json Strings and to make Json Strings out of Objects
 * @author pFriesch
 */
object JsonConverter {

  /**
   * Trys to parse a json String to a JobSignature
   * @param jsonString
   * @return
   */
  def parseJobJson(jsonString: String): Try[JobSignature] = {
    import org.json4s.native.JsonMethods._
    implicit val formats = DefaultFormats
    Try(org.json4s.native.JsonMethods.parse(jsonString).extract[JobSignature])
  }

  /**
   * Trys to parse a json String to a Settings object
   * @param jsonString
   * @return
   */
  def parseSettings(jsonString: String) : Try[Settings] = {
    import org.json4s.native.JsonMethods._
    implicit val formats = DefaultFormats
    Try(org.json4s.native.JsonMethods.parse(jsonString).extract[Settings])
  }

  /**
   * Builds a json String of the given instance of a case Class
   * @param caseClass
   * @return
   */
  def toJsonString(caseClass: AnyRef) : String = {
    implicit val formats = native.Serialization.formats(NoTypeHints)
    write(caseClass)
  }



}
