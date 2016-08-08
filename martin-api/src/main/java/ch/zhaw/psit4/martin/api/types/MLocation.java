package ch.zhaw.psit4.martin.api.types;

import java.util.Optional;

import org.json.JSONObject;

/**
 * A Location is a place or place or position.
 * 
 * This can be: - A geo-position on the surface of the earth - ... ?
 * 
 * @author simonflepp
 *
 */
public class MLocation extends BaseType {
	Optional<String> formattedAddress = Optional.ofNullable(null);
	Optional<Double> latitude = Optional.ofNullable(null);
	Optional<Double> longitude = Optional.ofNullable(null);

	public MLocation(String data) {
		super(data);
	}

	public Optional<String> getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(Optional<String> formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public Optional<Double> getLatitude() {
		return latitude;
	}

	public void setLatitude(Optional<Double> latitude) {
		this.latitude = latitude;
	}

	public Optional<Double> getLongitude() {
		return longitude;
	}

	public void setLongitude(Optional<Double> longitude) {
		this.longitude = longitude;
	}

	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);

		if (formattedAddress.isPresent()) {
			json.put("formattedAddress", formattedAddress.get());
		}
		if (latitude.isPresent()) {
			json.put("latitude", latitude.get());
		}
		if (longitude.isPresent()) {
			json.put("longitude", longitude.get());
		}
		return json.toString(4);
	}
}
