
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
 * This class will manipulate the Cells in the data base
 * @author Yazan Halawa
 *
 */
public class CellDAO {
	private static Logger logger;
	static {
		logger = Logger.getLogger("recordIndexer");
	}
	
	private Database db;
	
	public CellDAO(Database db) {
		this.db = db;
	}


	/**
	 * This method will add a Cell to the Cells table in the data base
	 * @param cell the cell to be added to the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public void add(Cell cell) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try{
			String query = "insert into cells (record_id, field_id, value)"
					+ " values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			
			if (cell.getRecord_id() == -1 || cell.getField_id() == -1)//if invalid record id or field id
				throw new DatabaseException("Invalid Cell");
			
			stmt.setInt(1, cell.getRecord_id());
			stmt.setInt(2, cell.getField_id());
			stmt.setString(3, cell.getValue());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				cell.setId(id);
			}
			else{
				throw new DatabaseException("Could not insert cell");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not insert cell", e);
		}
		finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * This method will get all the the Cells that correspond to the field_id
	 * @param fieldID the field id of the target Cells
	 * @return the list of all the Cells whose field ID match the passed one
	 * @throws DatabseException if the operation fails for any reason
	 */
	public List<Cell> getAll(int fieldID) throws DatabaseException{
		logger.entering("server.database.cells", "getAll");
		
		ArrayList<Cell> result = new ArrayList<Cell>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from cells where field_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (fieldID == -1)//if invalid field id
				throw new DatabaseException("Invalid field_id");
			
			stmt.setInt(1, fieldID);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int record_id = rs.getInt(2);
				int field_id = rs.getInt(3);
				String value = rs.getString(4);

				result.add(new Cell(id, record_id, field_id, value));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			logger.throwing("server.database.cells", "getAll", serverEx);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}

		logger.exiting("server.database.cells", "getAll");
		
		return result;	
		
	}

}
