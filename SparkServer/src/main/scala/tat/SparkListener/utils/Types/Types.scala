package tat.SparkListener.utils


///**
// * related: http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
// */
//val dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

case class T_HashtagFrequency(hashtag: String, count: Long)

case class T_TopHashtags(topHashtags: Array[T_HashtagFrequency], countAllHashtags: Long)

//TODO add other JobResults

trait T_ValueDelimiter[T] {
  def getValue(): T

  def minValue(): T

  def maxValue(): T

  def name(): String
}

abstract class T_DateElement(val value: Int) extends T_ValueDelimiter[Int] {
  override def toString(): String = value.toString()

  def getValue() = value
}

object TypeEvaluator {

  def evaluateDateMember(member: T_DateElement) {

    if (member.getValue() < member.minValue() || member.getValue() > member.maxValue()) {
      throw new IllegalArgumentException("ERROR: Parameter " + member.name() + " is less " + member.minValue() + " or bigger than " + member.maxValue())
    }

  }

}

class T_DateElementMinute(value: Int) extends T_DateElement(value) {
  def minValue() = 0

  def maxValue() = 59

  def name() = "minute"

  TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementHour(value: Int) extends T_DateElement(value) {
  val minValue = 0
  val maxValue = 23

  def name() = "hour"

  TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementDay(value: Int) extends T_DateElement(value) {
  val minValue = 1
  val maxValue = 31

  def name() = "day"

  TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementMonth(value: Int) extends T_DateElement(value) {
  val minValue = 1
  val maxValue = 12

  def name() = "month"

  TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementYear(value: Int) extends T_DateElement(value) {
  val minValue = 1970
  val maxValue = 3000

  def name() = "year"

  TypeEvaluator.evaluateDateMember(this)
}

class T_Date(val year: T_DateElementYear, val month: T_DateElementMonth, val day: T_DateElementDay, val hour: T_DateElementHour, val minute: T_DateElementMinute) {
  override def toString(): String = new String(year.value + "-" + month.value + "-" + day.value + " " + hour.value + ":" + minute.value)
}


class T_Path(path: String) {
  override def toString(): String = path
}

object TypeCreator {

  def createPathToClusterData(prefixPath: String, timestamp: T_Date, dataName: String): T_Path = {
    return new T_Path(prefixPath + timestamp.year.toString() + "/" + timestamp.month.toString() + "/" + timestamp.day.toString() + "/" + timestamp.hour.toString() + "/" + dataName)
  }

  def timestampToDate(timestamp: String): T_Date = {

    //This was made with some RegEx: | is an or that means split with - or " " or :
    val timeSplitted: Array[String] = timestamp.split("-| |:")

    if (timeSplitted.length != 5) {
      throw new IllegalArgumentException("ERROR: The timestamp has not 5 date elements.")
    }

    val tmpYear = new T_DateElementYear(timeSplitted(0).toInt)
    val tmpMonth = new T_DateElementMonth(timeSplitted(1).toInt)
    val tmpDay = new T_DateElementDay(timeSplitted(2).toInt)
    val tmpHour = new T_DateElementHour(timeSplitted(3).toInt)
    val tmpMinute = new T_DateElementMinute(timeSplitted(4).toInt)

    return new T_Date(tmpYear, tmpMonth, tmpDay, tmpHour, tmpMinute)
  }

}