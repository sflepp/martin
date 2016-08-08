package ch.zhaw.psit4.martin.models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.zhaw.psit4.martin.models.MPlugin;

/**
 * Contains a Paramter for a Plugin Function. The class is used to store
 * Authors, their names and options of a function.
 * 
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "author")
public class MAuthor extends BaseModel {

	private String name;
	private String email;

	@JsonIgnore
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private Set<MPlugin> plugins;

	public MAuthor() {
	}
	

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public Set<MPlugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(Set<MPlugin> plugins) {
		this.plugins = plugins;
	}

	public void addPlugin(MPlugin plugin) {
		plugins.add(plugin);

	}

}
