package ch.zhaw.psit4.martin.api.language.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import ch.zhaw.psit4.martin.api.types.EBaseType;

public class Sentence implements ISentence{
	
	public Sentence(String sentence){
		this.text = sentence;
	}
	
	protected String text;
	
	protected List<Phrase> phrases = new ArrayList<>();

	@Override
	public String getText() {
		return text;
	}

	@Override
	public List<Phrase> getPhrases() {
		return phrases;
	}

	/**
	 * Gets all phrases with a chosen IMartionType
	 * 
	 * @param IBaseType
	 *            full IMartinType classname as String (with package)
	 * @return a list of chosen phrases
	 */
	@Override
	public List<Phrase> getPhrasesOfType(EBaseType baseType) {
		return phrases.stream().filter(o -> o.getType().equals(baseType)).collect(Collectors.<Phrase> toList());
	}

	@Override
	public List<String> getWords() {
		return new ArrayList<>(
				Arrays.asList(text.replaceAll("[^a-zA-Z0-9- äöüÄÖÜ]", "").split(" ")));
	}
	
	@Override
	public String toString(){
		return text;
	}

	@Override
	public String toJson() {
		JSONObject json = new JSONObject();
		json.put("text",text);
		return json.toString(4);
	}

}
