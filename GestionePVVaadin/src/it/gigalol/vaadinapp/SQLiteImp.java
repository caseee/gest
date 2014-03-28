/**
 * 
 */
package it.gigalol.vaadinapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.VaadinService;

/**
 * @author Marco
 *
 */
public class SQLiteImp implements SqlInterface {
	private static final String WEBDIR = "WEB-INF";
	private static final String DBNAME = "SQLite.db";
	private static final String INITSQL = "SQLiteInit.sql";
	private static final String HOME_DIR_OPT = "user.home";
	private static final String FILE_SEPARATOR = "file.separator";
	private static final String SQLITE_JDBC_DRIVER_NAME = "org.sqlite.JDBC";
	private static final String SQLITE_JDBC_CONNECTION_STRING_PREAMBLE = "jdbc:sqlite:";
	private static final String SQLITE_USER = "SA";
	private static final String SQLITE_PASS = "";
	private static final int SQLITE_JDBC_START_CONNECTION = 2;
	private static final int SQLITE_JDBC_MAX_CONNECTION = 10;
	
	
	private String separator = System.getProperty(FILE_SEPARATOR);
	private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	
	private JDBCConnectionPool pool;
			
	private SQLContainer ArticlesContainer;
	public SQLContainer getArticlesContainer() {
		return ArticlesContainer;
	}

	
	public SQLiteImp() {
		
		String dbfile = basepath + separator  +WEBDIR+separator+DBNAME;
	
		java.util.logging.Logger.getAnonymousLogger().log(java.util.logging.Level.INFO, "DB LOCATION " + dbfile );
			
		try {

			boolean initdb=true;
			
			if (new File(dbfile).exists()) initdb = false;
			
			SQLiteConfig config = new SQLiteConfig();
			config.setOpenMode(SQLiteOpenMode.READWRITE);
			Class.forName(SQLITE_JDBC_DRIVER_NAME);
			pool = new SimpleJDBCConnectionPool(
					SQLITE_JDBC_DRIVER_NAME,
					SQLITE_JDBC_CONNECTION_STRING_PREAMBLE+dbfile, 
					SQLITE_USER, SQLITE_PASS, 
					SQLITE_JDBC_START_CONNECTION, 
					SQLITE_JDBC_MAX_CONNECTION);
			
			if (pool==null) throw new SQLException();
			
			if (initdb) popolateDB();
											
			TableQuery tq = new TableQuery("ARTICLES", pool);
			tq.setVersionColumn("ID");
			ArticlesContainer = new SQLContainer(tq);
				
		} catch ( java.lang.ClassNotFoundException cnfe ) {
			System.err.println( "ERRORE INIT SQLITE: " + cnfe.getClass().getName() + ": " + cnfe.getMessage() );
			System.exit(1);
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		catch ( Exception e ) 
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(1);
		}
		finally {
			
		}
		java.util.logging.Logger.getAnonymousLogger().log(java.util.logging.Level.INFO, "Database connection created." );
	}

	/* (non-Javadoc)
	 * @see it.gigalol.vaadinapp.SqlInterface#auth(java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean auth(String user, String pass, int levelreq) {
		Connection c;
		boolean result = false;
		try {
			c = pool.reserveConnection();	
			PreparedStatement stmt = c.prepareStatement("SELECT * FROM USERS WHERE USER=?;");			 
			stmt.setString(1, user);
			ResultSet rs = stmt.executeQuery();

			if (rs == null) 
				result = false;
			else if (rs.next() && rs.isFirst()) {

				int level = rs.getInt("LEVEL");
				String hashpass = rs.getString("PASS");

				if (rs.next()) // Se trova pi� di un utente
					result = false; 
				else if (!hashpass.equals(org.apache.commons.codec.digest.DigestUtils.sha1Hex(pass))) // Se la pass non corrisponde
					result = false;
				else if (level < levelreq) // Se il livello � inferiore
					result = false;
				else
					result = true;
				c.commit();
				rs.close();
				stmt.close();
				pool.releaseConnection(c);
				
			}


		} catch (SQLException e) {
			ApplicationController.getApplicationController().log(Level.WARNING,"SQLite error trying to authenticate: " +user );
			e.printStackTrace();
		}
		finally {
		
						
		}

		return result;

	}

		
	private void popolateDB() {

		String dbinit = basepath + separator  +WEBDIR+separator+INITSQL;  
		Connection c = null;
		try {
			c = pool.reserveConnection();

			FileReader fr = null;
			PrintWriter stdwriter;
			PrintWriter errwriter;
			SqlRunner sr;

			fr = new FileReader(dbinit);

			stdwriter = new PrintWriter(System.out);
			errwriter = new PrintWriter(System.err);
			sr = new SqlRunner(c, stdwriter, errwriter, false, true);

			sr.runScript(fr);
		}
		catch (FileNotFoundException e) {
			ApplicationController.getApplicationController().log(Level.SEVERE,"SQLite error creating db. File not found." );
			e.printStackTrace();
			System.exit(1);
		}
		catch (SQLException e) {
			ApplicationController.getApplicationController().log(Level.SEVERE,"SQLite error creating db." );
			e.printStackTrace();
			System.exit(1);
		}

		ApplicationController.getApplicationController().log(Level.INFO,"DB CREATED" );

	}

}
