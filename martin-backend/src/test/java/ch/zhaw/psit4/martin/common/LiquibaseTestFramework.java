package ch.zhaw.psit4.martin.common;

import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.logging.LogLevel;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * This class is used for Unit-Testing. It sets up a database based on a liquibase
 * changeset.
 */
public class LiquibaseTestFramework {
	private DataSource dataSource;
	private Liquibase liquibase;
	private Connection connection;
	
	@PersistenceContext
    private EntityManager entityManager;

	/**
	 * Initializes a database with a given Liquibase changeset.
	 * @param changeLogFile
	 * @throws Exception
	 */
	public void createDatabase(String changeset){
		try {
			connection = dataSource.getConnection();
			liquibase = new Liquibase(changeset, new ClassLoaderResourceAccessor(), new HsqlConnection(connection));
			liquibase.getLog().setLogLevel(LogLevel.WARNING);
			liquibase.dropAll();
			liquibase.update("");
		
			connection.close();
			
			// Flush EntityManager Cache
			entityManager.getEntityManagerFactory().getCache().evictAll();
			entityManager.clear();
		} catch(Exception e){
			System.err.println(e.getMessage());
		}
		
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
