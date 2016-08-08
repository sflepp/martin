package ch.zhaw.psit4.martin.language.typefactory;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MSet;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import edu.stanford.nlp.time.SUTime.TemporalSet;

public class MSetFactory {
	
	public static MSet fromPhrase(Phrase phrase, AnnotatedSentence sentence)
			throws BaseTypeInstanciationException {
		TemporalSet temporal = (TemporalSet) phrase.getPayload();
		MSet set = new MSet(phrase.getValue());
		
		set.setDuration(temporal.getPeriod().getJodaTimeDuration());

		return set;
	}

}
