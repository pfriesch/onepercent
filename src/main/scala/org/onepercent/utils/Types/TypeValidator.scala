/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.utils.Types

import java.text.SimpleDateFormat
import java.util.GregorianCalendar

import scala.util.Try

/**
 * This object provides methods validating various Types.
 *
 * @author Florian Willich
 */
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
