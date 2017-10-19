package proyecto.consumidor;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import proyecto.jsonweather.Weather;
import proyecto.serializersAnDtranformers.JsonTransformer;

public class Consumidor {
	// public static String KAFKA_HOST = " 172.31.20.14:9090, 172.31.20.14:9091,
	// 172.31.20.14:9092";
	public static String KAFKA_HOST = "localhost:9092";
	public static String TOPIC = "datos";
	private static final AtomicBoolean closed = new AtomicBoolean(false);

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Shutting down");
				closed.set(true);
			}
		});

		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "grupo1");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		// props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.ByteArrayDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		//props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,30);
		//props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG,40000);
		//props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,30000);
		//max.poll.records

		// KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(TOPIC));
		String value = "";
		Weather w = null;
		while (!closed.get()) {
			// ConsumerRecords<String, byte[]> records = consumer.poll(100);
			ConsumerRecords<String, String> records = consumer.poll(100);
			// for (ConsumerRecord<String, byte[]> record : records) {
			for (ConsumerRecord<String, String> record : records) {
				System.out.printf("partition = %2d   offset = %5d   key = %7s timestamp = %8s  value = %12s\n",
						record.partition(), record.offset(), record.key(), String.valueOf(record.timestamp()),
						record.value());
				value = record.value();
				w = new Weather();
				// TODO enviar record.value() aun nuevo método transfomerSchema que devuelve un
				// string
				// en forma de schemaAVRO
				JsonTransformer.transformerToweatherSchema(record.value(), w);

			}

			consumer.close();
		}
	}
}
