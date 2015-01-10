/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package htwb.onepercent.SparkListener.utils

import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, NoTypeHints, native}
import htwb.onepercent.SparkListener.JobSignature

import scala.reflect.ClassTag
import scala.util.Try

import java.io.{File, PrintWriter}

import scala.io.Source

/**
 * Provides methods to parse Objects out of Json Strings and to make Json Strings out of Objects
 * @author pFriesch
 */
object JsonTools {

  /**
   * Trys to parse a json String to a given object of type C
   * @param jsonString the string to be parsed.
   * @tparam C the class to be parsed.
   * @return the parsed object.
   */
  def parseClass[C: Manifest](jsonString: String): C = {
    import org.json4s.native.JsonMethods._
    implicit val formats = DefaultFormats
    org.json4s.native.JsonMethods.parse(jsonString).extract[C]
  }

  /**
   * Builds a json String of the given object.
   * @param obj
   * @return
   */
  def toJsonString(obj: AnyRef): String = {
    implicit val formats = native.Serialization.formats(NoTypeHints)
    write(obj)
  }

  /**
   * Writes the given obj to a file at the given path
   * @param obj the object to be written.
   * @param relativePath the path of the file to be written to.
   */
  def writeToFileAsJson(obj: AnyRef, relativePath: String) = {
    val file = new File(relativePath)
    file.getParentFile.mkdirs()
    file.createNewFile()
    file.setWritable(true)
    val writer = new PrintWriter(file)
    writer.write(JsonTools.toJsonString(obj))
    writer.close()
  }


}
