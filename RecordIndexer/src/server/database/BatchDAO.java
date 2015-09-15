
package server.database;

import shared.modelClasses.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * This class will access the batches in the database
 * @author Yazan Halawa
 *
 */
public class BatchDAO {
	private static Logger logger;
	static {
		logger = Logger.getLogger("recordIndexer");
	}
	
	private Database db;
	
	public BatchDAO(Database db) {
		this.db = db;
	}

	/**
	 * This method will access the Batches table and get the first batch that 
	 * corresponds to the selected project ID
	 * @param projectID the project id of the target batch
	 * @return the first batch that corresponds to the selected project ID
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public Batch getfirstBatch(int projectID) throws DatabaseException{
		logger.entering("server.database.batches", "getFirstBatch");
		
		Batch selectedBatch = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from batches where project_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (projectID == -1)//if invalid project id
				throw new DatabaseException("invalid project_id");
			
			stmt.setInt(1, projectID);
			rs = stmt.executeQuery();
			
			if (rs.next()){
				int id = rs.getInt(1);
				int project_id = rs.getInt(2);
				String dataPath = rs.getString(3);
				boolean available = rs.getBoolean(4);
				selectedBatch = new Batch(id, project_id, dataPath, available);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.batches", "getFirstBatch", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.batches", "getFirstBatch");
		
		return selectedBatch;		
	}
	
	/**
	 * This method will access the Batches table and get the first available batch that 
	 * corresponds to the selected project ID
	 * @param projectID the project id of the target batch
	 * @return the first available batch that corresponds to the selected project ID
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public Batch getfirstAvailableBatch(int projectID) throws DatabaseException{
		logger.entering("server.database.batches", "getFirstAvailableBatch");
		
		Batch selectedBatch = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from batches where project_id = ? and available = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (projectID == -1)//if invalid project id
				throw new DatabaseException("invalid project_id");
			
			stmt.setInt(1, projectID);
			stmt.setBoolean(2, true);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				int project_id = rs.getInt(2);
				String dataPath = rs.getString(3);
				boolean available = rs.getBoolean(4);

				selectedBatch = new Batch(id, project_id, dataPath, available);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.batches", "getFirstAvailableBatch", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.batches", "getFirstAvailableBatch");
		
		return selectedBatch;	
	}
	/**
	 * This method will add a Batch to the Batches table in the data base
	 * @param batch the batch to be added to the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public void add(Batch batch) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try{
			String query = "insert into batches (project_id, dataPath, available)"
					+ " values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getProject_id());
			stmt.setString(2, batch.getDataPath());
			stmt.setBoolean(3, batch.isAvailable());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				batch.setId(id);
			}
			else{
				throw new DatabaseException("Could not insert batch");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not insert batch", e);
		}
		finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * This method will access the batches table and get the batch with the target ID
	 * @param batchID the ID of the target batch
	 * @return the target batch if found in the table, null otherwise
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public Batch getBatch(int batchID) throws DatabaseException{
		logger.entering("server.database.batches", "getBatch");
		
		Batch selectedBatch = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from batches where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (batchID == -1)//if invalid batch id
				throw new DatabaseException("invalid batch_id");
			
			stmt.setInt(1, batchID);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int project_id = rs.getInt(2);
				String dataPath = rs.getString(3);
				boolean available = rs.getBoolean(4);

				selectedBatch = new Batch(id, project_id, dataPath, available);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.batches", "getBatch", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.batches", "getBatch");
		
		return selectedBatch;	
	}
	
	public void setBatchAvailability(int batchID, boolean availability) throws DatabaseException{
		PreparedStatement stmt = null;
		
		try{
			String query = "update batches set available=? where id=?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setBoolean(1, availability);
			stmt.setInt(2, batchID);
			
			if (stmt.executeUpdate() != 1){
				throw new DatabaseException("Could not update batch");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not update batch", e);
		}
		finally{
			Database.safeClose(stmt);
		}
	}

}
