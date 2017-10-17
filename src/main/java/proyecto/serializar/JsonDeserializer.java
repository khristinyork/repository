package proyecto.serializar;


import org.apache.kafka.common.serialization.Deserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
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

public class JsonDeserializer implements Deserializer<Map<String, Object>> {
    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);

    ObjectMapper mapper = new ObjectMapper();

    public void configure(Map configs, boolean isKey) {

    }

    public Map<String, Object> deserialize(String topic, byte[] data) {
        Map<String, Object> json = null;
        if(data != null) {
            try {
                json = mapper.readValue(data, Map.class);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return json;
    }

    public void close() {

    }
}
