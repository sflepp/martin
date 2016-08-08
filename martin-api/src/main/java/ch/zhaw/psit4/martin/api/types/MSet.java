package ch.zhaw.psit4.martin.api.types;

import org.joda.time.Duration;
import org.json.JSONObject;

public class MSet extends BaseType{
	
	Duration duration;

	public MSet(String data) {
		super(data);
	}
	
	public void setDuration(Duration duration){
		this.duration = duration;
	}
	
	public Duration getDuration(){
		return this.duration;
	}
	
	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);
		json.put("duration", duration.getMillis());

		return json.toString(4);
	}
}
