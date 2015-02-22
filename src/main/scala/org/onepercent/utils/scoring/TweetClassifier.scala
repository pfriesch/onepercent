/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.utils.scoring

import org.onepercent.utils.Config

/**
 * Can classify a Tweet based on the given probabilities of terms being in categories.
 * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
 * @param trainedData the base probabilities of each category and the term probabilities of each term in each category.
 * @author pFriesch
 */
//needs to be serializable to be distributed
class TweetClassifier(trainedData: TrainedData) extends Serializable {
  type Category = String

  /**
   * Classifies the given tweet into categories based on the probabilities of each term in the tweet.
   * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
   * @param tweet the tweet to be classified.
   * @return the probabilities of the tweet to be in each category.
   */
  def classify(tweet: String): (Category, Double) = {
    val tokenizedTweet = Tokenizer.tokenize(tweet)
    if (tokenizedTweet.length > 0) {
      val categories: List[Category] = trainedData.categoryProb.map(X => X._1).toList
      val score: Map[Category, Double] = categories.map { C =>
        (C, Math.log(trainedData.categoryProb(C)) +
          tokenizedTweet.map(S => trainedData.termProb.getOrElse(S, trainedData.unknownWordProb)(C)).map(Math.log).reduce(_ + _))
      }.toMap
      normalize(score).maxBy(_._2)
    } else {
      (Config.get.scoring_OtherCategoryName, 1)
    }
  }

  def classifyVerbose(tweet: String): (String, Map[Category, Double]) = {
    val tokenizedTweet = Tokenizer.tokenize(tweet)
    if (tokenizedTweet.length > 0) {
      val categories: List[Category] = trainedData.categoryProb.map(X => X._1).toList
      val score: Map[Category, Double] = categories.map { C =>
        (C, Math.log(trainedData.categoryProb(C)) +
          tokenizedTweet.map(S => trainedData.termProb.getOrElse(S, trainedData.unknownWordProb)(C)).map(Math.log).reduce(_ + _))
      }.toMap
      (tweet, normalize(score))
    } else {
      (tweet, Map(Config.get.scoring_OtherCategoryName -> 1))
    }
  }

  //normalizes to 0..1
  private def normalize(classifications: Map[Category, Double]): Map[Category, Double] = {
    // due to the use of the logarithm all results are negative, but still the greates value is the highest probability
    //so to make the highest probability the greatest value the function the exponential function is applied
    val scoresInverted = classifications.map(X => (X._1, Math.exp(X._2)))
    val sum: Double = scoresInverted.reduce((X, Y) => (X._1, X._2 + Y._2))._2
    //if its more likely to be in the category the value is lower, so it needs to be inverted
    scoresInverted.map(X => (X._1, X._2 / sum))
  }

}
