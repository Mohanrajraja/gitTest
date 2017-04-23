package org.sparkexamples.first_example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import scala.reflect.io.Path;

import java.nio.file.Paths;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * WordCountTask class, we will call this class with the test WordCountTest.
 */
public class WordCountTask {
  /**
   * We use a logger to print the output. Sl4j is a common library which works with log4j, the
   * logging system used by Apache Spark.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTask.class);

  /**
   * This is the entry point function when the task is called with spark-submit.sh from command
   * line. In our example we will call the task from a WordCountTest instead.
   * See {@see http://spark.apache.org/docs/latest/submitting-applications.html}
   */
  public static void main(String[] args) {
	 //String path =  "E:\Studies\hdata\Spark" ;
	 java.nio.file.Path sourceFilePath =  Paths.get("E:/Studies/hdata/Spark/WordCount.txt");
	 System.out.println(" path   : "+ sourceFilePath.toString());
	 System.out.println(" path   : "+ args[0].toString() + " " + args.length);
	 
    //checkArgument(args.length < 1, "Please provide the path of input file as first parameter.");
    //new WordCountTask().run(sourceFilePath.toString());
    new WordCountTask().run(args[0], args[1]);
  }

  /**
   * The task body
   */
  public void run(String inputFilePath, String OutputDirectory) {
    /*
     * This is the address of the Spark cluster. We will call the task from WordCountTest and we
     * use a local standalone cluster. [*] means use all the cores available.
     * See {@see http://spark.apache.org/docs/latest/submitting-applications.html#master-urls}.
     */
    String master = "local[*]";

    /*
     * Initialises a Spark context.
     */
    SparkConf conf = new SparkConf()
        .setAppName(WordCountTask.class.getName())
        .setMaster(master);
    JavaSparkContext context = new JavaSparkContext(conf);

    /*
     * Performs a work count sequence of tasks and prints the output with a logger.
     */
    context.textFile(inputFilePath).flatMap(text -> Arrays.asList(text.split(" ")).iterator())
    .mapToPair(word -> new Tuple2<>(word, 1))
    .reduceByKey((a, b) -> a + b).saveAsTextFile(OutputDirectory);
    
    
    //.foreach(result -> LOGGER.info(
    //    String.format("Word [%s] count [%d].", result._1(), result._2)));
    
  }
}