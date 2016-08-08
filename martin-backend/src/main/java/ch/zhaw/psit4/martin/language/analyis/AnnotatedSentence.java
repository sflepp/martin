package ch.zhaw.psit4.martin.language.analyis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.zhaw.psit4.martin.api.language.parts.ISentence;
import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.language.parts.Sentence;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.models.MKeyword;
import ch.zhaw.psit4.martin.timing.TimingInfoLogger;
import ch.zhaw.psit4.martin.timing.TimingInfoLoggerFactory;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NormalizedNamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.trees.UniversalEnglishGrammaticalRelations;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;

/**
 * This class represents a sequence of words capable of standing alone to make
 * an assertion, ask a question, or give a command, usually consisting of a
 * subject and a predicate containing a finite verb
 *
 */

public class AnnotatedSentence extends Sentence implements ISentence {
	private static final TimingInfoLogger TIMING_LOG = TimingInfoLoggerFactory.getInstance();
	private static final Log LOG = LogFactory.getLog(AnnotatedSentence.class);

	private AnnotationPipeline annotationPipeline;

	private Annotation annotation;

	private List<Phrase> phrasesPopState;
	boolean popStateDirty;
	private List<SemanticGraph> semanticGraphs = new ArrayList<>();

	private List<MOutput> predefinedAnswer = new ArrayList<>();

	public AnnotatedSentence() {
		super(null);
	}

	public AnnotatedSentence(String sentence, AnnotationPipeline annotationPipeline) {
		super(sentence);

		TIMING_LOG.logStart("Text analyzation");
		this.annotationPipeline = annotationPipeline;

		if (!"".equals(sentence)) {
			this.annotate();
			this.generatePhrasesAndSemanticGraph();
			this.resetPopState();
		}

		predefinedAnswer = PredefinedAnswerGenerator.generadePredefinedAnswer(this);

		TIMING_LOG.logEnd("Text analyzation");
	}

	public void annotate() {
		annotation = new Annotation(text);

		// Set time reference for all Time-Annotations
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now = Calendar.getInstance();

		annotation.set(CoreAnnotations.DocDateAnnotation.class, dateFormat.format(now.getTime()));
		annotationPipeline.annotate(annotation);
	}

	/**
	 * Recognizes named (PERSON, LOCATION, ORGANIZATION, MISC), numerical
	 * (MONEY, NUMBER, ORDINAL, PERCENT), and temporal (DATE, TIME, DURATION,
	 * SET) entities. Named entities are recognized using a combination of three
	 * CRF sequence taggers trained on various corpora, such as ACE and MUC.
	 * Numerical entities are recognized using a rule-based system. Numerical
	 * entities that require normalization, e.g., dates, are normalized to
	 * NormalizedNamedEntityTagAnnotation.
	 */
	public void generatePhrasesAndSemanticGraph() {
		List<CoreMap> sentences = annotation.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			generateSentencePhrases(sentence.get(TokensAnnotation.class));
			generateTimestampPhrases(sentence.get(TimeAnnotations.TimexAnnotations.class));
			SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
			semanticGraphs.add(dependencies);
			LOG.info("SemanticGraph: " + dependencies.toCompactString());
		}
	}

	public void generateSentencePhrases(List<CoreLabel> tokens) {
		StringBuilder sb = new StringBuilder();
		CoreLabel previousToken;
		CoreLabel currentToken = tokens.get(0);
		List<EBaseType> skip = Arrays.asList(EBaseType.TIMESTAMP, EBaseType.SET, EBaseType.DURATION, EBaseType.UNKNOWN);

		for (CoreLabel token : tokens) {
			previousToken = currentToken;
			currentToken = token;

			if (previousToken.get(NamedEntityTagAnnotation.class).equals(token.get(NamedEntityTagAnnotation.class))) {
				sb.append(" " + token.get(TextAnnotation.class));
			} else {
				Phrase phrase = new Phrase(sb.toString().trim());
				phrase.setNerTag(previousToken.get(NamedEntityTagAnnotation.class));
				phrase.setNormalizedValue(previousToken.get(NormalizedNamedEntityTagAnnotation.class));

				if (!skip.contains(phrase.getType())) {
					phrases.add(phrase);
					LOG.info("NER-Tag found: " + phrase.toString());
				}

				sb.setLength(0);
				sb.append(token.get(TextAnnotation.class));
			}
		}

		Phrase phrase = new Phrase(sb.toString().trim());
		phrase.setNerTag(currentToken.get(NamedEntityTagAnnotation.class));
		phrase.setNormalizedValue(currentToken.get(NormalizedNamedEntityTagAnnotation.class));

		if (!skip.contains(phrase.getType())) {
			phrases.add(phrase);
			LOG.info("NER-Tag found: " + phrase.toString());
		}

	}
	
	public void generateNominalModifierPhrases(){
		
	}
	
	/**
	 * Build a String with the Nominal Nominal Modifiers of the keywords in the
	 * sentence Example: the sentence "show me a picture of a dog in a hause"
	 * returns: "dog hause"
	 * 
	 * @param sentence
	 * @param matchingKeywords
	 * @return
	 */
	public void generateNominalModifierPhrases(Collection<MKeyword> matchingKeywords) {

		String parameterAsString = "";

		// Working only with graph of first text sentence
		if (getSemanticGraphs().isEmpty() || getSemanticGraphs().get(0) == null) {
			return;
		}
		SemanticGraph dependencies = getSemanticGraphs().get(0);
		for (MKeyword keyword : matchingKeywords) {

			// Working only with first occurrence of the keyword
			List<IndexedWord> indexKeywordList = dependencies.getAllNodesByWordPattern(keyword.getKeyword());
			if (indexKeywordList.isEmpty()) {
				return;
			}
			IndexedWord indKeyWord = indexKeywordList.get(0);

			Set<IndexedWord> nominalMods = dependencies.getChildrenWithReln(indKeyWord,
					UniversalEnglishGrammaticalRelations.NOMINAL_MODIFIER);
			for (IndexedWord nominalModifier : nominalMods) {
				parameterAsString = parameterAsString.concat(nominalModifier.value() + " ");
			}
		}
		
		Phrase phrase = new Phrase(parameterAsString.trim());
		phrase.setType(EBaseType.NOMINAL_MODIFIER);
		
		if(!"".equals(parameterAsString)){
			LOG.info("Dependency found: NOMINAL_MODIFIER | " + parameterAsString);
			phrasesPopState.add(phrase);
		}
	}

	public void generateTimestampPhrases(List<CoreMap> tokens) {
		for (CoreMap map : tokens) {
			TimeExpression timeExpression = map.get(TimeExpression.Annotation.class);
			Temporal temporal = timeExpression.getTemporal();

			Phrase phrase = new Phrase(timeExpression.getText());
			phrase.setNormalizedValue(temporal.getTimexValue());
			phrase.setType(EBaseType.fromNLPTag(temporal.getTimexType().toString()));
			phrase.setPayload(temporal);
			phrases.add(phrase);
		}
	}

	public void resetPopState() {
		phrasesPopState = new ArrayList<>(phrases);
		popStateDirty = false;
	}

	public boolean isPopStateDirty() {
		return popStateDirty;
	}

	/**
	 * Removes one element of type IMartinType from the internal list and
	 * returns it.
	 * 
	 * @param type
	 *            full IMartinType classname as String (with package)
	 * @return a phrese with the chosen type
	 */
	public Phrase popPhraseOfType(EBaseType type) {
		popStateDirty = true;

		Optional<Phrase> token = phrasesPopState.stream().filter(o -> o.getType().equals(type)).findFirst();

		if (token.isPresent()) {
			phrasesPopState.remove(phrasesPopState.indexOf(token.get()));
			return token.get();
		} else {
			return null;
		}
	}

	public List<MOutput> getPredefinedAnswer() {
		return predefinedAnswer;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public List<SemanticGraph> getSemanticGraphs() {
		return semanticGraphs;
	}

	public void setSemanticGraphs(List<SemanticGraph> semanticGraphs) {
		this.semanticGraphs = semanticGraphs;
	}

	public AnnotationPipeline getAnnotationPipeline() {
		return this.annotationPipeline;
	}
	
	public List<Phrase> getPhrasesOfTypeFromPopState(EBaseType baseType) {
		return phrasesPopState.stream().filter(o -> o.getType().equals(baseType)).collect(Collectors.<Phrase> toList());
	}

}
