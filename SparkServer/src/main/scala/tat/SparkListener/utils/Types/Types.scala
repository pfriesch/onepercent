package tat.SparkListener.utils

//Scala imports
import scala.util.Try;

//JAVA imports
import java.io.File
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This Type represents a path to the filesystem which directory is validated.
 *
 * @param       path                        The path including the data ending.
 * @throws      IllegalArgumentException    If the directory is not valid on the filesystem.
 *
 * @author      Florian Willich
 */
case class T_Path(path: String) {

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
case class ErrorMessage(errorMessage: String, errorCode: Int)

//TODO add other JobResults

/**
 * ==================================================================================
 * TYPES RELATED TO JOB PARAMETERS
 * ==================================================================================
 */

object TypeCreator {

  /**
   * This method creates a path out of the given parameters as follows:
   * First the prefixpath within the last / concatenated with the given time
   * 2014-12-04 14:00:00 will result to <prefixPath>/2014/12/4/14/ this means the hour
   * is the last element in the path - finalized with the dataName ending.
   *
   * @param prefixPath      The path to put before the time.
   * @param time            The time with which the path will be build.
   * @param dataName        THe data/file.
   * @return                A Path if successful.
   *
   * @author                Florian Willich, Puis Friesch
   */
def createT_Path(prefixPath: String, time: GregorianCalendar, dataName: String): Try[T_Path] = {
    Try(T_Path(prefixPath + time.get(Calendar.YEAR) + "/" + String.format("%02d", time.get(Calendar.MONTH)+1: Integer) + "/" + String.format("%02d", time.get(Calendar.DAY_OF_MONTH): Integer) + "/" + String.format("%02d", time.get(Calendar.HOUR_OF_DAY): Integer) + "/" + dataName))
  }

  def createT_Path(prefixPath: String, timeBegin: GregorianCalendar, timeEnd: GregorianCalendar, dataName: String): Try[T_Path] = {
    //calculate hours between the dates
    val hours: Long = (timeEnd.getTimeInMillis - timeBegin.getTimeInMillis) / 3600000

    val pathList: List[T_Path] = List()

    for (i <- 0 to hours) {

      //TODO: Make path list out of timeBegin to timeEnd like
      //T_Path(prefixPath, TimeString, data
      var
    }


  }

  def createTimePath(time: GregorianCalendar) : String = {
    time.get(Calendar.YEAR) + "/" + String.format("%02d", time.get(Calendar.MONTH)+1: Integer) + "/" + String.format("%02d", time.get(Calendar.DAY_OF_MONTH): Integer) + "/" + String.format("%02d", time.get(Calendar.HOUR_OF_DAY): Integer)
  }

}

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