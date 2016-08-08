package ch.zhaw.psit4.martin.requestprocessor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.models.MRequest;
import ch.zhaw.psit4.martin.models.MResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class RequestProcessorTest {
	@Autowired
	private RequestProcessor requestProcessor;

	@Autowired
	private LiquibaseTestFramework liquibase;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		liquibase.createDatabase("database/unit-tests/RequestProcessorTest/db.RequestProcessorTest-1.0.xml");
	}

	@Test
	public void testMultipleParameterOfSameType() {
		MResponse response1 = new MResponse();
		MRequest request1 = new MRequest("Hello Martin, today I met Chuck Norris and and he's such an awsome guy!",
				false);
		ExtendedRequest extRequest1 = requestProcessor.extend(request1, response1);
		assertEquals(extRequest1.getCalls().isEmpty(), false);
		assertEquals(extRequest1.getCalls().get(0).getPlugin().getName(), "HelloPlugin");
		assertEquals(extRequest1.getCalls().get(0).getFunction().getName(), "greeting");
		assertEquals(extRequest1.getCalls().get(0).getArguments().values().size(), 2);
	}

	@Test
	public void testExtendRequestPluginAndFeature() {
		MResponse response0 = new MResponse();
		MRequest request0 = new MRequest("What's the weather in Zürich tomorrow?", false);
		ExtendedRequest extRequest0 = requestProcessor.extend(request0, response0);
		assertEquals(extRequest0.getCalls().isEmpty(), false);
		assertEquals(extRequest0.getCalls().get(0).getPlugin().getName(), "WetterPlugin");
		assertEquals(extRequest0.getCalls().get(0).getFunction().getName(), "getWeatherAtLocation");
		assertEquals(extRequest0.getCalls().get(0).getArguments().get("time").toString(), "tomorrow");
		assertEquals(extRequest0.getCalls().get(0).getArguments().get("location").toString(), "Zürich");
	}


	@Test
	public void testUnknownLocation() {
		MResponse response2 = new MResponse();
		MRequest request2 = new MRequest("I'd like to know the weather at the Hugentoblerplatz.", false);
		ExtendedRequest extRequest2 = requestProcessor.extend(request2, response2);
		assertEquals(extRequest2.getCalls().isEmpty(), false);
		assertEquals(extRequest2.getCalls().get(0).getPlugin().getName(), "WetterPlugin");
		assertEquals(extRequest2.getCalls().get(0).getFunction().getName(), "getWeatherAtLocation");
		assertEquals(extRequest2.getCalls().get(0).getArguments().get("location").toString(), "Hugentoblerplatz");
		assertEquals(extRequest2.getCalls().size(), 1);
	}

}
