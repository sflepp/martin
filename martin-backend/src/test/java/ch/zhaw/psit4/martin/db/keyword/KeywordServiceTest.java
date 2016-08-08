package ch.zhaw.psit4.martin.db.keyword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.models.MKeyword;
import ch.zhaw.psit4.martin.models.repositories.MKeywordRepository;

import static org.junit.Assert.*;

import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class KeywordServiceTest {
	
	@Autowired
	private LiquibaseTestFramework liquibase;

	@Autowired
    private MKeywordRepository keywordService;
    private Log log;

    @Before
    public void setUp(){
    	liquibase.createDatabase("database/unit-tests/keywordTest/db.keywordUnitTest-1.0.xml");
        log = LogFactory.getLog(KeywordServiceTest.class);        
    }
    
    @Test
    public void testListKeywords() throws Exception {
        List<MKeyword> keywords = keywordService.findAll();
        keywords.stream().forEach(keyword -> log.info(keyword.getId()+", "+keyword.getKeyword()));
        assertEquals(false,keywords.isEmpty());
        assertEquals(6,keywords.size());
    }
    
    @Test
    public void testAddKeyword() throws Exception {
        MKeyword newWord = new MKeyword("Hallo Welt");
        keywordService.save(newWord);
        String word = keywordService.findOne(7).getKeyword();
        assertEquals("Hallo Welt", word);
    }


    @Test
    public void testGetKeywordById() throws Exception {
        assertEquals("weather", keywordService.findOne(1).getKeyword());
        assertEquals(1, keywordService.findOne(1).getId());
        assertEquals("Sunday", keywordService.findOne(6).getKeyword());
        
        assertEquals(null, keywordService.findOne(10));
    }

    @Test
    public void testUpdateKeyword() throws Exception {
        MKeyword toChange = keywordService.findOne(3);
        toChange.setKeyword("there");
        keywordService.save(toChange);
        assertEquals("there", keywordService.findOne(3).getKeyword());
    }

    @Test
    public void testRemoveKeyword() throws Exception {
        //remove 2 & 6
        keywordService.delete(2);
        keywordService.delete(6);
        
        
        //check
        List<MKeyword> keywords = keywordService.findAll();
        keywords.stream().forEach(keyword -> log.info(keyword.getId()+", "+keyword.getKeyword()));
        assertEquals(4, keywords.size());
        assertEquals(null, keywordService.findOne(6));
        
    }
}
