/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package htwb.onepercent.SparkListener.utils

//Scala imports

import htwb.onepercent.SparkListener.JobResult

import scala.util.Try;


//JAVA imports
import java.text.SimpleDateFormat
import java.util.GregorianCalendar

/**
 * This Type represents a path to the filesystem which directory is validated.
 *
 * @param       path                        The path including the data ending.
 * @throws      IllegalArgumentException    If the directory is not valid on the filesystem.
 *
 * @author      Florian Willich
 */
case class Path(path: String) {

  /**
  if (!(new File(new File(path).getParent()).isDirectory())) {
    throw new IllegalArgumentException("This path does not exist!")
  }**/

}

/**
 * Error Type
 * @param     errorMessage    The message of this error.
 * @param     errorCode       The error Code.
 *
 * @author Florian Willich
 */
case class ErrorMessage(errorMessage: String, errorCode: Int) extends JobResult

//TODO add other JobResults



object TypeValidator {

  /**
   * This method returns a GregorianCalender set on the time and formatted with the
   * format if successful.
   *
   * @param time      The time on which the GregorianCalender will be set this has to
   *                  match the format.
   * @param format    The time format.
   *
   * @return          the GregorianCalender set on the given time if successful.
   *
   * @author          Florian Willich
   */
  def validateTime(time: String, format: SimpleDateFormat) : Try[GregorianCalendar] = {

    val calendar: GregorianCalendar = {
      new GregorianCalendar()
    }

    Try(calendar.setTime(format.parse(time))).map(c => calendar)
  }

}