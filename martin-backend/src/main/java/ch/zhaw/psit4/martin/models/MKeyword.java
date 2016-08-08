package ch.zhaw.psit4.martin.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.zhaw.psit4.martin.common.MartinHelper;

/**
 * Contains a Keyword for a Plugin. Either Funtion or Parameter. The class is
 * used to store a retreived keywords of a plugin.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "keyword")
public class MKeyword extends BaseModel {

	private String keyword;

	// mapped by set in Parameter.java
	@JsonIgnore
	@ManyToMany(mappedBy = "parameterKeywords")
	private Set<MParameter> parameter;

	@JsonIgnore
	@ManyToMany(mappedBy = "keywords")
	private Set<MFunction> functions;

	public MKeyword() {
	}

	public MKeyword(String keyword) {
		this.setKeyword(keyword);
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public Set<MParameter> getParameter() {
		return parameter;
	}

	public void setParameter(Set<MParameter> parameter) {
		this.parameter = parameter;
	}

	@JsonIgnore
	public Set<MParameter> getParentParameter() {
		return this.parameter;
	}

	public Set<MFunction> getFunctions() {
		return functions;
	}

	public void setFunctions(Set<MFunction> functions) {
		this.functions = functions;
	}

	/**
	 * Only call it if the parameter knows the keyword 
	 * @param parameter
	 */
    public void addParameter(MParameter parameter) {
        //Mapped by Parameter
        this.parameter =MartinHelper.initSetifNull(this.parameter);
       this.parameter.add(parameter);
    }

	/**
	 * Only call it if the function knows the keyword 
	 * @param function
	 */
    public void addFunction(MFunction function) {
        this.functions =MartinHelper.initSetifNull(this.functions);
        this.functions.add(function);
    }

}
