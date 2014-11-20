Twitter-Analytics-Tool
======================

A tool to analyze the twitter stream an visualize them

Please read before using:
args0: The JSON file you want to analyse.
args1: topX of the hashtags

##### Running Local
spark-submit --class tat.App --master local[2] <pathToRepository>/twitter-analytics-tool/target/twitter-analytics-tool-0.0.1.jar args0 args1 


##### Running on the Cluster
Get Binarys for [Apache Spark 1.1.0](http://d3kbcqa49mib13.cloudfront.net/spark-1.1.0-bin-hadoop2.4.tgz) and extract them.
Use:

>spark-1.1.0/bin/spark-submit --class tat.App --master spark://hadoop03.f4.htw-berlin.de:60001 --driver-java-options "-Dspark.executor.memory=8G" <pathToRepository>/twitter-analytics-tool/target/twitter-analytics-tool-0.0.2.jar args0 args1
