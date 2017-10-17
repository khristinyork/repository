package proyecto.productor;

import org.apache.kafka.clients.producer.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.codehaus.jackson.map.ObjectMapper;
import com.google.common.net.HttpHeaders;



import proyecto.serializar.JsonSerializer;
import proyecto.serializersAnDtranformers.JsonTransformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Productor {
	// public static String brokerList ="localhost:9090,localhost:9091,localhost:9092";
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	public final static String APPLICATION_JSON = "application/json";
	public static String brokerList = "localhost:9092";
	public static String topic = "datos";
	public static String[] capitales = { "51537", "112931", "146268", "160196", "167541", "184745", "223816", "232422",
			"250441", "276781", "285787", "287286", "290030", "290340", "292968", "323786", "343300", "360630",
			"365618", "425374", "444178", "524901", "588409", "616912", "618069", "625144", "658226", "676742",
			"703448", "727011", "785842", "786714", "890299", "908248", "909137", "921772", "921772", "932505",
			"933773", "934154", "934985", "964137", "975511", "1040652", "1070940", "1138958", "1176615", "1230089",
			"1252416", "1273840", "1282027", "1283678", "1298987", "1298987", "1348591", "1484839", "1526273",
			"1528334", "1561096", "1619460", "1630798", "1631845", "1645457", "1659203", "1668341", "1699806",
			"1701668", "1735106", "1735161", "1816670", "1820906", "1843564", "1846735", "2028462", "2075807",
			"2081918", "2108502", "2110257", "2110384", "2113779", "2135171", "2161311", "2172517", "2198148",
			"2207349", "2210247", "2220957", "2240449", "2253354", "2260535", "2279755", "2306104", "2312096",
			"2352778", "2365266", "2374776", "2377450", "2389853", "2392087", "2395635", "2399697", "2408770",
			"2410758", "2413876", "2422488", "2427123", "2460596", "2472818", "2538474", "2562955", "2595294",
			"2673722", "2759794", "2800865", "2960316", "2962486", "2988507", "2988507", "2993458", "3028258",
			"3054643", "3060972", "3065330", "3094325", "3164670", "3183875", "3186886", "3191281", "3193044",
			"3196359", "3374333", "3383330", "3416900", "3441575", "3469058", "3474570", "3575551", "3575551",
			"3575635", "3579926", "3583361", "3598132", "3600949", "3617763", "3621841", "3646738", "3652462",
			"3678415", "3689793", "3703433", "3718426", "3739034", "3846616", "3903987", "3936456", "3983689",
			"4032402", "4140963", "4215307", "4292686", "4516749", "4562695", "4713813", "4792273", "4905006",
			"5138935", "5374175" };
	//public static byte[] serializar(String url, String key) {
	public static String serializar(String url, String key) {
		Map<String, Object> data = new  HashMap<String, Object>();
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader(HttpHeaders.ACCEPT, APPLICATION_JSON);

		//byte[] raw = null;

		HttpResponse response;
		HttpEntity entity;
		String respStr="";
		String respStrEnv="";
		try {
			response = client.execute(getRequest);
			entity=response.getEntity();
			respStr = EntityUtils.toString(entity);
			respStrEnv=JsonTransformer.transformer(respStr);
			//{ "lon": 31.05, "lat": -17.83, "temp_max": 304.15, "temp_min": 304.15, "temp": 304.15, "pressure": 1022, "humidity": 14 } 		
			//JsonSerializer jsonSerial = new JsonSerializer();
			//raw = jsonSerial.serialize(key, respStr);// JSON_MAPPER.writeValueAsBytes(data);
		} catch (IOException ex) {
			Object logger;
			System.out.println("Error retrieving   " + ex.toString());
		}
		//return raw;
		return respStrEnv;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		final AtomicBoolean closed = new AtomicBoolean(false);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Shutting down");
				closed.set(true);
			}
		});
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		//props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "proyecto.particioner.SimplePartitioner");

		//Producer<String, byte[]> producer = new KafkaProducer<>(props);
		Producer<String, String> producer = new KafkaProducer<>(props);
		
		String url = "";
		while (!closed.get()) {
			for (int i = 0; i < capitales.length; i++) {
				url = "http://api.openweathermap.org/data/2.5/weather?id=" + capitales[i]
						+ "&APPID=9398ed1aa953fe02d3725e32f2ccaa6a";
				//byte[] mensaje = serializar(url, capitales[i]);
				String mensaje = serializar(url, capitales[i]);
				//producer.send(new ProducerRecord<String, byte[]>(topic, capitales[i], mensaje));
				producer.send(new ProducerRecord<String, String>(topic, capitales[i], mensaje));
				//System.out.println("Sending message with: "+mensaje+" key: " + capitales[i]);
				Thread.sleep(1000);
			}
		}

		producer.flush();
		producer.close();
	}

}
