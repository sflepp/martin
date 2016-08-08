package ch.zhaw.psit4.martin.api.types;

import java.util.Optional;

import org.json.*;

import ch.zhaw.psit4.martin.api.language.parts.ISentence;

public abstract class BaseType implements IBaseType {
	protected String data;
	
	protected Optional<ISentence> parentSentence = Optional.ofNullable(null);

	public BaseType(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Optional<ISentence> getParentSentence() {
		return parentSentence;
	}

	public void setParentSentence(ISentence parentSentence) {
		this.parentSentence = Optional.ofNullable(parentSentence);
	}

	@Override
	public String toString() {
		return data;
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("type", this.getClass().getName());
		json.put("data", data);
		if(parentSentence.isPresent()){
			json.put("parentSentence", parentSentence.toString());
		}
		return json.toString(4);
	}
}
