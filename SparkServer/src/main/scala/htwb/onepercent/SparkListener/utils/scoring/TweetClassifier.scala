/*The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
For more detailed information, please read the licence.txt in the root directory.*/

package htwb.onepercent.SparkListener.utils.scoring

import htwb.onepercent.SparkListener.utils.Config

/**
 * Can classify a Tweet based on the given probabilities of terms being in categories.
 * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
 * @param trainedData the base probabilities of each category and the term probabilities of each term in each category.
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
        (C, Math.log10(trainedData.categoryProb(C) * tokenizedTweet.map(S => trainedData.termProb.getOrElse(S, trainedData.unknownWordProb)(C)).reduce(_*_)))
      }.toMap
      //normalizes to 0..1
      def normalize(classifications: Map[Category, Double]): Map[Category, Double] = {
        val sum: (Category, Double) = classifications.reduce((X, Y) => (X._1, X._2 + Y._2))
        classifications.map(X => (X._1, X._2 / sum._2))
      }
      normalize(score).maxBy(_._2)
    } else {
      println("------> Empty Tweet")
      (Config.get.scoring_OtherCategoryName, 1)
    }
  }

}
