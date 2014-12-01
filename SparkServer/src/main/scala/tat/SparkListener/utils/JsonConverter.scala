package tat.SparkListener.utils

import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, NoTypeHints, native}
import tat.SparkListener.JobSignature

import scala.util.Try

/**
 * Created by plinux on 28/11/14.
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
   * Builds a json String of the given instance of a case Class
   * @param jobResult
   * @return
   */
  def jobResultToJson(jobResult: AnyRef) : String = {
    if (!isCaseClass(jobResult)) throw IllegalArgumentException
    implicit val formats = native.Serialization.formats(NoTypeHints)
    //TODO a "\n" is bad, alternative?
    write(jobResult)
  }


  def isCaseClass(o: AnyRef) = o.getClass.getInterfaces.find(_ == classOf[scala.Product]) != None

}
