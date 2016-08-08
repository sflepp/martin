package ch.zhaw.psit4.martin.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ch.zhaw.psit4.martin.common.MartinHelper;
import ch.zhaw.psit4.martin.models.MKeyword;
import ch.zhaw.psit4.martin.models.MParameter;
import ch.zhaw.psit4.martin.models.MPlugin;

/**
 * Contains a Paramter for a Plugin Function. The class is used to store
 * Functions, their names and options of a function.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "function")
public class MFunction extends BaseModel {

	private String name;
	private String description;

	@OneToMany(mappedBy = "function", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MParameter> parameters;

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "function_has_keyword", joinColumns = {
			@JoinColumn(name = "function_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "keyword_id", nullable = false, updatable = false) })
	private Set<MKeyword> keywords;
	
	@OneToMany(mappedBy = "function",cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<MExampleCall> exampleCalls;

	@ManyToOne
	@JoinColumn(nullable = false)
	private MPlugin plugin;

	public MFunction() {
	}

	public MFunction(String name, String description) {
	    this.name = name;
	    this.description = description;
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

	public void setParameter(Set<MParameter> parameter) {
		parameter.stream().forEach(p -> p.setFunction(this));
		this.parameters = parameter;
	}

	public Set<MParameter> getParameters() {
		return this.parameters;
	}

	public Set<MKeyword> getKeywords() {
		return this.keywords;
	}

	public void setPlugin(MPlugin plugin) {
		this.plugin = plugin;
	}

	public MPlugin getPlugin() {
		return this.plugin;
	}

	public void addParameters(Set<MParameter> parameter) {
		// needs to be set, otherwise it can not be persisted
		parameter.stream().forEach(p -> p.setFunction(this));
		this.parameters.addAll(parameter);
	}

    public void addKeyword(MKeyword keyword) {
        keywords = MartinHelper.initSetifNull(keywords);
        keywords.add(keyword);
        keyword.addFunction(this);
        
    }

    public void addParameter(MParameter parameter) {
	    parameters = MartinHelper.initSetifNull(parameters);
	    parameters.add(parameter);
	    parameter.setFunction(this);
    }

    public void addExampleCall(MExampleCall exampleCall) {
	    exampleCalls = MartinHelper.initSetifNull(exampleCalls);
	    exampleCalls.add(exampleCall);
	    exampleCall.setFunction(this);
        
    }

}
