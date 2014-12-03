package tat.SparkListener.utils

//Scala imports
import scala.util.Try;

//JAVA imports
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

/**
  * We need to validate this path (if its a valid path on the running file system).
  * Class File from Java is probably no solution because it cannot resolve the *.data ...
 */
//TODO: validate directory (/to/dir/*.data) validate -> /to/dir/
case class T_Path(path: String)

/**
 * Error Type
 */
case class ErrorMessage(errorMessage: String, errorCode: Int)

//TODO add other JobResults

/**
 * ==================================================================================
 * TYPES RELATED TO JOB PARAMETERS
 * ==================================================================================
 */

object TypeCreator {

  def createClusterPath(prefixPath: String, time: GregorianCalendar, dataName: String): Try[T_Path] = {
    Try(T_Path(prefixPath + time.get(Calendar.YEAR) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.DAY_OF_MONTH) + "/" + time.get(Calendar.HOUR_OF_DAY) + "/" + dataName))
  }

}

//OP because there is a naming conflict just with TypeValidator
object TypeValidator {

  def validateTime(time: String, format: SimpleDateFormat) : Try[Try[GregorianCalendar]] = {

    val calendar: GregorianCalendar = {
      new GregorianCalendar()
    }

    format.setLenient(false)

    Try(calendar.setTime(format.parse(time))).map(c => Try(calendar))
  }

}