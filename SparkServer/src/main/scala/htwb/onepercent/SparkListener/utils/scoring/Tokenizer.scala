/*The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
For more detailed information, please read the licence.txt in the root directory.*/

package htwb.onepercent.SparkListener.utils.scoring

/**
 * Provides tools to make tokens form full text.
 */
object Tokenizer {

  /* TODO
   * By doing so stopwords and special characters are cleared and the string is splied by whitespaces.
  */

  /**
   * Makes tokens from a list of full text strings.
   * @param strings the string list to be tokenized.
   * @return list of tokens.
   */
  def tokenizeList(strings: List[String]): List[String] = {
    strings.flatMap(S => tokenize(S))
  }

  /**
   * Makes tokens from a full text string.
   * @param string the string to be tokenized.
   * @return list of tokens.
   */
  def tokenize(string: String): List[String] = {
    //TODO split and sanatize and delete stopwords
    string.split(" ").toList
  }
}
