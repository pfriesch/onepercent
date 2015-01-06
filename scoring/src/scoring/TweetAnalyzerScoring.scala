package scoring

// http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html


class TweetScoringLearner {

  type Category = String


  def learn(tweets: Map[Category, List[String]]): (Map[Category, Double], Map[String, Map[Category, Double]]) = {

    val categories: List[Category] = tweets.map(T => T._1).toList

    val tokenizedTweets: Map[Category, List[String]] = tweets.map(catTweets => catTweets._1 -> Tokenizer.tokenizeList(catTweets._2))

    val termCount: Map[String, Map[Category, Int]] = computeTermCount(tokenizedTweets, categories)

    val termCountPerClass: Map[Category, Int] = tokenizedTweets.map(catTokens => (catTokens._1, catTokens._2.length))

    val totalToken: Int = termCountPerClass.values.reduce(_ + _)
    val categoryProb: Map[Category, Double] = termCountPerClass.map(CatCount => (CatCount._1, CatCount._2.toDouble / totalToken.toDouble + 1))

    val termProb: Map[String, Map[Category, Double]] = computeTermProb(termCount, categories)

    (categoryProb, termProb)

  }

  def computeTermProb(termCount: Map[String, Map[Category, Int]], categories: List[Category]): Map[String, Map[Category, Double]] = {
    val categoryTermCount: Map[Category, Int] =
      categories.map(C => (C, termCount.map(X => (X._1, X._2.getOrElse(C, 0) + 1)).values.reduce(_ + _))).toMap
    // füllt die termCount bei allen leeren Categorien mit null
    val filledTermCount = termCount.map(termWithCount => termWithCount._1 -> categories.map(C => C -> termWithCount._2.getOrElse(C, 0)))

    def condProbFun(termCount: Int, categoryTermCount: Int): Double = (termCount + 1).toDouble + 1 / (categoryTermCount + 1).toDouble

    filledTermCount.map(termsWithCount => (termsWithCount._1, termsWithCount._2.map(catWithCount =>
      catWithCount._1 -> condProbFun(catWithCount._2, categoryTermCount(catWithCount._1))).toMap))
  }

  def computeTermCount(tokenizedTweets: Map[Category, List[String]], categories: List[Category]): Map[String, Map[Category, Int]] = {

    //set der terme
    val termSet: Set[String] = tokenizedTweets.flatMap(X => X._2).toSet
    // anzahl der terme in den kategorien
    val termCount: Map[Category, Map[String, Int]] = tokenizedTweets.map(X => X._1 -> X._2.groupBy(X => X).map(X => (X._1 -> X._2.length)))
    val termBuf: collection.mutable.Map[String, collection.mutable.Map[Category, Int]] =
      collection.mutable.HashMap() ++ termSet.map(X => X -> (collection.mutable.HashMap() ++ categories.map(X => X -> 0).toMap)).toMap

    for (categ <- termCount; termFrequency <- categ._2) termBuf(termFrequency._1)(categ._1) = termFrequency._2
    collection.immutable.Map() ++ termBuf.map(X => X._1 -> (collection.immutable.Map() ++ X._2))
  }

}


