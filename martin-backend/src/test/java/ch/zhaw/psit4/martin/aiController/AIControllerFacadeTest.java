package ch.zhaw.psit4.martin.aiController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.common.Call;
import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.frontend.FrontendController;
import ch.zhaw.psit4.martin.language.analyis.AnnotatedSentence;
import ch.zhaw.psit4.martin.models.MHistoryItem;
import ch.zhaw.psit4.martin.models.MRequest;
import ch.zhaw.psit4.martin.models.MResponse;
import ch.zhaw.psit4.martin.models.repositories.MHistoryItemRepository;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.requestprocessor.RequestProcessor;
import edu.stanford.nlp.pipeline.AnnotationPipeline;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class AIControllerFacadeTest {

	@Mock
	private MHistoryItemRepository historyItemServiceMock;

	@Mock
	private RequestProcessor requestProcessorMock;

	@Mock
	private IPluginLibrary pluginLibraryMock;

	@Mock
	private FrontendController frontend;
	
	@Mock
	private MOutputQueueThread outputQueue;

	@InjectMocks
	private AIControllerFacade aiController;

	@Autowired
	private LiquibaseTestFramework liquibase;

	@Autowired
	private AnnotationPipeline stanfordNLP;

	MRequest request = null;
	ExtendedRequest extRequest = null;
	MResponse response = null;
	MHistoryItem historyItem = null;
	Call call = null;

	@Before
	public void setUp() throws Exception {
		liquibase.createDatabase("database/db.changeset-schema-latest.xml");
		MockitoAnnotations.initMocks(this);

		request = new MRequest("request test", false);
		response = new MResponse();
		extRequest = new ExtendedRequest(request, response);
		call = new Call();
		extRequest.addCall(call);
		extRequest.setSentence(new AnnotatedSentence("test", stanfordNLP));
		response = new MResponse("response test");
		historyItem = new MHistoryItem(request, response);

		when(requestProcessorMock.extend(any(), any())).thenReturn(extRequest);
		when(pluginLibraryMock.executeRequest(extRequest)).thenReturn(response);

		Mockito.doNothing().when(frontend).sendRequestAndResponseToConnectedClients(Mockito.any());
		Mockito.doNothing().when(outputQueue).addToOutputQueue(Mockito.any());

		ArrayList<MHistoryItem> getHistoryResult = new ArrayList<>();
		getHistoryResult.add(new MHistoryItem(new MRequest("command1", false), new MResponse("response1")));
		getHistoryResult.add(new MHistoryItem(new MRequest("command2", false), new MResponse("response2")));
		getHistoryResult.add(new MHistoryItem(new MRequest("command3", false), new MResponse("response3")));
		when(historyItemServiceMock.findAll()).thenReturn(getHistoryResult);
	}

	@Test
	public void canGetAListOfHistoryItems() {
		List<MHistoryItem> list = aiController.getHistory();
		assertEquals(3, list.size());
		assertEquals("command1", list.get(0).getRequest().getCommand());
	}

	@Test
	public void saveAHistoryItemWhenRequestMakeSense() {
		aiController.elaborateRequest(request);
		// The timestamp must be updated to be the "same" as the generated in
		// the elaborateRequestFunction, otherwise the mock will not be the
		// same as the one generated.
		historyItem.setDate(new Timestamp(new Date().getTime()));
	}
}
