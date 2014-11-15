package tat

/**
* This object represents the Type of a Job you can see
* for example in the TweetAnalyser Class.
**/
object T_Job extends Enumeration {
    type T_Job = Value
    val hashtagsTopOfThePops, otherJob = Value
}

/**
* This class is a Type of one hashtag and its frequency being
* posted.
**/
case class T_HashtagFrequency(hashtag: String, count: Long);

/**
* This class is a Type for the Top hashtags.
*
* \param totalTweetCount 		Amount of all tweets. 
* 								(Or just all tweets with Hashtags?
*								opening Issue for that!)
* \param topHashtags 			The Top Hashtags!
**/
case class T_TopHashtag(totalTweetCount: Long, topHashtags: List[T_HashtagFrequency]);

abstract class T_DateElement(_value: Int) {
	val value: Int = _value
	override def toString() : String
	def minValue: Int
	def maxValue: Int
}

object TypeEvaluator {

	def evaluateDateMember(member: T_DateElement) {
		
		if (member.value < member.minValue || member.value > member.maxValue) {
			throw new IllegalArgumentException("ERROR: Parameter " + member.toString() + " is less " + member.minValue + " or bigger than " + member.maxValue)
		}
		
	}

}

class T_DateElementMinute(_value: Int) extends T_DateElement(_value) {
	override def toString() : String = "minute"
	val minValue = 0
	val maxValue = 59
	TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementHour(_value: Int) extends T_DateElement(_value) {
	override def toString() : String = "hour"
	val minValue = 0
	val maxValue = 23
	TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementDay(_value: Int) extends T_DateElement(_value) {
	override def toString() : String = "day"
	val minValue = 1
	val maxValue = 31
	TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementMonth(_value: Int) extends T_DateElement(_value) {
	override def toString() : String = "month"
	val minValue = 1
	val maxValue = 12
	TypeEvaluator.evaluateDateMember(this)
}

class T_DateElementYear(_value: Int) extends T_DateElement(_value) {
	override def toString() : String = "year"
	val minValue = 1970
	val maxValue = 3000
	TypeEvaluator.evaluateDateMember(this)
}

class T_Date(val year: T_DateElementYear, val month: T_DateElementMonth, val day: T_DateElementDay, val hour: T_DateElementHour, val minute: T_DateElementMinute) {
	override def toString() : String = new String(year.value + "-" + month.value + "-" + day.value + " " + hour.value + ":" + minute.value)
}

class T_Path(path: String) {
	override def toString() : String = path
}

object TypeCreator {

	def createPathToClusterData(prefixPath: String, timestamp: T_Date)  : T_Path = {
		return new T_Path(prefixPath + timestamp.year + "/" + timestamp.month + "/" + timestamp.day + "/" + timestamp.hour + "/*.data")
	}

	def timestampToDate(timestamp: String) : T_Date = {

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