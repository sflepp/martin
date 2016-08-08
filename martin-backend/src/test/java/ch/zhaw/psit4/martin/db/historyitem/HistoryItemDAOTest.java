package ch.zhaw.psit4.martin.db.historyitem;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.zhaw.psit4.martin.api.types.output.MOutputType;
import ch.zhaw.psit4.martin.common.LiquibaseTestFramework;
import ch.zhaw.psit4.martin.models.MHistoryItem;
import ch.zhaw.psit4.martin.models.MRequest;
import ch.zhaw.psit4.martin.models.MResponse;
import ch.zhaw.psit4.martin.models.repositories.MHistoryItemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:Beans.xml", "classpath:Beans-unit-tests.xml" })
public class HistoryItemDAOTest {

	/**
	 * Is used to setup the unit-test environment (setup in-memory-database and
	 * import database schema).
	 */
	@Autowired
	private LiquibaseTestFramework liquibase;

	/**
	 * The class to test.
	 */
	@Autowired
	private MHistoryItemRepository historyItemRepository;

	@Before
	public void setUp() {
		liquibase.createDatabase("database/unit-tests/HistoryTest/db.changeset-test.xml");
	}

	@Test
	@Transactional
	public void aHistoryItemCanBeSavedInDB() throws Exception {

		MRequest request = new MRequest("test", false);
		MResponse response = new MResponse();
		response.addResponse(MOutputType.TEXT, "test");
		
		MHistoryItem historyItem = new MHistoryItem(request, response);
		this.historyItemRepository.save(historyItem);

		assertEquals(3, this.historyItemRepository.findAll().size());
	}

	@Test
	@Transactional
	public void canGetAListOfAllHistoryItems() throws Exception {
		List<MHistoryItem> historyItems = this.historyItemRepository.findAll();
		
		
		assertEquals(historyItems.size(), 2);
	}
}
