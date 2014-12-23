package htwb.onepercent.SparkListener.utils.Types

//Java imports
import java.text.SimpleDateFormat
import java.util.{Calendar, GregorianCalendar}

//Scala imports
import scala.collection.mutable.ListBuffer
import scala.util.Try

//Own Imports
import htwb.onepercent.SparkListener.utils.T_Path


/**
 * With this object you can create several Types.
 *
 * @author Florian Willich
 */
object TypeCreator {

  /**
   * This method creates a path out of the given parameters as follows:
   * First the "/prefix/path/" concatenated with the given time e.g.
   * "2014-12-04 14:00:00 will" result to /prefix/path/2014/12/4/14/ this means the hour
   * is the last element in the path - finalized with the dataName ending.
   *
   * @param prefixPath      The path to put before the time.
   * @param time            The time with which the path will be build.
   * @param dataName        THe data/file e.g. "example.type" or "*.type"
   * @return                A Path if successful.
   *
   * @author                Florian Willich
   */
  def createClusterPath(prefixPath: String, time: GregorianCalendar, dataName: String): Try[T_Path] = {
    Try(T_Path(prefixPath + createTimePath(time) + dataName))
  }

  /**
   * This method creates multiple paths as same as the method createClusterPath.
   * @see { createClusterPath }
   *
   * For Testing:
   * val begin: GregorianCalendar = new GregorianCalendar()
   * begin.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-12-04 12:00:00"))
   * val end: GregorianCalendar = new GregorianCalendar()
   * end.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-12-04 23:00:00"))
   * val paths = createT_Path("/prefix/path/", begin, end, "*.data")
   *
   * @param     prefixPath                    The path to put before the time.
   * @param     timeBegin                     The time with which this method will begin to build the Paths.
   * @param     timeEnd                       The time with which this method will end to build Paths.
   * @param     dataName                      The data/file.
   * @throws    IllegalArgumentException      If the date does not differ with a minimum of one (1)
   *                                          hour.
   * @return                                  A list of paths.
   *
   * @author                Florian Willich
   */
  def createMultipleClusterPath(prefixPath: String, timeBegin: GregorianCalendar, timeEnd: GregorianCalendar, dataName: String): Try[List[T_Path]] = {
    //calculate hours between the dates
    //TODO: Maybe without any cast???
    val hours: Int = ((timeEnd.getTimeInMillis - timeBegin.getTimeInMillis) / 3600000).toInt

    hours match {

      case toLess if toLess < 1 =>
        throw new IllegalArgumentException("The difference of the dates has to be 1 hour or bigger. Your dates differ the follows: " + toLess)

      case x =>
        var pathList: ListBuffer[T_Path] = new ListBuffer[T_Path]()
        pathList += T_Path(prefixPath + createTimePath(timeBegin) + dataName)

        for (i <- 1 to x) {
          timeBegin.add(Calendar.HOUR, 1)
          pathList += T_Path(prefixPath + createTimePath(timeBegin) + dataName)
        }

        Try(pathList.toList)
    }

  }

  /**
   * This method creates a valid path out of the given time as follows:
   * "2014-12-04 14:00:00" => "/2014/12/04/14/"
   *
   * @param time    The time with which this method will build the path string.
   *
   * @return
   *
   * @author        Florian Willich, Pius Friesch
   */
  def createTimePath(time: GregorianCalendar) : String = {
    time.get(Calendar.YEAR) + "/" + String.format("%02d", time.get(Calendar.MONTH)+1: Integer) + "/" + String.format("%02d", time.get(Calendar.DAY_OF_MONTH): Integer) + "/" + String.format("%02d", time.get(Calendar.HOUR_OF_DAY): Integer) + "/"
  }

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
  def createGregorianCalendar(time: String, format: SimpleDateFormat) : Try[GregorianCalendar] = {

    val calendar: GregorianCalendar = {
      new GregorianCalendar()
    }

    Try(calendar.setTime(format.parse(time))).map(c => calendar)
  }

}
