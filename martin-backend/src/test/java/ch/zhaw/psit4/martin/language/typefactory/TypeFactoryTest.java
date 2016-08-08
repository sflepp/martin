package ch.zhaw.psit4.martin.language.typefactory;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.IBaseType;
import ch.zhaw.psit4.martin.api.types.MDuration;
import ch.zhaw.psit4.martin.api.types.MLocation;
import ch.zhaw.psit4.martin.api.types.MNumber;
import ch.zhaw.psit4.martin.api.types.MSet;
import ch.zhaw.psit4.martin.api.types.MTimestamp;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import edu.stanford.nlp.pipeline.AnnotationPipeline;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class TypeFactoryTest {
	@Autowired
	private AnnotationPipeline annotationPipeline;
	
	private static final Log LOG = LogFactory.getLog(TypeFactoryTest.class);
	

	@Test
	public void testNumber() {

		try {
			Phrase phrase = new Phrase("1");
			phrase.setType(EBaseType.NUMBER);
			MNumber one = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(one.getIntegerNumber().get(), (Integer) 1);
			assertEquals(one.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(one.getRawFormat(), MNumber.RawFormat.NUMERIC);

			phrase = new Phrase("one");
			phrase.setType(EBaseType.NUMBER);
			MNumber oneWord = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(oneWord.getIntegerNumber().get(), (Integer) 1);
			assertEquals(oneWord.getDoubleNumber().get(), (Double) 1.0);
			assertEquals(oneWord.getRawFormat(), MNumber.RawFormat.WORD_EN);

			phrase = new Phrase("two thousand");
			phrase.setType(EBaseType.NUMBER);
			MNumber twoThousand = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(twoThousand.getIntegerNumber().get(), (Integer) 2000);
			assertEquals(twoThousand.getDoubleNumber().get(), (Double) 2000.0);
			assertEquals(twoThousand.getRawFormat(), MNumber.RawFormat.WORD_EN);

			phrase = new Phrase("eleven");
			phrase.setType(EBaseType.NUMBER);
			MNumber eleven = (MNumber) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			assertEquals(eleven.getIntegerNumber().get(), (Integer) 11);
			assertEquals(eleven.getDoubleNumber().get(), (Double) 11.0);
			assertEquals(eleven.getRawFormat(), MNumber.RawFormat.WORD_EN);

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}
	}


	@Test
	public void testLocation() {
		// Skip test if internet connection is not available
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress("google.com", 80), 100);
		} catch (IOException e) {
			return;
		}

		try {
			Phrase phrase = new Phrase("Zürich");
			phrase.setType(EBaseType.LOCATION);
			MLocation zurich = (MLocation) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());

			assertEquals(zurich.getFormattedAddress().get(), "Zürich, Switzerland");
			assertEquals(zurich.getLatitude().get(), (Double) 47.3768866);
			assertEquals(zurich.getLongitude().get(), (Double) 8.541694);

			
			phrase = new Phrase("Honolulu");
			phrase.setType(EBaseType.LOCATION);
			MLocation honolulu = (MLocation) BaseTypeFactory.fromPhrase(phrase, new AnnotatedSentence());
			
			assertEquals(honolulu.getFormattedAddress().get(), "Honolulu, HI, USA");
			assertEquals(honolulu.getLatitude().get(), (Double)21.3069444);
			assertEquals(honolulu.getLongitude().get(), (Double)(-157.8583333));

		} catch (Exception e) {
			fail("Expected no exception, but got: " + e.getMessage());
		}

	}
	
	@Test
	public void testTimeExpressions(){
		// http://www.timeml.org/tempeval2/tempeval2-trial/guidelines/timex3guidelines-072009.pdf
		String[] sentences = {
				"about 2 seconds ago",
				"about 2 minutes ago",
				"about 2 hours ago",
				"about 2 months ago",
				"about 2 decades ago",
				"about 2 milliseconds ago",
				"What's the weather tomorrow 2pm?",
				"the second of December.",
				"summer of 1965",
				"ten of two",
				"ten minutes to three",
				"half past noon",
				"eleven in the morning",
				"I’m leaving on vacation two weeks from next Tuesday",
				"John left 2 days before yesterday.",
				"I tutored an English student some Thursdays in 1998.",
				"The concert is at 8:00 p.m. on Friday.",
				"The concert is Friday at 8:00 p.m.",
				// Date
				"Mr. Smith left Friday, October 1, 1999",
				"Mr. Smith left the second of December",
				"Mr. Smith left yesterday",
				"Mr. Smith left in October of 1963",
				"Mr. Smith left in the summer of 1964",
				"Mr. Smith left on Tuesday 18th",
				"Mr. Smith left in November 1943",
				"Mr. Smith left this year’s summer",
				"Mr. Smith left two weeks from next Tuesday",
				"Mr. Smith left last week",
				// Time
				"Mr. Smith left ten minutes to three",
				"Mr. Smith left at five to eight",
				"Mr. Smith left at twenty after twelve",
				"Mr. Smith left at half past noon",
				"Mr. Smith left at eleven in the morning",
				"Mr. Smith left at 9 a.m. Friday, October 1, 1999",
				"Mr. Smith left the morning of January 31",
				"Mr. Smith left late last night",
				"Mr. Smith left last night",
				// Duration
				"Mr. Smith stayed 2 months in Boston",
				"Mr. Smith stayed 48 hours in Boston",
				"Mr. Smith stayed three weeks in Boston",
				"Mr. Smith stayed all last night in Boston",
				// Set
				"John swims twice a week",
				"John swims every 2 days",
				"John swims every second hour",
				// Points
				"more than a decade ago",
				"less than a year ago",
				"no less than a year ago",
				"no more than a year ago",
				// Durations
				"less than 2 hours long",
				"more than 5 minutes",
				"no more than 10 days",
				"at least 10 days",
				// Points and Durations
				"the middle of the month, mid-February",
				"the end of the year",
				"about three years ago"
				
		};
		
		for(int i = 0; i < sentences.length; i++){
			LOG.info(sentences[i]);
			AnnotatedSentence sentence = new AnnotatedSentence(sentences[i], annotationPipeline);
			
			for(Phrase phrase : sentence.getPhrases()){
				try {
					
					IBaseType baseType = BaseTypeFactory.fromPhrase(phrase, sentence);
					
					if(phrase.getType().equals(EBaseType.TIMESTAMP)){
						MTimestamp timestamp = (MTimestamp)baseType;
						LOG.info("   -> " + phrase.getNerTag() + " | " + timestamp.getInstant().toDate() + " | " + phrase.getValue());
						assertNotNull(timestamp.getTimestamp());
					} else if(phrase.getType().equals(EBaseType.DURATION)){
						MDuration duration = (MDuration)baseType;
						LOG.info("   -> " + phrase.getNerTag() + " | " + duration.getDuration().getMillis() + "ms | " + phrase.getValue());
						assertNotNull(duration.getMilliseconds());
					} else if(phrase.getType().equals(EBaseType.SET)) {
						MSet set = (MSet)baseType;
						LOG.info("   -> " + phrase.getNerTag() + " | " + set.getDuration().getMillis() + " ms | " + phrase.getValue());
						//assertNotNull()
					}
				} catch(Exception e){
					LOG.error(e);
				}
				
			}
			
		}
	}

}
