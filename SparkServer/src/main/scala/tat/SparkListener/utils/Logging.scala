package tat.SparkListener.utils

/**
 * This trait can be used to log stuff to your console with println.
 *
 * @author Florian Willich
 */
trait Logging {

  /**
   * This method prints out a unified logging message.
   *
   * @param functionName    The name of the method you logging from.
   * @param information     The information you want to log.
   *
   * @author Florian Willich
   */
  def log(functionName: String, information: String) = {
    println("### DEBUG ### class[" + this.getClass.getName + "] fun(" + functionName + ") :: " + information)
  }

}
