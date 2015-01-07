package htwb.onepercent.SparkListener.utils

import scala.util.{Success, Failure, Try}

/**
 * This object solves language codes of the ISO 639-1 standard.
 *
 * @author Florian Willich
 */
object LanguageCodeSolver {

  val languageMap: Map[String, String] = createLanguageMap()

  def solve(code: String) : String = {

    Try(languageMap(code)) match {
      case Success(name) =>
        name
      case Failure(_) =>
        "Language not supported"
    }

  }

  /**
   * This Language Map is build with the standard of  ISO 639-1.
   *
   * @return
   */
  private def createLanguageMap() : Map[String, String] = {
    var languageMap: Map[String, String] = Map("aa" -> "Afar")

    languageMap += ("ab" -> "Abkhazian")
    languageMap += ("af" -> "Afrikaans")
    languageMap += ("am" -> "Amharic")
    languageMap += ("ar" -> "Arabic")
    languageMap += ("as" -> "Assamese")
    languageMap += ("ay" -> "Aymara")
    languageMap += ("az" -> "Azerbaijani")
    languageMap += ("ba" -> "Bashkir")
    languageMap += ("be" -> "Byelorussian")
    languageMap += ("bg" -> "Bulgarian")
    languageMap += ("bh" -> "Bihari")
    languageMap += ("bi" -> "Bislama")
    languageMap += ("bn" -> "Bengali")
    languageMap += ("bo" -> "Tibetan")
    languageMap += ("br" -> "Breton")
    languageMap += ("ca" -> "Catalan")
    languageMap += ("co" -> "Corsican")
    languageMap += ("cs" -> "Czech")
    languageMap += ("cy" -> "Welch")
    languageMap += ("da" -> "Danish")
    languageMap += ("de" -> "German")
    languageMap += ("dz" -> "Bhutani")
    languageMap += ("el" -> "Greek")
    languageMap += ("en" -> "English")
    languageMap += ("eo" -> "Esperanto")
    languageMap += ("es" -> "Spanish")
    languageMap += ("et" -> "Estonian")
    languageMap += ("eu" -> "Basque")
    languageMap += ("fa" -> "Persian")
    languageMap += ("fi" -> "Finnish")
    languageMap += ("fj" -> "Fiji")
    languageMap += ("fo" -> "Faeroese")
    languageMap += ("fr" -> "French")
    languageMap += ("fy" -> "Frisian")
    languageMap += ("ga" -> "Irish")
    languageMap += ("gd" -> "Scots Gaelic")
    languageMap += ("gl" -> "Galician")
    languageMap += ("gn" -> "Guarani")
    languageMap += ("gu" -> "Gujarati")
    languageMap += ("ha" -> "Hausa")
    languageMap += ("hi" -> "Hindi")
    languageMap += ("he" -> "Hebrew")
    languageMap += ("hr" -> "Croatian")
    languageMap += ("hu" -> "Hungarian")
    languageMap += ("hy" -> "Armenian")
    languageMap += ("ia" -> "Interlingua")
    languageMap += ("id" -> "Indonesian")
    languageMap += ("ie" -> "Interlingue")
    languageMap += ("ik" -> "Inupiak")
    languageMap += ("in" -> "former Indonesian")
    languageMap += ("is" -> "Icelandic")
    languageMap += ("it" -> "Italian")
    languageMap += ("iu" -> "Inuktitut (Eskimo)")
    languageMap += ("iw" -> "former Hebrew")
    languageMap += ("ja" -> "Japanese")
    languageMap += ("ji" -> "former Yiddish")
    languageMap += ("jw" -> "Javanese")
    languageMap += ("ka" -> "Georgian")
    languageMap += ("kk" -> "Kazakh")
    languageMap += ("kl" -> "Greenlandic")
    languageMap += ("km" -> "Cambodian")
    languageMap += ("kn" -> "Kannada")
    languageMap += ("ko" -> "Korean")
    languageMap += ("ks" -> "Kashmiri")
    languageMap += ("ku" -> "Kurdish")
    languageMap += ("ky" -> "Kirghiz")
    languageMap += ("la" -> "Latin")
    languageMap += ("ln" -> "Lingala")
    languageMap += ("lo" -> "Laothian")
    languageMap += ("lt" -> "Lithuanian")
    languageMap += ("lv" -> "Latvian, Lettish")
    languageMap += ("mg" -> "Malagasy")
    languageMap += ("mi" -> "Maori")
    languageMap += ("mk" -> "Macedonian")
    languageMap += ("ml" -> "Malayalam")
    languageMap += ("mn" -> "Mongolian")
    languageMap += ("mo" -> "Moldavian")
    languageMap += ("mr" -> "Marathi")
    languageMap += ("ms" -> "Malay")
    languageMap += ("mt" -> "Maltese")
    languageMap += ("my" -> "Burmese")
    languageMap += ("na" -> "Nauru")
    languageMap += ("ne" -> "Nepali")
    languageMap += ("nl" -> "Dutch")
    languageMap += ("no" -> "Norwegian")
    languageMap += ("oc" -> "Occitan")
    languageMap += ("om" -> "(Afan) Oromo")
    languageMap += ("or" -> "Oriya")
    languageMap += ("pa" -> "Punjabi")
    languageMap += ("pl" -> "Polish")
    languageMap += ("ps" -> "Pashto, Pushto")
    languageMap += ("pt" -> "Portuguese")
    languageMap += ("qu" -> "Quechua")
    languageMap += ("rm" -> "Rhaeto-Romance")
    languageMap += ("rn" -> "Kirundi")
    languageMap += ("ro" -> "Romanian")
    languageMap += ("ru" -> "Russian")
    languageMap += ("rw" -> "Kinyarwanda")
    languageMap += ("sa" -> "Sanskrit")
    languageMap += ("sd" -> "Sindhi")
    languageMap += ("sg" -> "Sangro")
    languageMap += ("sh" -> "Serbo-Croatian")
    languageMap += ("si" -> "Singhalese")
    languageMap += ("sk" -> "Slovak")
    languageMap += ("sl" -> "Slovenian")
    languageMap += ("sm" -> "Samoan")
    languageMap += ("sn" -> "Shona")
    languageMap += ("so" -> "Somali")
    languageMap += ("sq" -> "Albanian")
    languageMap += ("sr" -> "Serbian")
    languageMap += ("ss" -> "Siswati")
    languageMap += ("st" -> "Sesotho")
    languageMap += ("su" -> "Sudanese")
    languageMap += ("sv" -> "Swedish")
    languageMap += ("sw" -> "Swahili")
    languageMap += ("ta" -> "Tamil")
    languageMap += ("te" -> "Tegulu")
    languageMap += ("tg" -> "Tajik")
    languageMap += ("th" -> "Thai")
    languageMap += ("ti" -> "Tigrinya")
    languageMap += ("tk" -> "Turkmen")
    languageMap += ("tl" -> "Tagalog")
    languageMap += ("tn" -> "Setswana")
    languageMap += ("to" -> "Tonga")
    languageMap += ("tr" -> "Turkish")
    languageMap += ("ts" -> "Tsonga")
    languageMap += ("tt" -> "Tatar")
    languageMap += ("tw" -> "Twi")
    languageMap += ("ug" -> "Uigur")
    languageMap += ("uk" -> "Ukrainian")
    languageMap += ("ur" -> "Urdu")
    languageMap += ("uz" -> "Uzbek")
    languageMap += ("vi" -> "Vietnamese")
    languageMap += ("vo" -> "Volapuk")
    languageMap += ("wo" -> "Wolof")
    languageMap += ("xh" -> "Xhosa")
    languageMap += ("yi" -> "Yiddish")
    languageMap += ("yo" -> "Yoruba")
    languageMap += ("za" -> "Zhuang")
    languageMap += ("zh" -> "Chinese")
    languageMap += ("zu" -> "Zulu")

    languageMap
  }

}
