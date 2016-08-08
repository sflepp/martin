package ch.zhaw.psit4.martin.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.models.MRequest;
import ch.zhaw.psit4.martin.models.MResponse;

/**
 * This class holds extended information about the request. It holds a possible
 * function call along with the required and optional arguments used to call the
 * plugin feature.
 *
 * @version 0.1
 **/
public class ExtendedRequest {
	/**
	 * The unique ID of this item. The id is distributed by the {@link UUID}
	 * implementation.
	 */
	private UUID id;
	/**
	 * The raw Request containing a command string.
	 */
	private MRequest request;

	/**
	 * The response to be returned.
	 */
	private MResponse response;

	/**
	 * List of possible Calls for the request ordered by possibility.
	 */
	private List<Call> calls;

	/**
	 * Parsed and analyzed sentence for further analysis.
	 */
	@Transient
	@JsonIgnore
	private AnnotatedSentence sentence;

	public ExtendedRequest(MRequest request, MResponse response) {
		this.calls = new ArrayList<Call>();
		this.id = UUID.randomUUID();
		this.request = request;
		this.response = response;
	}

	public UUID getID() {
		return this.id;
	}

	public void setID(UUID id) {
		this.id = id;
	}

	public MRequest getRequest() {
		return this.request;
	}

	public void setRequest(MRequest request) {
		this.request = request;
	}

	public void addCall(Call call) {
		this.calls.add(call);
	}

	public void setCalls(List<Call> calls) {
		this.calls = calls;
	}

	public List<Call> getCalls() {
		return this.calls;
	}

	public AnnotatedSentence getSentence() {
		return this.sentence;
	}

	public void setSentence(AnnotatedSentence sentence) {
		this.sentence = sentence;
	}

	public MResponse getResponse() {
		return response;
	}

	public void setResponse(MResponse response) {
		this.response = response;
	}
}
