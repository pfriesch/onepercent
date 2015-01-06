package scoring

/**
 * Created by plinux on 06/01/15.
 */
object Tokenizer {

    def tokenizeList(strings: List[String]): List[String] = {
      strings.flatMap(S => tokenize(S))
    }

  def tokenize(string:String): List[String] = {
    //TODO split and sanatize and delete stopwords
    string.split(" ").toList
  }
}
