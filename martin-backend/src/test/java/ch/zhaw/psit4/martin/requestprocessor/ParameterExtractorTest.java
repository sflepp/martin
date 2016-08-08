package ch.zhaw.psit4.martin.requestprocessor;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.language.parts.Phrase;
import ch.zhaw.psit4.martin.api.types.EBaseType;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.models.MKeyword;
import ch.zhaw.psit4.martin.models.MParameter;
import edu.stanford.nlp.pipeline.AnnotationPipeline;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class ParameterExtractorTest {
    
    @Autowired
    private AnnotationPipeline annotationPipeline;

    @Before
    public void setUp() throws Exception {}
    
    @Test
    public void extractTextParameterTest(){
        String cmd = "Give me a picture of a dog";
        AnnotatedSentence sentence = new AnnotatedSentence(cmd, annotationPipeline);
        MKeyword k = new MKeyword("picture");
        MParameter p = new MParameter();
        p.addKeyword(k);
        p.setType(EBaseType.NOMINAL_MODIFIER.getValue());
        ArrayList<MKeyword> keywordList = new ArrayList<>();
        keywordList.add(k);
        
        sentence.generateNominalModifierPhrases(keywordList);
        
        Collection<MKeyword> matchingKeywords = new HashSet<MKeyword>();
        matchingKeywords.add(k);
        
        Phrase par = ParameterExtractor.extractParameter(p, sentence);
        assertEquals("dog", par.getValue());
        
    }

}
