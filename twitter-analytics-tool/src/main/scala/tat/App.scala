package tat

/* SimpleApp.scala */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

/**
 * @author ${user.name}
 */
object App {
  
	def main(args: Array[String]) {
    	
		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)

    	val data = sc.textFile("/opt/spark/README.md", 2).cache()

    	//val a = data.filter(line => line.contains("Spark")).count()


    	//val count = textFile.count()
		//println("Text file count: " + count)

  	}

}