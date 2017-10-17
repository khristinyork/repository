package proyecto.spark;

import java.util.HashMap;
import java.util.HashSet;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import scala.Tuple2;

import com.google.common.collect.Lists;
import kafka.serializer.*;
import proyecto.jsonweather.CapitalesdelMundo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
//org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.dstream.DStream;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import scala.Tuple2;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.Durations;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Consumes messages from one or more topics in Kafka and does wordcount. Usage:
 * JavaDirectKafkaWordCount <brokers> <topics> <brokers> is a list of one or
 * more Kafka brokers <topics> is a list of one or more kafka topics to consume
 * from
 *
 * Example: $ bin/run-example streaming.JavaDirectKafkaWordCount
 * broker1-host:port,broker2-host:port topic1,topic2
 */

public class SparkStreaming {

	private static final Pattern SPACE = Pattern.compile(" ");
	private static final byte[] Array = null;

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: JavaDirectKafkaWordCount <brokers> <topics>\n"
					+ "  <brokers> is a list of one or more Kafka brokers\n"
					+ "  <topics> is a list of one or more kafka topics to consume from\n\n");
			System.exit(1);
		}

		// String brokers = "172.31.20.14:9092,172.31.20.14:9090,172.31.20.14:9091";
		String brokers = "192.168.1.71:9092";
		String topics = "datos";

		// Create context with a 2 seconds batch interval
		SparkConf sparkConf = new SparkConf().setAppName("JavaDirectKafkaWordCount");
		// Duration => duracion de batch
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(1));

		HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
		// java.util.List<String> topicsSet = new
		// ArrayList<String>(Arrays.asList(topics.split(",")));
		Map<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokers);

		// Create direct kafka stream with brokers and topics
		JavaPairInputDStream<String, String> messages1 = KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);
		
		DStream<Tuple2<String, String>> dstreamTuple2 = messages1.dstream();
		//public JavaDStream<T> filter(Function<T,java.lang.Boolean> f)
		//TODO tratar el value del map que viene en String parsearlo a json

		
		// Get the lines, split them into words, count the words and print
		JavaDStream<String> lines = messages1.map(new Function<Tuple2<String, String>, String>() {
			@Override
			public String call(Tuple2<String, String> tuple2) {
				return tuple2._2();
			}
		});
		JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterable<String> call(String x) {
				return Lists.newArrayList(SPACE.split(x));
			}
		});
		JavaPairDStream<String, Integer> wordCounts = words.mapToPair(new PairFunction<String, String, Integer>() {
			@Override
			public Tuple2<String, Integer> call(String s) {
				return new Tuple2<String, Integer>(s, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});
		wordCounts.print();

		// Start the computation
		jssc.start();
		jssc.awaitTermination();
	}
}
