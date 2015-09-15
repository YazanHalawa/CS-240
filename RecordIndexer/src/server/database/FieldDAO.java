package server.database;

import shared.modelClasses.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class will access the fields in the data base
 * @author Yazan Halawa
 *
 */
public class FieldDAO {
	private static Logger logger;
	static {
		logger = Logger.getLogger("recordIndexer");
	}
	private Database db;
	
	public FieldDAO(Database db) {
		this.db = db;
	}

	/**
	 * This method will add a Field to the Fields table in the data base
	 * @param field the field to be added to the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public void add(Field field) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try{
			String query = "insert into fields (title, project_id, width, columnNum, firstXCoordinate,"
					+ "helpHtml, knownData) values (?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, field.getTitle());
			stmt.setInt(2, field.getProject_id());
			stmt.setInt(3, field.getWidth());
			stmt.setInt(4, field.getColumnNum());
			stmt.setInt(5, field.getFirstXCoord());
			stmt.setString(6, field.getHelpHtml());
			stmt.setString(7, field.getKnownData());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				field.setId(id);
			}
			else{
				throw new DatabaseException("Could not insert Field");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not insert Field", e);
		}
		finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * This method will access the Fields table and get all the Fields for a specific project
	 * @param projectID the ID of the project whose fields we want
	 * @return The list of all Fields for the project
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public List<Field> getAllFieldsForProject(int projectID) throws DatabaseException{
		logger.entering("server.database.fields", "getAllFieldsForProject");
		
		ArrayList<Field> result = new ArrayList<Field>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from fields where project_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (projectID == -1)//if invalid project id
				throw new DatabaseException("invalid project_id");
			
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				int project_id = rs.getInt(3);
				int width = rs.getInt(4);
				int columnNum = rs.getInt(5);
				int firstXCoordinate = rs.getInt(6);
				String helpHtml = rs.getString(7);
				String knownData = rs.getString(8);
				result.add(new Field(id, title, project_id, width, columnNum,
									firstXCoordinate, helpHtml, knownData));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.fields", "getAllFieldsForProject", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.fields", "getAllFieldsForProject");
		
		return result;
		
	}
	
	/**
	 * This method will access the Fields table and get all the Fields in the data base
	 * @return The list of all Fields in the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public List<Field> getAllFields() throws DatabaseException{
		logger.entering("server.database.fields", "getAllFields");
		
		ArrayList<Field> result = new ArrayList<Field>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from fields";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				int project_id = rs.getInt(3);
				int width = rs.getInt(4);
				int columnNum = rs.getInt(5);
				int firstXCoordinate = rs.getInt(6);
				String helpHtml = rs.getString(7);
				String knownData = rs.getString(8);
				result.add(new Field(id, title, project_id, width, columnNum, firstXCoordinate,
									helpHtml, knownData));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.fields", "getAllFields", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.fields", "getAllFields");
		
		return result;	
		
	}
}
