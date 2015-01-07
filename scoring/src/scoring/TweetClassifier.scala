package scoring

import scala.collection.{immutable, mutable}

/**
 * Created by plinux on 06/01/15.
 */
class TweetClassifier(categoryProb: Map[String, Double], termProb: Map[String, Map[String, Double]]) {
  type Category = String


  def classify(tweet: String): Map[Category, Double] = {

    val tokenizedTweet = Tokenizer.tokenize(tweet)
    val categories: List[Category] = categoryProb.map(X => X._1).toList
    val score: Map[Category, Double] = categories.map { C =>
      (C, Math.log10(categoryProb(C) + tokenizedTweet.map(S => termProb.getOrElse(S, Map()).getOrElse(C, 0.toDouble)).reduce(_ + _)))
    }.toMap
    //normalizes to 0..1
    def normalize(classifications: Map[Category, Double]): Map[Category, Double] = {
      val sum: (Category,Double) = classifications.reduce((X, Y) => (X._1, X._2 + Y._2))
      classifications.map(X => (X._1, X._2 / sum._2))
    }
    normalize(score)
  }

}
