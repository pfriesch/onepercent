/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.utils

//Scala imports

import org.onepercent.JobResult

import scala.util.Try;


//JAVA imports
import java.text.SimpleDateFormat
import java.util.GregorianCalendar

/**
 * This Type represents a path to the filesystem which directory is validated.
 *
 * @param       path                        The path including the data ending.
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
 * This Type represents a Error Message, including the message itself and a error code.
 *
 * @param     errorMessage    The message of this error.
 * @param     errorCode       The error Code.
 *
 * @author Florian Willich
 */
case class ErrorMessage(errorMessage: String, errorCode: Int) extends JobResult