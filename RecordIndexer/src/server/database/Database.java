package server.database;

import java.io.*;
import java.sql.*;
import java.util.logging.*;


public class Database {
	
	private static final String DATABASE_DIRECTORY = "database";
	private static final String DATABASE_FILE = "recordIndexer.sqlite";
	private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_DIRECTORY +
												File.separator + DATABASE_FILE;

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("recordIndexer");
	}

	public static void initialize() throws DatabaseException {
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		}
		catch(ClassNotFoundException e) {
			
			DatabaseException serverEx = new DatabaseException("Could not load database driver", e);
			
			logger.throwing("server.dataBaseAccessClasses.Database", "initialize", serverEx);

			throw serverEx; 
		}
	}

	private UserDAO usersDAO;
	private ProjectDAO projectsDAO;
	private BatchDAO batchesDAO;
	private FieldDAO fieldsDAO;
	private RecordDAO recordsDAO;
	private CellDAO cellsDAO;
	private Connection connection;
	
	public Database() {
		usersDAO = new UserDAO(this);
		projectsDAO = new ProjectDAO(this);
		batchesDAO = new BatchDAO(this);
		fieldsDAO = new FieldDAO(this);
		recordsDAO = new RecordDAO(this);
		cellsDAO = new CellDAO(this);
		connection = null;
	}
	
	public UserDAO getUsersDAO() {
		return usersDAO;
	}
	
	public ProjectDAO getProjectsDAO(){
		return projectsDAO;
	}
	
	public BatchDAO getBatchesDAO(){
		return batchesDAO;
	}
	
	public FieldDAO getFieldsDAO(){
		return fieldsDAO;
	}
	
	public RecordDAO getRecordsDAO(){
		return recordsDAO;
	}
	
	public CellDAO getCellsDAO(){
		return cellsDAO;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void startTransaction() throws DatabaseException {
		try {
			assert (connection == null);			
			connection = DriverManager.getConnection(DATABASE_URL);
			connection.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not connect to database. Make sure " + 
				DATABASE_FILE + " is available in ./" + DATABASE_DIRECTORY, e);
		}
	}
	
	public void endTransaction(boolean commit) {
		if (connection != null) {		
			try {
				if (commit) {
					connection.commit();
				}
				else {
					connection.rollback();
				}
			}
			catch (SQLException e) {
				System.out.println("Could not end transaction");
				e.printStackTrace();
			}
			finally {
				safeClose(connection);
				connection = null;
			}
		}
	}
	
	public static void safeClose(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			}
			catch (SQLException e) {
				System.out.println("Could not safe close connection");
				e.printStackTrace();
			}
		}
	}
	
	public static void safeClose(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				System.out.println("Could not safe close statement");
				e.printStackTrace();
			}
		}
	}
	
	public static void safeClose(PreparedStatement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException e) {
				System.out.println("Could not safe close prepared statement");
				e.printStackTrace();
			}
		}
	}
	
	public static void safeClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				System.out.println("Could not safe close resultSet");
				e.printStackTrace();
			}
		}
	}
	
}
