package tat.SparkListener.utils

import java.text.SimpleDateFormat
import java.util.GregorianCalendar

/**
 * This trait can be used to log stuff to your console with println.
 *
 * @author Florian Willich
 */
trait Logging {

  val time: GregorianCalendar = new GregorianCalendar()
  val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  /**
   * This method prints out a unified logging message.
   *
   * @param functionName    The name of the method you logging from.
   * @param information     The information you want to log.
   *
   * @author Florian Willich
   */
  def log(functionName: String, information: String) = {
    println("### DEBUG ### [" + format.format(time.getTime()) + "] class[" + this.getClass.getName + "] function[" + functionName + "] => " + information)
  }

}
