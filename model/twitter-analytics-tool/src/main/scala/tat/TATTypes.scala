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
class T_TopHashtag(private val totalTweetCount: Long, private val topHashtags: List[T_HashtagFrequency]) {

	/**
	* \return 	the total amount of Tweets.
	**/
	def getTalTweetCount() : Long = {
		return totalTweetCount
	} 

	/**
	* \return 	the top hashtags
	**/
	def getTopHashtags() : List[T_HashtagFrequency] = {
		return topHashtags
	}

}