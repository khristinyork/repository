package proyecto.serializersAnDtranformers;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import processing.core.*;
import proyecto.jsonweather.Weather;

public class JsonTransformer {
	public static String transformer(String s0) {
		// La cadena de salida debe ser igual a esta=>
		// String s1 =
		// "{"\"lon\":89.64,\"lat\":27.47,\"temp\":277.837,\"pressure\":645.72,\"humidity\":58,\"temp_min\":277.837,"
		// + "\"temp_max\":277.837}";
		String cadenaSalida = "";
		String strLon = "{\"lon\":";
		String strlat = ",\"lat\":";
		String strtemp = ",\"temp\":";
		String strpressure = ",\"pressure\":";
		String strhumidity = ",\"humidity\":";
		String strtemp_min = ",\"temp_min\":";
		String strtemp_max = ",\"temp_max\":";
		String finalCadena = "}";
		try {
			// contenidoJson es tu string conteniendo el json.

			processing.data.JSONObject mainObject = new processing.data.JSONObject();
			processing.data.JSONObject obj3 = mainObject.parse(s0);
			Iterator<String> itkeys = obj3.keys().iterator();

			while (itkeys.hasNext()) {
				String key = itkeys.next();
				if (key.equals("coord")) {
					processing.data.JSONObject jsonObject = obj3.getJSONObject(key);
					int lon = jsonObject.getInt("lon");
					strLon = strLon + lon;
					cadenaSalida = strLon;
					int lat = jsonObject.getInt("lat");
					cadenaSalida = cadenaSalida + strlat + lat;
					// System.out.println("cadenaSalida: " + cadenaSalida);
				} else if (key.equals("main")) {
					processing.data.JSONObject jsonObject = obj3.getJSONObject(key);

					int temp = jsonObject.getInt("temp");
					cadenaSalida = cadenaSalida + strtemp + temp;
					int pressure = jsonObject.getInt("pressure");
					cadenaSalida = cadenaSalida + strpressure + pressure;
					int humidity = jsonObject.getInt("humidity");
					cadenaSalida = cadenaSalida + strhumidity + humidity;
					int temp_min = jsonObject.getInt("temp_min");
					cadenaSalida = cadenaSalida + strtemp_min + temp_min;
					int temp_max = jsonObject.getInt("temp_max");
					cadenaSalida = cadenaSalida + strtemp_max + temp_max + finalCadena;

					// System.out.println("cadenaSalida: " + cadenaSalida);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Parser :" + e.getMessage());
		}
		return cadenaSalida;
	}

	public static Weather transformerToweatherSchema(String s0, Weather w) {
		// La cadena que recibimos=>
		// String s1 =
		// "{"\"lon\":89.64,\"lat\":27.47,\"temp\":277.837,\"pressure\":645.72,\"humidity\":58,\"temp_min\":277.837,"
		// + "\"temp_max\":277.837}";
		try {

			processing.data.JSONObject mainObject = new processing.data.JSONObject();
			processing.data.JSONObject obj3 = mainObject.parse(s0);
			Iterator<String> itkeys = obj3.keys().iterator();

			while (itkeys.hasNext()) {
				String key = itkeys.next();
				//processing.data.JSONObject jsonObject = obj3.getJSONObject(key);
				if (key.equals("temp")) {
					int temp = obj3.getInt("temp");
					w.setTemp(temp);
				} else if (key.equals("lon")) {
					int lon = obj3.getInt("lon");
					w.setLon(lon);
				} else if (key.equals("lat")) {
					int lat = obj3.getInt("lat");
					w.setLat(lat);
				} else if (key.equals("pressure")) {
					int pressure = obj3.getInt("pressure");
					w.setPressure(pressure);
				} else if (key.equals("humidity")) {
					int humidity = obj3.getInt("humidity");
					w.setHumidity(humidity);
				} else if (key.equals("temp_min")) {
					int temp_min = obj3.getInt("temp_min");
					w.setTempMin(temp_min);
				} else if (key.equals("temp_max")) {
					int temp_max = obj3.getInt("temp_max");
					w.setTempMax(temp_max);
				}
			}

			System.out.println(
					"Weather schema longitud: " + w.getLon() + " latitud: " + w.getLat() + " temp: " + w.getTemp());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Parser :" + e.getMessage());
		}
		return w;
	}

	public static void main(String[] args) {
		String s1 = "{\"coord\":{" + "\"lon\":89.64," + "\"lat\":27.47},"
				+ "\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"base\":\"stations\","
				+ "\"main\":{" + "\"temp\":277.837," + "\"pressure\":645.72," + "\"humidity\":58,"
				+ "\"temp_min\":277.837," + "\"temp_max\":277.837," + "\"sea_level\":1018.96,"
				+ "\"grnd_level\":645.72},"
				+ "\"wind\":{\"speed\":0.77,\"deg\":167.502},\"clouds\":{\"all\":0},\"dt\":1508153690,\"sys\":{\"message\":0.0028,\"country\":\"BT\",\"sunrise\":1508112127,\"sunset\":1508153476},\"id\":1252416,\"name\":\"Thimphu\",\"cod\":200}";

		transformer(s1);

	}

}
