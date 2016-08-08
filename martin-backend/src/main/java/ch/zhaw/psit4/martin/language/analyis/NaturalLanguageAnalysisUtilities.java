package ch.zhaw.psit4.martin.language.analyis;

import java.util.Properties;

import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;
import edu.stanford.nlp.time.TimeAnnotator;

/**
 * This Class holds some Utilities for analyzing natural language to help
 * getting the best result for a user request.
 *
 */
public class NaturalLanguageAnalysisUtilities {
	public static final String CORENLP_SERVER = "corenlp.run";
	public static final Integer CORENLP_PORT = 80;
	public static final Integer CORENLP_MAX_THREADS = 1;
	

	/**
	 * Returns an Instance of StanfordCoreNLP. StanfordCoreNLP can be used to
	 * analyze natural language in an advanced way.
	 * 
	 * Source: http://stanfordnlp.github.io Try-Out: http://corenlp.run
	 * 
	 * @return A usable Stanford NLP pipline.
	 */
	public AnnotationPipeline bootStanfordNLP() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		AnnotationPipeline pipeline = new StanfordCoreNLP(props);
		pipeline.addAnnotator(new TimeAnnotator("sutime", props));
		
		return pipeline;
	}
	
	/**
	 * Returns an Instance of StanfordCoreNLPClient. StanfordCoreNLP can be used to
	 * analyze natural language in an advanced way.
	 * 
	 * The client is a leightweight version of CoreNLP, which connects to corenlp.run for 
	 * fetching analyzing text.
	 * 
	 * Source: http://stanfordnlp.github.io Try-Out: http://corenlp.run
	 * 
	 * @return A usable Stanford NLP pipline.
	 */
	public AnnotationPipeline bootStanfordNLPClient() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		return new StanfordCoreNLPClient(props, CORENLP_SERVER, CORENLP_PORT, CORENLP_MAX_THREADS);

	}

}
