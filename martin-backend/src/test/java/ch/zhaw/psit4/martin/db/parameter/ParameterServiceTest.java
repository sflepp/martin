package ch.zhaw.psit4.martin.db.parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.models.MParameter;
import ch.zhaw.psit4.martin.models.repositories.MParameterRepository;

import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class ParameterServiceTest {

	@Autowired
	private LiquibaseTestFramework liquibase;

	@Autowired
    private MParameterRepository parameterService;
    private Log log;

    @Before
    public void setUp(){
    	liquibase.createDatabase("database/unit-tests/parameterTest/db.parameterUnitTest-1.0.xml");
        log = LogFactory.getLog(ParameterServiceTest.class);        
    }

    @Test
    public void testListParameters() throws Exception {
        List<MParameter> parameter = parameterService.findAll();
        parameter.stream().forEach(param -> printParam(param));
        assertEquals(false,parameter.isEmpty());
        assertEquals(4,parameter.size());

    }

    private void printParam(MParameter param) {
        log.info(param.getId()+", "+param.getName()+", "+param.getType());
        log.info("Keywords for "+param.getName());
        param.getKeywords().stream().forEach(keyword -> log.info(keyword.getKeyword()));
    }

    /*
    @Test
    public void testAddParameter() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testGetParameterById() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testUpdateParameter() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testRemoveParameter() throws Exception {
        throw new RuntimeException("not yet implemented");
    }
    */

}
