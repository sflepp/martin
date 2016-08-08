package ch.zhaw.psit4.martin.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ch.zhaw.psit4.martin.models.MRequest;
import ch.zhaw.psit4.martin.models.MResponse;

/**
 * A HistoryItem is used to keep track of the Requests sent to Martin and the
 * relative Responses.
 *
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "history_item")
public class MHistoryItem extends BaseModel {

	@NotNull
	private Timestamp date;

	@OneToOne(cascade = CascadeType.ALL)
	private MRequest request;

	@OneToOne(cascade = CascadeType.ALL)
	private MResponse response;

	public MHistoryItem() {
	}

	public MHistoryItem(MRequest request, MResponse response) {
		this.request = request;
		this.response = response;
		this.date = new Timestamp(new Date().getTime());
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public MRequest getRequest() {
		return request;
	}

	public void setRequest(MRequest request) {
		this.request = request;
	}

	public MResponse getResponse() {
		return response;
	}

	public void setResponse(MResponse response) {
		this.response = response;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MHistoryItem)) {
			return false;
		}
		final MHistoryItem item = (MHistoryItem) obj;
		if (this.getId() != item.getId() || !this.getDate().equals(item.getDate())
				|| !this.request.equals(item.getRequest()) || !this.response.equals(item.getResponse())) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return this.getId() + this.getDate().hashCode() * this.getRequest().hashCode() * this.getResponse().hashCode();
	}
}
