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
 * This class will manipulate the projects in the data base
 * @author Yazan Halawa
 *
 */
public class ProjectDAO {

	private static Logger logger;
	static {
		logger = Logger.getLogger("recordIndexer");
	}
	private Database db;
	
	public ProjectDAO(Database db) {
		this.db = db;
	}

	/**
	 * This method will add a project to the projects table in the data base
	 * @param project the project to be added to the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public void addProject(Project project) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try{
			String query = "insert into projects (title, recordsForImage, firstYCoordinate,recordsHeight)"
					+ " values (?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsForImage());
			stmt.setInt(3, project.getFirstYCoord());
			stmt.setInt(4, project.getRecordsHeight());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				project.setId(id);
			}
			else{
				throw new DatabaseException("Could not insert Project");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not insert Project", e);
		}
		finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	/**
	 * This method will access the Projects table and get all the Projects
	 * @return The list of all Projects in the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public List<Project> getAll() throws DatabaseException{
		logger.entering("server.database.projects", "getAll");
		
		ArrayList<Project> result = new ArrayList<Project>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from projects";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				int recordsForImage = rs.getInt(3);
				int firstYCoordinate = rs.getInt(4);
				int recordsHeight = rs.getInt(5);

				result.add(new Project(id, title, recordsForImage, firstYCoordinate, recordsHeight));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.projects", "getAll", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.projects", "getAll");
		
		return result;	
	}
	
	/**
	 * This method will access the Projects table and get the project with the target ID
	 * @param projectID the ID of the target project
	 * @return the target Project if found in the table, null otherwise
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public Project getProject(int projectID) throws DatabaseException{
		logger.entering("server.database.projects", "getProject");
		
		Project selectedProject = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from projects where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (projectID == -1)//if invalid project id
				throw new DatabaseException("invalid project_id");
			
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				int recordsForImage = rs.getInt(3);
				int firstYCoordinate = rs.getInt(4);
				int recordsHeight = rs.getInt(5);

				selectedProject = new Project(id, title, recordsForImage, firstYCoordinate, recordsHeight);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.projects", "getProject", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.projects", "getProject");
		
		return selectedProject;	
	}
}
