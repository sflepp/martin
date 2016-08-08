package ch.zhaw.psit4.martin.models;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import ch.zhaw.psit4.martin.timing.TimingInfo;

@Entity
@Table(name = "response")
@Access(AccessType.FIELD)
public class MResponse extends BaseModel {

	@Transient
	private List<MOutput> responses = new ArrayList<>();

	@Transient
	private List<TimingInfo> timingInfo = new ArrayList<>();

	public MResponse() {
	}

	public MResponse(List<MOutput> responseList) {
		this.responses = responseList;
	}

	public MResponse(String text) {
		setSingleResponse(MOutputType.TEXT, text);
	}

	public List<MOutput> getResponses() {
		return responses;
	}

	public void setResponseList(List<MOutput> responseList) {
		this.responses = responseList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MResponse)) {
			return false;
		}
		final MResponse r = (MResponse) obj;
		if (this.getId() != r.getId() || !this.getResponses().equals(r.getResponses())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return super.hashCode() * (this.getId() + this.getResponses().hashCode()) * 7;
	}

	public List<TimingInfo> getTimingInfo() {
		return timingInfo;
	}

	public void setTimingInfo(List<TimingInfo> timingInfo) {
		this.timingInfo = timingInfo;
	}

	public void setResponseErrorText(String errorMessage) {
		setSingleResponse(MOutputType.ERROR, errorMessage);
	}

	@NotNull
	@Column(name = "responsetext")
	@Access(AccessType.PROPERTY)
	String getResponseText() {
		Gson gson = new Gson();
		return gson.toJson(responses);

	}

	void setResponseText(String jsonText) {
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<MOutput>>() {
		}.getType();
		responses = gson.fromJson(jsonText, listType);
	}

	public void setSingleResponse(MOutputType type, String text) {
		List<MOutput> response = new ArrayList<>();
		response.add(new MOutput(type, text));
		this.responses = response;
	}

	public void addResponse(MOutputType type, String text) {
		responses.add(new MOutput(type, text));
	}
}
