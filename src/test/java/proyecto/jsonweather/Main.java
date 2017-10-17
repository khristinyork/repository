package proyecto.jsonweather;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Main {

	private double temp;
	private int pressure;
	private int humidity;
	private double tempMin;
	private double tempMax;
	@Valid
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public Main() {
	}

	/**
	 * 
	 * @param humidity
	 * @param pressure
	 * @param tempMax
	 * @param temp
	 * @param tempMin
	 */
	public Main(double temp, int pressure, int humidity, double tempMin, double tempMax) {
		super();
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public double getTempMin() {
		return tempMin;
	}

	public void setTempMin(double tempMin) {
		this.tempMin = tempMin;
	}

	public double getTempMax() {
		return tempMax;
	}

	public void setTempMax(double tempMax) {
		this.tempMax = tempMax;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("temp", temp).append("pressure", pressure).append("humidity", humidity)
				.append("tempMin", tempMin).append("tempMax", tempMax)
				.append("additionalProperties", additionalProperties).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(humidity).append(pressure).append(tempMax).append(additionalProperties)
				.append(temp).append(tempMin).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Main) == false) {
			return false;
		}
		Main rhs = ((Main) other);
		return new EqualsBuilder().append(humidity, rhs.humidity).append(pressure, rhs.pressure)
				.append(tempMax, rhs.tempMax).append(additionalProperties, rhs.additionalProperties)
				.append(temp, rhs.temp).append(tempMin, rhs.tempMin).isEquals();
	}

}