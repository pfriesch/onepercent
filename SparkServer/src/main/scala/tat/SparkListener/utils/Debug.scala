package tat.SparkListener.utils

/**
 * This object can be used to print out debug information.
 *
 * @author Florian Willich
 */
object Debug {

  /**
   * This method prints out a unified logging message.
   *
   * @param className       The name of the class you logging from.
   * @param functionName    The name of the method you logging from.
   * @param information     The information you want to log.
   *
   * @author Florian Willich
   */
  def log(className: String, functionName: String, information: String) = {
    println("### DEBUG ### class[" + className + "] fun(" + functionName + ") :: " + information)
  }

}
