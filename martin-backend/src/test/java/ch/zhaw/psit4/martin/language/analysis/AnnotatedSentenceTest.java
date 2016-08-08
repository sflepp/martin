package ch.zhaw.psit4.martin.language.analysis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import edu.stanford.nlp.pipeline.AnnotationPipeline;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class AnnotatedSentenceTest {
	@Autowired
	private AnnotationPipeline stanfordNLP;

	@Test
	public void testPersonSimple() {
		AnnotatedSentence sentence = new AnnotatedSentence("Hello, my name is Chuck Norris.", stanfordNLP);

		Phrase phrase = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase.getType(), EBaseType.PERSON);
		assertEquals(phrase.getValue(), "Chuck Norris");
	}

	@Test
	public void testPersonMultiple() {
		AnnotatedSentence sentence = new AnnotatedSentence("Today I met Donald Trump hanging out with Barack Obama and Putin. They were laughing alot.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase1.getType(), EBaseType.PERSON);
		assertEquals(phrase1.getValue(), "Donald Trump");
		
		
		Phrase phrase2 = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase2.getType(), EBaseType.PERSON);
		assertEquals(phrase2.getValue(), "Barack Obama");
		
		Phrase phrase3 = sentence.popPhraseOfType(EBaseType.PERSON);

		assertEquals(phrase3.getType(), EBaseType.PERSON);
		assertEquals(phrase3.getValue(), "Putin");
	}
	
	@Test
	public void testDuration() {
		AnnotatedSentence sentence = new AnnotatedSentence("I was waiting for at least two weeks for my new iPhone.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.DURATION);

		assertEquals(phrase1.getType(), EBaseType.DURATION);
		assertEquals(phrase1.getValue(), "at least two weeks");
		
	}
	
	@Test
	public void testMoney() {
		AnnotatedSentence sentence = new AnnotatedSentence("My car was on sale and I got a bounty of 100 dollar.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.MONEY);

		assertEquals(phrase1.getType(), EBaseType.MONEY);
		assertEquals(phrase1.getValue(), "100 dollar");
		
	}
	
	@Test
	public void testLocation() {
		AnnotatedSentence sentence = new AnnotatedSentence("Yesterday I wanted to fly to Dubai.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.LOCATION);

		assertEquals(phrase1.getType(), EBaseType.LOCATION);
		assertEquals(phrase1.getValue(), "Dubai");
		
	}
	
	@Test
	public void testNumber() {
		AnnotatedSentence sentence = new AnnotatedSentence("Thank you thousand times!", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.NUMBER);

		assertEquals(phrase1.getType(), EBaseType.NUMBER);
		assertEquals(phrase1.getValue(), "thousand");
		
	}
	
	@Test
	public void testOrdinal() {
		AnnotatedSentence sentence = new AnnotatedSentence("I will be the first in this race!", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.ORDINAL);

		assertEquals(phrase1.getType(), EBaseType.ORDINAL);
		assertEquals(phrase1.getValue(), "first");
		
	}
	
	@Test
	public void testOrganization() {
		AnnotatedSentence sentence = new AnnotatedSentence("Facebook will take over the world if we don't stop them.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.ORGANIZATION);

		assertEquals(phrase1.getType(), EBaseType.ORGANIZATION);
		assertEquals(phrase1.getValue(), "Facebook");
		
	}
	
	@Test
	public void testPercent() {
		AnnotatedSentence sentence = new AnnotatedSentence("If the glass is half empty, it is only 50 percent full.", stanfordNLP);

		Phrase phrase1 = sentence.popPhraseOfType(EBaseType.PERCENT);

		assertEquals(phrase1.getType(), EBaseType.PERCENT);
		assertEquals(phrase1.getValue(), "50 percent");
		
	}

}
