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
 * This class will manipulate the Records in the data base
 * @author Yazan Halawa
 *
 */
public class RecordDAO {
	private static Logger logger;
	static {
		logger = Logger.getLogger("recordIndexer");
	}
	
	private Database db;
	
	public RecordDAO(Database db) {
		this.db = db;
	}

	/**
	 * This method will add a Record to the Records table in the data base
	 * @param record the record to be added to the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public void add(Record record) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try{
			String query = "insert into records (batch_id, rowNum)"
					+ " values (?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			int batch_id = record.getBatch_id();
			int rowNum = record.getRowNum();
			
			if (batch_id == -1 || rowNum == -1)//if invalid batch id or row num
				throw new DatabaseException("Invalid record");
			
			stmt.setInt(1, record.getBatch_id());
			stmt.setInt(2, record.getRowNum());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				record.setId(id);
			}
			else{
				throw new DatabaseException("Could not insert Record");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not insert Record", e);
		}
		finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * This method will get all the the Records that correspond to the Batch_id
	 * @param batchID the batch id of the target records
	 * @return the list of all the records whose batch ID match the passed one
	 * @throws DatabseException if the operation fails for any reason
	 */
	public List<Record> getAll(int batchID) throws DatabaseException{
		logger.entering("server.database.records", "getAll");
		
		ArrayList<Record> result = new ArrayList<Record>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from records where batch_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (batchID == -1)//if invalid batch id
				throw new DatabaseException("Invalid batch_id reference");
			
			stmt.setInt(1, batchID);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int batch_id = rs.getInt(2);
				int rowNum = rs.getInt(3);

				result.add(new Record(id, batch_id, rowNum));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.records", "getAll", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.records", "getAll");
		
		return result;	
	}
	
	/**
	 * This method will access the Records table and get the record with the target ID
	 * @param recordID the ID of the target record
	 * @return the target record if found in the table, null otherwise
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public Record getRecord(int recordID) throws DatabaseException{
		logger.entering("server.database.records", "getRecord");
		
		Record selectedRecord = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from records where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (recordID == -1)//if invalid record id
				throw new DatabaseException("invalid record_id");
			
			stmt.setInt(1, recordID);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int batch_id = rs.getInt(2);
				int rowNum = rs.getInt(3);

				selectedRecord = new Record(id, batch_id, rowNum);
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.records", "getRecord", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.records", "getRecord");
		
		return selectedRecord;	
	}
}
