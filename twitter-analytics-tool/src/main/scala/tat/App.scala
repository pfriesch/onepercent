package tat

/* SimpleApp.scala */
//import org.apache.spark.SparkContext
//import org.apache.spark.SparkContext._
//import org.apache.spark.SparkConf
import org.apache.spark._

/**
 * @author ${user.name}
 */
object App {
  
	def main(args: Array[String]) {
    	
		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)

    	val data = sc.textFile(args(0), 2).cache()

    	val a = data.filter(line => line.contains("Spark")).count()
		
		println("Text file count: " + a)

  	}

}