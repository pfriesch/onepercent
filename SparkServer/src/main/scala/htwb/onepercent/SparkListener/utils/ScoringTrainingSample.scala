package htwb.onepercent.SparkListener.utils

import scala.collection.mutable.MutableList
import scala.collection.immutable.List

/**
 * Created by flo on 10.01.15.
 */
object ScoringTrainingSample {

  def trainingSampleSports() : List[String] = {
    var sportsSample: scala.collection.mutable.MutableList[String] = scala.collection.mutable.MutableList()

    //Yahoo Sports Tweets (https://twitter.com/yahoosports)
    sportsSample += "Utah's Trevor Booker hits the shot of the season in 0.2 seconds --> http://yhoo.it/1x8kmE7"
    sportsSample += "Sunderland 0-1 Liverpool: Markovic’s strike enough for resurgent Reds --> http://yhoo.it/1x8k1RU"
    sportsSample += "NFL Playoff Preview: Dallas Cowboys at Green Bay Packers (@Eric_Edholm) --> http://yhoo.it/1wCLoEt"
    sportsSample += "Nevada commission: Jon Jones tests results show no signs of doping (via @kevini) --> http://yhoo.it/1IwUYhX"
    sportsSample += "RT @DanWetzel USOC will submit Boston as possible host city of 2024 Summer Olympics."
    sportsSample += "Jameis Winston's dad reportedly says quarterback is turning pro --> http://yhoo.it/1Bz1IeQ"
    sportsSample += "From letters to bosses, to Tinder, Hawks CEO Steve Koonin finds wacky ways to fill the arena for his 1st-place team: http://yhoo.it/1DvKZZk" //https://twitter.com/YahooSports/status/552663411759525888
    sportsSample += "RT @WojYahooNBA Dion Waiters will be traded to Oklahoma City in the three-way deal, league sources tell Yahoo Sports."
    sportsSample += "Kareem Hunt runs for five TDs as Toledo wins GoDaddy Bowl vs. Arkansas State: http://yhoo.it/1KdB4vX"
    sportsSample += "Athletes and sports personalities react to ESPN's Stuart Scott's passing at the age of 49: http://yhoo.it/1F51OP8"

    //ESPN Tweets (https://twitter.com/espn)
    sportsSample += "ICYMI: Trevor Booker made one of the most unbelievable shots ever for the @utahjazz last night: http://es.pn/1tTZD6U"
    sportsSample += "This was perhaps the poster dunk of the college hoops season ... until it was taken away by a charge call: http://es.pn/1s7epvj"
    sportsSample += "Raptors coach Dwane Casey will \"get in a physical fight\" with coaches if they leave Kyle Lowry off the All-Star team: http://es.pn/1DidNqP"
    sportsSample += "A college hoops team pulled off this amazing alley-oop tip-in to force OT after trailing by 2 with 0.6 seconds left: http://es.pn/1Kd7iHK"
    sportsSample += "The @CFBPlayoff Championship Game is just 8 days away. Get ready by reliving the best plays of bowl season so far » http://es.pn/13ResA5"
    sportsSample += "Cam Newton warms up, seeking his first postseason win as the NFL Playoffs are about to kick off on ESPN."
    sportsSample += "Ohio State head coach Urban Meyer had a great reaction to Oregon's blowout win over Florida State in the #RoseBowl http://es.pn/13N2CHa"
    sportsSample += "The @CFBPlayoff National Championship Game is set: Oregon vs. Ohio State, Monday, Jan. 12, 8:30 p.m. ET on ESPN."
    sportsSample += "Cam Newton warms up, seeking his first postseason win as the NFL Playoffs are about to kick off on ESPN."
    sportsSample += "The throw by Ohio State WR Evan Spencer was great. The catch by Michael Thomas was even better on this MUST-SEE TD: http://es.pn/1zXyhnm"

    //Sports Illustrated Tweets (https://twitter.com/sinow)
    sportsSample += "Watch List: Juan Martin del Potro returns, final Aussie Open tune-ups http://on.si.com/1BSSvMn"
    sportsSample += "Expect UVA to fall from the ranks of unbeaten teams in this week's college basketball picks: http://on.si.com/1Ax9K6m"
    sportsSample += "Last night, Trevor Booker beat the clock with a CRAZY circus shot http://on.si.com/1xNJwNl"
    sportsSample += "Did you see @NerlensNoel3's game-winning dunk that lifted the Sixers over the Nets? Watch it: http://on.si.com/1BXPIls"
    sportsSample += "Trent Richardson is now practicing with @Colts' special teams http://on.si.com/1AyehWb"
    sportsSample += "J.R. Smith's motto: 'When in doubt, shoot the ball' http://on.si.com/1HPF7fU"
    sportsSample += "RB C.J. Anderson is changing the @Broncos' offensive dynamic"
    sportsSample += "#MLS 2015 schedule is out. Circle these dates for the league’s 20th season (via @liviubird): http://www.si.com/planet-futbol/2015/01/07/mls-2015-schedule-top-games"
    sportsSample += "Chicago Bulls point guard Derrick Rose says his 'slump' is 'just part of basketball’ http://on.si.com/1tP73xZ"
    sportsSample += "49ers QB Colin Kaepernick will train with Kurt Warner over the offseason http://on.si.com/1BzDuBh"

    //BBC Sports Tweets (https://twitter.com/bbcsport)
    sportsSample += "Bad defending cost #whufc three points at #swans according to Neil McDonald. Watch here http://bbc.in/1xPnklN"
    sportsSample += "Sam Burgess scored his first try for Bath as they closed the gap on the Premiership leaders http://bbc.in/14zDdBi"
    sportsSample += "It is all defence and no attack for Crystal Palace at the moment vs #Spurs. Live text: http://bbc.in/1DIYXHn"
    sportsSample += "Aberdeen re-establish 4-point lead at the top of the Premiership with victory over St Mirren: http://bbc.in/1wHbPJl"
    sportsSample += "Tony Pulis wins his first Premier League match as West Brom manager. Match report: http://bbc.in/1AB5B41  #wba #hcafc"
    sportsSample += "Swansea fight back to earn a point against West Ham. Match report: http://bbc.in/14zVIWg  #whufc #swans"
    sportsSample += "Man City held to a draw at Everton as champions fall two points behind leaders Chelsea. http://bbc.in/1DsPZk8  #efc"

    sportsSample.toList
  }

  /**
   * This Method returns a sample of economy Tweets from the following accounts:
   * - The Economist Tweets (https://twitter.com/theeconomist)
   * - Yahoo Finance Tweets (https://twitter.com/YahooFinance)
   * - Bloomberg Business Tweets (https://twitter.com/business)
   * - BBC Business Tweets (https://twitter.com/bbcbusiness)
   * - CNN Money Tweets (https://twitter.com/cnnmoney)
   *
   * @return a sample of economy Tweets.
   */
  def trainingSampleEconomy() : List[String] = {
    var economySample: scala.collection.mutable.MutableList[String] = scala.collection.mutable.MutableList()

    //The Economist Tweets (https://twitter.com/theeconomist)
    economySample += "Mining bitcoin has become a huge, ruthlessly competitive business. But is it a profitable one? http://econ.st/1Iyl58g"
    economySample += "What is the future of pharmaceuticals? New medicines may not be too good for health budgets: http://econ.st/1DBFSGS"
    economySample += "#Dailychart: Oil has fallen by around 55% since its 2014 highpoint of $115 a barrel http://econ.st/1DkJCiU"
    economySample += "A new book on the history of cotton gives a fine account of 900 years of globalisation http://econ.st/1DtbHln"
    economySample += "US oil price dips below $50 a barrel. Why is the price of oil tumbling? http://econ.st/1DtzNfP  #econarchive"
    economySample += "We will need to produce more food in the next 40 years than in the previous 10,000 combined http://econ.st/13Wg37z"
    economySample += "#Dailychart: Asia will be the fastest growing region in the world this year http://econ.st/1DoCrU6"
    economySample += "Environmental regulations may not cost as much as governments and businesses fear http://econ.st/1K5gKgb"
    economySample += "Growth in America is likely to be robust in 2015 and will start to benefit ordinary families http://econ.st/1xku76I"
    economySample += "A millionaire, financier, notorious businessman and ex-convict. The man who transformed London http://econ.st/1vjfxYQ"

    //Yahoo Finance Tweets (https://twitter.com/YahooFinance)
    economySample += "JPM Chief U.S. Economist Michael Feroli says the gloomy dip in average hourly earnings in December is likely a fluke. http://yhoo.it/1ATVkil" //https://twitter.com/YahooFinance/status/553744057496002560
    economySample += "Fitch Ratings slices Russia’s credit rating to ‘BBB-‘ from ‘BBB’ with a ‘negative’ outlook."
    economySample += "President Barack Obama's publicly-funded community college plan is seen costing about $60 billion over ten years. http://yhoo.it/1ASzXxV" //https://twitter.com/YahooFinance/status/553654998794784770
    economySample += "Wall Street poised to snap two-day rally as traders digest mixed jobs data. http://yhoo.it/1KtpGMw"
    economySample += "Labor Market Momentum Continues; High-Wage Job Gains Outpace Low-Wage Hiring http://joebrusuelas.tumblr.com/post/107600657337/labor-market-momentum-continues-high-wage-job … via @joebrusuelas"
    economySample += "2015 is a pay period leap year, which means there are 27 pay periods instead of the usual 26. http://yhoo.it/1BFxQ0n"
    economySample += "More than a dozen new Zara stores planned for the US in 2015 http://yhoo.it/1Dq0dle"
    economySample += "Dunkin Donuts wants to be all over China in 2035, plans for 1,400 new stores http://yhoo.it/1BEorWZ  $DNKN"
    economySample += "Coca-Cola eliminating up to 1,800 jobs as part of cost-cutting effort http://yhoo.it/1BEoNwH  $KO"
    economySample += "Russian hedge fund manager Kim Karapetyan is missing...and so, it seems, are all $20 million of his firm's assets. http://yhoo.it/141UByi"

    //Bloomberg Business Tweets (https://twitter.com/business)
    economySample += "The U.S. is back at the helm of the global economy http://bloom.bg/1DtSrXw" //https://twitter.com/business/status/553687888698044418
    economySample += "What would Greece's exit from the euro--so-called \"Grexit\"--mean for the country and markets? http://bloom.bg/1rX4zw2"
    economySample += "Oil prices below $60 a barrel are testing the shale-led U.S. drive for energy independence. http://bloom.bg/1rWTA5N"
    economySample += "Young home buyers are returning to the property market in the U.S. as the economy picks up. http://bloom.bg/1DowTss"
    economySample += "The euro hit its lowest level vs. the dollar in almost nine years amid speculation on ECB QE. http://bloom.bg/1zNPBGs"
    economySample += "The best and worst investments of 2014 http://bloom.bg/1BdlwEv"
    economySample += "BREAKING: U.S. imposes new sanctions on North Korea in response to Sony hack http://bloom.bg/1wKIJaA"
    economySample += "ECB President Mario Draghi has hinted that more easing may be needed to counter deflation. http://bloom.bg/140aOnK"
    economySample += "Home prices in Singapore suffer their longest losing streak in a decade http://bloom.bg/1vzoMU0"
    economySample += "Low inflation in the U.S. won't stop the Fed from raising rates in mid-2015, economists say. http://bloom.bg/1y5P8Un"

    //BBC Business Tweets (https://twitter.com/bbcbusiness)
    economySample += "Asian stocks in mostly upbeat mood http://bbc.in/1BGdNyN"
    economySample += "Petrol prices hit five year low http://bbc.in/1BDlOo8"
    economySample += "Deflation in eurozone as prices fall http://bbc.in/1BydiXE"
    economySample += "UK new car sales hit 10-year high http://bbc.in/1By4gtL"
    economySample += "Euro falls to nine-year low, China's Xiaomi doubles revenue. BBC business headlines http://bbc.in/1Afc2ts"
    economySample += "UK manufacturing growth 'slows' http://bbc.in/140d2DJ"
    economySample += "Goldman Sachs UK staff get top bonus http://bbc.in/13X71rm"
    economySample += "Venezuelan economy enters recession http://bbc.in/1xxjnRz"
    economySample += "Xiaomi most valuable tech start-up http://bbc.in/1xu7ko1"
    economySample += "House prices 'rise £16,000' in 2014 http://bbc.in/1AVgSL8"

    //CNN Money Tweets (https://twitter.com/cnnmoney)
    economySample += "Minimum wage was only 25 cents in 1938. Check out how it's changed over the years http://cnnmon.ie/1tTGqYC"
    economySample += "\"We're all out of jobs now\": Cheap gas takes its toll on oil workers http://cnnmon.ie/1FiY6S6"
    economySample += "It took 24 years of playing hooky to finally get an Indian gov't worker fired http://cnnmon.ie/1tTWslo"
    economySample += "Citigroup traders' coveted bonus checks are going to be smaller this year http://cnnmon.ie/1I4lgK2"
    economySample += "Where are America's jobs, and who's getting hired? http://cnnmon.ie/1DGZUjx"
    economySample += "Tough Friday on Wall Street as Dow sheds 169 points and S&P 500 falls 0.8%. All three indexes end week in red. http://cnnmon.ie/bkgnews"
    economySample += "Wages will tick higher in 2015 but you may be disappointed if you're expecting a big raise http://cnnmon.ie/14Aohmw"
    economySample += "Employers beware: 35% of workers say they'll quit if they don't get a raise this year http://cnnmon.ie/1tSLRH6"
    economySample += "Blackout averted: India's coal workers call off massive strike http://cnnmon.ie/1tOBbnt"
    economySample += "Whew. After their worst start since 2008, stocks are back on an upward swing http://cnnmon.ie/1x0jMIg"

    economySample.toList
  }

  /**
   * This Method returns a sample of technology Tweets from the following accounts:
   * - David Pogue (https://twitter.com/pogue)
   * - Wired (https://twitter.com/wired)
   * - ARSTechnica (https://twitter.com/arstechnica)
   * - GIGA OM (https://twitter.com/gigaom)
   * - NYTimes Bits (https://twitter.com/nytimesbits)
   *
   * @return a sample of technology Tweets.
   */
  def trainingSampleTechnology() : List[String] = {
    var techSample: scala.collection.mutable.MutableList[String] = scala.collection.mutable.MutableList()

    //David Pogue Tweets (https://twitter.com/pogue)
    techSample += "The clearest explanation yet of where we are with 4K television, and how it may succeed even though nobody wants it. http://j.mp/14lWFBy"
    techSample += "From CES: This self-balancing skateboard/Segway thing is amazing. http://j.mp/1wQVFvA"
    techSample += "The 11th Day of Techness! (30-sec tech gift videos.) Today: The first flash drive for iPhone… http://j.mp/1wcrCyf"
    techSample += "The TENTH Day of Techness (30-sec gift ideas): the phone case with a camera remote built in! http://j.mp/13lNbFx"
    techSample += "The 9th Day of Techness (30-sec videos): a phone case with impossibly thin reading glasses! http://j.mp/13hrbvI"
    techSample += "On the Mac, you can FINALLY mass-rename your files! Here’s the trick, in 30 seconds. http://yhoo.it/1rtKlog"
    techSample += "My new column & video: HP Sprout. A PC with two giant touch screens, instantly scans 2D and 3D objects. A flawed gem. http://j.mp/1xU1IV7"
    techSample += "Smackdown: MacBook Air vs. Microsoft Surface 3 vs. Lenovo’s new Yoga 3, which contorts into 4 different positions. http://j.mp/1FkS9iv"
    techSample += "The Pogue Review: The Microsoft Band health band/smartwatch. 10 sensors, sophisticated, bulky, and amazing. http://j.mp/1thWUlX"
    techSample += "Excellent explanation of how Verizon has been tracking you online. (AT&T, a little. Sprint and T-Mo, not at all.) http://j.mp/1pib8Zb"

    //Wired Tweets (https://twitter.com/wired)
    techSample += "How \"Pong\" and the Odyssey console launched the videogame era http://wrd.cm/1BJMaov"
    techSample += "Linux’s creator wants us all to chill out about the leap second http://wrd.cm/14BFc8f"
    techSample += "This autopilot tech could be a total game-changer for drones http://wrd.cm/1AT8w6X"
    techSample += "Cheap cameras and boring sensors make the best smart home stuff. Here's why: http://wrd.cm/1xRZq8e"
    techSample += "The coolest things we found at CES yesterday, from bionic birds to beer-brewing robots http://wrd.cm/1Bzf0rA"
    techSample += "3-D printed prosthetics that look fit for a sci-fi warrior http://wrd.cm/1tOFULu"
    techSample += "Open source databases keep chipping away at Oracle’s empire http://wrd.cm/14nGSSO"
    techSample += "Razor's new Android set-top box also streams PC games to your TV http://wrd.cm/1Im0Zho"
    techSample += "Simple pictures that state-of-the-art A.I. still can’t recognize http://wrd.cm/1wQB8Y3"
    techSample += "Today Lenovo unveiled a line of multi-mode, flip-screen laptops http://wrd.cm/1Fc6naz"

    //ARSTechnica Tweets (https://twitter.com/arstechnica)
    techSample += "Bitstamp reopens bitcoin exchange, adds security precautions http://ars.to/1xZhmRk by @roblemos"
    techSample += "Alibaba to join Microsoft’s fight against pirate software in China http://ars.to/1BJhNhW by @drpizza"
    techSample += "DDoS service targeting PSN and Xbox powered by home Internet routers http://ars.to/1BJfrj9  by @dangoodin001"
    techSample += "AT&T tells FCC it can’t treat mobile data as a common carrier service http://ars.to/1BVwyfW  by @JBrodkin"
    techSample += "On net neutrality, Internet providers are betrayed by one of their own http://ars.to/1Iwi2gG  by @JBrodkin"
    techSample += "MT @KyleOrl Shorter \"How hackers got IRC running on an SNES through a Pokemon game\": a wizard did it -- http://arstechnica.com/gaming/2015/01/pokemon-plays-twitch-how-a-robot-got-irc-running-on-an-unmodified-snes/"
    techSample += "Got an Asus router? Someone on your network can probably hack it http://ars.to/146aZh0  by @dangoodin001"
    techSample += "The Great Firewall keeps growing, as China blocks all Gmail access http://ars.to/1xtamcf  by @joemullin"
    techSample += "Hackers DDoS PSN, XBox Live on XMas MT @kenpex Here's why you don't make shit that does require to be -always- online http://arstechnica.com/security/2014/12/grinches-steal-christm"
    techSample += "Apple automatically patches Macs to fix severe NTP security flaw http://ars.to/16MFJ8K  by @AndrewWrites"

    //GIGA OM Tweets (https://twitter.com/gigaom)
    techSample += "As HomeKit arrives, will smart home devices still love Android? http://bit.ly/14vQrz1"
    techSample += "MongoDB confirms an $80M funding round https://gigaom.com/2015/01/09/mongodb-confirms-an-80m-funding-round/ … by @JonathanVanian" //https://twitter.com/gigaom/status/553708910146637824
    techSample += "Artificial intelligence is real now and it’s just getting started https://gigaom.com/2015/01/09/artificial-intelligence-is-real-now-and-its-just-getting-" //https://twitter.com/gigaom/status/553627695205326849
    techSample += "New Google cloud tool lets clients monitor app performance http://bit.ly/1BF5Jyk" //https://twitter.com/gigaom/status/553361083273338880
    techSample += "The LG WebOS smartwatch is real, and reportedly launching in 2016 http://bit.ly/14u080U"
    techSample += "Rethink Robotics nabs $26.6M to keep building easy-to-train robots http://bit.ly/1BDORrM"
    techSample += "Cheap cloud + open source = a great time for startups http://bit.ly/1BCnDSt"
    techSample += "Amazon Web Services tops list of most reliable public clouds http://bit.ly/14qsNE5"
    techSample += "These washable smart socks can reduce injuries from running http://bit.ly/1BAxtUU"
    techSample += "Check out Intel's $149, 4-inch, PC in your pocket Compute Stick: http://bit.ly/14pmIYH #CES"

    //NYTimes Bits Tweets (https://twitter.com/nytimesbits)
    techSample += "Intel Betting on (Customized) Commodity Chips for Cloud Computing http://nyti.ms/1sFIVHC" //https://twitter.com/nytimesbits/status/545912219926818816
    techSample += "U.S. Said to Find North Korea Ordered Cyberattack on Sony http://nyti.ms/1sC8vgV" //https://twitter.com/nytimesbits/status/545433024926851072
    techSample += "Machine Learning: Improving In-Flight Wi-Fi and Streaming From Virgin America, JetBlue and More http://nyti.ms/1zw6w3X" //https://twitter.com/nytimesbits/status/545405789947834368
    techSample += "Game Consoles are Using More Electricity Than Ever http://nyti.ms/1sxN2FD" //https://twitter.com/nytimesbits/status/544976417205673984
    techSample += "Big Data Companies Turn Focus to Support, and Away From Proprietary Software http://nyti.ms/1sth90V" //https://twitter.com/nytimesbits/status/544464833656397824
    techSample += "News Analysis: Where Tech Giants Protect Privacy http://nyti.ms/1sqHk8F" //https://twitter.com/nytimesbits/status/543912537608421377
    techSample += "Gadgetwise: A Review of Digital Technology for Children http://nyti.ms/1x3F7GK" //Gadgetwise: A Review of Digital Technology for Children http://nyti.ms/1x3F7GK
    techSample += "Apple and IBM Introduce First Offerings in Line of Mobile Apps for Business http://nyti.ms/1sirQ6r" //https://twitter.com/nytimesbits/status/542673452659146752
    techSample += "Copenhagen Lighting the Way to Greener, More Efficient Cities http://nyti.ms/1x1ABbM" //https://twitter.com/nytimesbits/status/542674581774139392

    techSample.toList
  }

}
