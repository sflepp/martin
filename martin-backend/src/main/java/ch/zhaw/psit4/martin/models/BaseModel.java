package ch.zhaw.psit4.martin.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass 
public abstract class BaseModel {
	@Id
	@Column(unique = true, nullable = false, updatable = false)
	@GeneratedValue
	private int id;

	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	
    private Integer version;

	public int getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
		this.version = 1;
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = new Date();
		if(version == null){
			version = 1;
		} else {
			version++;
		}
	}

}
