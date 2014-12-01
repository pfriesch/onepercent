package tat.SparkListener.Jobs.Types

//Scala imports
import scala.util.Try;

//JAVA imports
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.io.File;



///**
// * related: http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
// */
//val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

/**
 * ==================================================================================
 * TYPES RELATED TO JOB RESULTS
 * ==================================================================================
 */

/**
 * Type representing one Twitter hashtag and its related counts.
 *
 * @param hashtag   The twitter hashtag.
 * @param count     The count of this twitter hashtag.
 */
case class T_HashtagFrequency(hashtag: String, count: Long)

/**
 * Type representing The Top twitter hashtags as an analysis result including
 * all hashtags and its frequency and the count of all hashtags counted whyle
 * analysing the twitter tweets.
 *
 * @param topHashtags       All hashtags and its related frequency.
 * @param countAllHashtags  The count of all hashtags counted whyle analysing the
 *                          twitter tweets.
 */
case class T_TopHashtags(topHashtags: Array[T_HashtagFrequency], countAllHashtags: Long)

//TODO add other JobResults

/**
 * ==================================================================================
 * TYPES RELATED TO JOB PARAMETERS
 * ==================================================================================
 */

object TypeCreator {

  def createClusterFile(prefixPath: String, time: GregorianCalendar, dataName: String): Try[File] = {
    Try(new File(prefixPath + time.get(Calendar.YEAR) + "/" + time.get(Calendar.MONTH) + "/" + time.get(Calendar.DAY_OF_MONTH) + "/" + time.get(Calendar.HOUR_OF_DAY) + "/" + dataName))
  }

}

object TypeValidator {

  def validateTime(time: String) : Try[GregorianCalendar] = {
    Try(new GregorianCalendar().setGregorianChange(DateFormat.parse(time)));
  }

}

/**
 * ==================================================================================
 * OTHER STUFF
 * ==================================================================================
 */

trait T_ValueDelimiter[T] {
  def getValue(): T
  def minValue(): T
  def maxValue(): T
  def name(): String
}

object TypeEvaluator {

  def evaluateDateMember(member: T_DateElement) {

    if (member.getValue() < member.minValue() || member.getValue() > member.maxValue()) {
      throw new IllegalArgumentException("ERROR: Parameter " + member.name() + " is less " + member.minValue() + " or bigger than " + member.maxValue())
    }

  }

}