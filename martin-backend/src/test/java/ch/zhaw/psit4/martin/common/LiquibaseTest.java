package ch.zhaw.psit4.martin.common;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This class Tests if the Database schema is correctly setup and example-data
 * is loaded. Schema-Version: 1.0 Data-Version: none
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Beans.xml", "classpath:Beans-unit-tests.xml"})
public class LiquibaseTest {
	/**
	 * Is used to setup the unit-test environment (setup in-memory-database and import database schema).
	 */
	@Autowired
	private LiquibaseTestFramework liquibase;

	/**
	 * The class to test.
	 */
	@Autowired
	private DriverManagerDataSource datasource;

	@Before
	public void setUp() {
		liquibase.createDatabase("database/unit-tests/LiquiBaseTest/db.changeset-test.xml");
	}

	/**
	 * Tests, if the database schema is correct.
	 */
	@Test
	public void testDatabaseSchema() {
		ArrayList<String> tables = new ArrayList<String>();
		try {
			PreparedStatement statement = datasource.getConnection()
					.prepareStatement("SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA()");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				tables.add(result.getString("table_name"));
			}

			assertTrue(tables.contains("AUTHOR"));
			assertTrue(tables.contains("EXAMPLE_CALL"));
			assertTrue(tables.contains("FUNCTION"));
			assertTrue(tables.contains("FUNCTION_HAS_KEYWORD"));
			assertTrue(tables.contains("HISTORY_ITEM"));
			assertTrue(tables.contains("KEYWORD"));
			assertTrue(tables.contains("PARAMETER"));
			assertTrue(tables.contains("PARAMETER_HAS_KEYWORD"));
			assertTrue(tables.contains("PLUGIN"));
			assertTrue(tables.contains("REQUEST"));
			assertTrue(tables.contains("RESPONSE"));

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests, if the datasets inserted into the database are correct
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testDatabaseContent() {
		try {
			PreparedStatement statement = datasource.getConnection()
					.prepareStatement("SELECT name,email FROM author WHERE id = 1");
			ResultSet result = statement.executeQuery();
			result.next();
			String name = result.getString("name");
			String email = result.getString("email");

			assertEquals(name, "Muster Max");
			assertEquals(email, "mmuster@students.zhaw.ch");

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
