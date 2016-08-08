package ch.zhaw.psit4.martin.models;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.zhaw.psit4.martin.common.MartinHelper;
import ch.zhaw.psit4.martin.models.MAuthor;
import ch.zhaw.psit4.martin.models.MFunction;

/**
 * Contains a Paramter for a Plugin Function. The class is used to store
 * plugins, their names and options of a function.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "plugin")
public class MPlugin extends BaseModel {

	private String uuid;
	private String name;
	private String description;
	private Date date;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(nullable = false)
	private MAuthor author;

	@JsonIgnore
	@OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MFunction> functions;


	public MPlugin(){}

    public MPlugin(String uuid, String name, String description, Date date) {
        this.name = name;
        this.uuid = uuid;
        this.description = description;
        if(date != null){
            this.date = date;
        }
    }

    public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setFunctions(Set<MFunction> functions) {
		this.functions = functions;
		functions.stream().forEach(f -> f.setPlugin(this));
	}
	
	public void addFunction(MFunction function) {
	    functions = MartinHelper.initSetifNull(functions);
	    functions.add(function);
	    function.setPlugin(this);
	}

	public Set<MFunction> getFunctions() {
		return this.functions;
	}

	public MAuthor getAuthor() {
		return this.author;
	}

	public void setAuthor(MAuthor author) {
		this.author = author;
		//author.addPlugin(this);
	}

}
