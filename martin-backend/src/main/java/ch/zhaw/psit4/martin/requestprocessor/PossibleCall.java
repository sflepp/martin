package ch.zhaw.psit4.martin.requestprocessor;

import java.util.HashMap;
import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.models.*;
/**
 * This class is used to prepare possible calls. The state of the PossibleCall
 * may not be complete and will be changed over the analyzation of the code. A
 * PossibleCall may be thrown away if not all criteria for a valid Call is being
 * met.
 * 
 * @author simonflepp
 *
 */
class PossibleCall {
	private MPlugin plugin;
	private MFunction function;
	private HashMap<String, IBaseType> parameters = new HashMap<>();
	private HashMap<Integer, MKeyword> matchingKeywords = new HashMap<>();

	/**
	 * Is used to determine how important the keyword-count for a usable result
	 * is.
	 */
	private static final Integer RELEVANCE_WEIGHT_KEYWORD_COUNT = 10;
	// TODO: Add more weights to get better results

	public PossibleCall(MPlugin plugin, MFunction function) {
		this.plugin = plugin;
		this.function = function;
	}

	/**
	 * @return Returns a number which can be compared with other
	 *         PossibleRequests to determine the best result
	 */
	public Integer getRelevance() {
		return RELEVANCE_WEIGHT_KEYWORD_COUNT * this.matchingKeywords.size();
	}

	public void setPlugin(MPlugin plugin) {
		this.plugin = plugin;
	}

	public MPlugin getPlugin() {
		return plugin;
	}

	public void setFunction(MFunction function) {
		this.function = function;
	}

	public MFunction getFunction() {
		return function;
	}

	public void addMatchingKeyword(MKeyword keyword) {
		matchingKeywords.put(keyword.getId(), keyword);
	}

	public Map<Integer, MKeyword> getMatchingKeywords() {
		return matchingKeywords;
	}

	public void addParameter(String key, IBaseType value) {
		parameters.put(key, value);
	}

	public Map<String, IBaseType> getParameters() {
		return parameters;
	}
}
