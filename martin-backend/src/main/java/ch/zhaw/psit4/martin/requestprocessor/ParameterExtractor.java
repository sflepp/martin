package ch.zhaw.psit4.martin.requestprocessor;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.models.MParameter;

public class ParameterExtractor {

	public static Phrase extractParameter(MParameter parameter, AnnotatedSentence sentence) {

		if (sentenceHasMoreParameterValues(sentence, parameter)) {
			return extractFromSentence(parameter, sentence);
		} else {
			return extractFromKnowledge(parameter);
		}
	}

	private static Phrase extractFromSentence(MParameter parameter, AnnotatedSentence sentence) {
		return sentence.popPhraseOfType(EBaseType.fromClassName(parameter.getType()));
	}

	
	private static Phrase extractFromKnowledge(MParameter parameter) {
		// Some default-values based on History or knowledge of MArtin,
		// etc.
		// At the moment fix.
		switch (EBaseType.fromClassName(parameter.getType())) {
		case LOCATION:
			Phrase phrase = new Phrase("Zürich");
			phrase.setType(EBaseType.LOCATION);
			return phrase;
		default:
			return null;
		}
	}
	
	public static boolean hasMoreParameterValues(AnnotatedSentence sentence, MParameter parameter){
		return extractFromKnowledge(parameter) != null || sentenceHasMoreParameterValues(sentence, parameter);
	}

	private static boolean sentenceHasMoreParameterValues(AnnotatedSentence sentence, MParameter parameter) {
		return sentence.getPhrasesOfTypeFromPopState(EBaseType.fromClassName(parameter.getType())).size() > 0;
	}
}
