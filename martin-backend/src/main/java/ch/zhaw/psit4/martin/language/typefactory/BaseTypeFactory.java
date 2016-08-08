package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;

public class BaseTypeFactory {
	private static final Log LOG = LogFactory.getLog(BaseTypeFactory.class);
	public static IBaseType fromPhrase(Phrase phrase, AnnotatedSentence sentence) throws BaseTypeInstanciationException {
		
		IBaseType instance;
		
		switch (phrase.getType()) {
		case NUMBER:
			instance = MNumberFactory.fromString(phrase.getValue());
			break;
		case LOCATION:
			instance = MLocationFactory.fromString(phrase.getValue());
			break;
		case TIMESTAMP:
			instance = MTimestampFactory.fromPhrase(phrase, sentence);
			break;
		case DURATION:
			instance = MDurationFactory.fromPhrase(phrase, sentence);
			break;
		case SET:
			instance = MSetFactory.fromPhrase(phrase, sentence);
			break;
		default:
			instance = IBaseType.fromString(phrase.getType(), phrase.getValue());
			break;
		}
		
		LOG.info("Parameter constructed: \n" + instance.toJson());
		
		return instance;
	}

}
