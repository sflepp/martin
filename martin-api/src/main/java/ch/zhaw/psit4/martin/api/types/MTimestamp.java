package ch.zhaw.psit4.martin.api.types;

import org.joda.time.Instant;
import org.joda.time.Partial;
import org.json.JSONObject;

public class MTimestamp extends BaseType {
	private Instant instant;
	private Partial partial;

	public MTimestamp(String data) {
		super(data);
	}

	public Long getTimestamp() {
		return instant.getMillis();
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);
		json.put("timestamp", instant.getMillis());
		json.put("partial", partial.toString());

		return json.toString(4);
	}
	
	public Instant getInstant() {
		return instant;
	}

	public void setInstant(Instant instant) {
		this.instant = instant;
	}

	public Partial getPartial() {
		return partial;
	}

	public void setPartial(Partial partial) {
		this.partial = partial;
	}
}
