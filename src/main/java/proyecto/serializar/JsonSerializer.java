package proyecto.serializar;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.annotation.*;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonSerializer implements Serializer<Map<String, String>> {
   
    ObjectMapper mapper = new ObjectMapper();

    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nothing to do
    }

    public byte[] serialize(String topic, String respStr) {
        byte[] raw = null;
        try {
        	//configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
            raw = mapper.writeValueAsBytes(respStr);
        } catch (JsonProcessingException e) {
            System.out.printf(e.getMessage(), e);
        } catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return raw;
    }

    public void close() {
        // Nothing to do
    }

	@Override
	public byte[] serialize(String topic, Map<String, String> data) {
		// TODO Auto-generated method stub
		return null;
	}
}
