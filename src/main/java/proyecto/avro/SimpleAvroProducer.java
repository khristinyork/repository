package proyecto.avro;

import com.twitter.bijection.Injection;
import com.twitter.bijection.avro.GenericAvroCodecs;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class SimpleAvroProducer {
	public static final String 
	USER_SCHEMA = 
	"{" + "\"type\":\"record\"," +
          "\"name\":\"myrecord\"," + 
		  "\"fields\":["+
	                       "{\"name\":\"lon\",\"type\":\"int\"}," + 
			               "{\"name\":\"lat\",\"type\":\"int\"},"+
				           "{\"name\":\"temp\",\"type\":\"int\"}" + 
		             "]"+
    "}";
	final String WEATHER_SCHEMA = 
			"{" + "\"type\":\"record\"," +
		          "\"name\":\"myrecord\"," + 
				  "\"fields\":["+
			                       "{\"name\":\"lon\",\"type\":\"int\"}," + 
					               "{\"name\":\"lat\",\"type\":\"int\"},"+
						           "{\"name\":\"temp\",\"type\":\"int\"}" + 
						           "{\"name\":\"pressure\",\"type\":\"int\"}" + 
						           "{\"name\":\"humidity\",\"type\":\"int\"}" +
						           "{\"name\":\"temp_min\",\"type\":\"int\"}" +
						           "{\"name\":\"temp_max\",\"type\":\"int\"}" +
				             "]"+
		    "}";

	public static void main(String[] args) throws InterruptedException {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

		Schema.Parser parser = new Schema.Parser();
		Schema schema = parser.parse(USER_SCHEMA);
		Injection<GenericRecord, byte[]> recordInjection = GenericAvroCodecs.toBinary(schema);

		KafkaProducer<String, byte[]> producer = new KafkaProducer<>(props);

		for (int i = 0; i < 1000; i++) {
			GenericData.Record avroRecord = new GenericData.Record(schema);
			avroRecord.put("lon",  9*i);
			avroRecord.put("lat", 8*i);
			avroRecord.put("temp", i*2);

			byte[] bytes = recordInjection.apply(avroRecord);

			ProducerRecord<String, byte[]> record = new ProducerRecord<>("mytopic", bytes);
			producer.send(record);

			Thread.sleep(250);

		}

		producer.close();
	}

}
