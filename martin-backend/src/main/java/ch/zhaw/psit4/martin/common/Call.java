package ch.zhaw.psit4.martin.common;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.models.*;

/**
* This class holds information about a Plugin feature call. It holds the
* plugin and feature name along with the required and optional arguments.
*
* @version 0.1
**/
public class Call {
	private MPlugin plugin;
	private MFunction function;
	private Map<String, IBaseType> arguments;
	
	public Call(){
		this.arguments = new HashMap<>();
	}

	public MPlugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(MPlugin plugin) {
		this.plugin = plugin;
	}

	public MFunction getFunction() {
		return this.function;
	}

	public void setFunction(MFunction feature) {
		this.function = feature;
	}
	
	/**
	 * Adds an argument. 
	 * @param key the argument name
	 * @param value the argument content packed into a IMartinType
	 */
	public void addArgument(String key, IBaseType value){
		this.arguments.put(key, value);
	}
	
	public void setParameters(Map<String, IBaseType> parameters){
		this.arguments = parameters;
	}
	
	/**
	 * @return A map of all arguments.
	 */
	public Map<String, IBaseType> getArguments(){
		return arguments;
	}

}
