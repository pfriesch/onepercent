Twitter-Analytics-Tool
======================

A tool to analyze the twitter stream an visualize them

Use:
spark-submit --class tat.App --master local[2] <pathToRepository>/twitter-analytics-tool/target/twitter-analytics-tool-0.0.1.jar "<JSON File path>" 


##### Running on the Cluster
Get Binarys for [Apache Spark 1.1.0](http://d3kbcqa49mib13.cloudfront.net/spark-1.1.0-bin-hadoop2.4.tgz) and extract them.
Use:

>spark-1.1.0/bin/spark-submit --class tat.App --master spark://hadoop03.f4.htw-berlin.de:60001 --driver-java-options "-Dspark.executor.memory=8G" <pathToRepository>/twitter-analytics-tool/target/twitter-analytics-tool-0.0.2.jar "JSON File path" 
