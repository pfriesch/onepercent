/*The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
For more detailed information, please read the licence.txt in the root directory.*/

package htwb.onepercent.SparkListener.utils.scoring

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

/**
 * Representation of trained Data
 * @param categoryProb the category probability, the previous probability of a word to be in a specific class
 * @param termProb the term probability, the probabilities of a term to be in categories
 * @param unknownWordProb the probability of a in the trained data unknown term to be in the categories
 */
case class TrainedData(categoryProb: Map[String, Double], termProb: Map[String, Map[String, Double]], unknownWordProb: Map[String, Double])

/**
 * Produces probabilities of terms being in categories based on training data.
 * The given SparkContext is used to compute these.
 * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
 * @param sc SparkContext to compute the probabilities.
 * @author pFriesch
 */
class TweetScoringLearner(sc: SparkContext) {

  type Category = String

  /**
   * Learns from training data to produce a pair of category probability and term probabilities of terms being in a given category.
   * @see http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
   * @param tweets the training tweets divided in categories.
   * @return a pair of category probability and term probabilities of terms being in a given category.
   */
  def learn(tweets: Map[Category, List[String]]): TrainedData = {

    val distTweets = sc.parallelize(tweets.toSeq)

    val categories = distTweets.map(T => T._1)

    val tokenizedTweets = distTweets.map(catTweets => catTweets._1 -> Tokenizer.tokenizeList(catTweets._2))

    val termCount = computeTermCount(tokenizedTweets, categories)

    val termCountPerClass = tokenizedTweets.map(catTokens => (catTokens._1, catTokens._2.length))

    val totalToken = termCountPerClass.map(X => X._2).reduce(_ + _)

    val categoryProb = termCountPerClass.map(CatCount => (CatCount._1, CatCount._2.toDouble / totalToken.toDouble + 1))

    val termProb = computeTermProb(termCount, categories.collect().toList)

    TrainedData(categoryProb.collect().toMap, termProb._1.map(X => (X._1, X._2.toMap)).collect().toMap, termProb._2)

  }

  private def computeTermProb(termCount: RDD[(String, Map[Category, Int])], categories: List[Category]): (RDD[(String, List[(Category, Double)])], Map[Category, Double]) = {
    val smoothing = 1
    //needs to be accessible on all workers, so no RDD
    val categoryTermCount = categories.map(C => (C, termCount.map(
      X => (X._1, X._2.getOrElse(C, 0) + smoothing)).map(X => X._2).reduce(_ + _))
    ).toMap
    // fills empty categories in the termcount with 0
    val filledTermCount = termCount.map(
      termWithCount => (termWithCount._1, categories.map(
        C => C -> termWithCount._2.getOrElse(C, 0))
        )
    )
    (filledTermCount.map(termsWithCount => (termsWithCount._1, termsWithCount._2.map {
      case (category: Category, count: Int) =>
        (category,
          // condProbFun
          (count + smoothing).toDouble / categoryTermCount(category).toDouble
          // \condProbFun
          )
      // probability for an unknown Word
    })), categoryTermCount.map(X => (X._1, 1.toDouble / X._2.toDouble)))
  }

  private def computeTermCount(tokenizedTweets: RDD[(Category, List[String])], categories: RDD[Category]): RDD[(String, Map[Category, Int])] = {
    val termCount = tokenizedTweets.map(X => X._1 -> X._2.groupBy(X => X).map(X => X._1 -> X._2.length))
    termCount.flatMap(X => X._2.map(Y => (Y._1, (X._1, Y._2)))).groupByKey().map(X => (X._1, X._2.toMap))
  }

}


