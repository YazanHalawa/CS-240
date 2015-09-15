package server.database;

import shared.modelClasses.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * This class will access and manipulate the users in the data base
 * @author Yazan Halawa
 *
 */
public class UserDAO {
	private static Logger logger;
	static {
		logger = Logger.getLogger("recordIndexer");
	}
	private Database db;
	
	public UserDAO(Database db) {
		this.db = db;
	}

	/**
	 * This method will add a user to the users table in the data base
	 * @param user the user to be added to the data base
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public void addUser(User user) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		
		try{
			String query = "insert into users (userName, password, firstName,lastName, email, indexedRecords, currentBatchID) "
					+ "values (?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getIndexedNum());
			stmt.setInt(7, user.getCurrentBatchID());
			
			if(stmt.executeUpdate() == 1){
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				user.setId(id);
			}
			else{
				throw new DatabaseException("Could not insert User");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not insert User", e);
		}
		finally{
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	/**
	 * This method will check the users table for the passed user information.
	 * @param userName the userName for the target User
	 * @param password the password for the target User
	 * @return the User if he exists in the table, null otherwise
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public User checkUser(String userName, String password) throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User selectedUser = null;
		
		if (userName == null || password == null)//if invalid user name or password
			throw new DatabaseException("Invalid username or password");
		try{
			String query = "select * from users where userName=? and password=?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, userName);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			
			while (rs.next()){
				int id = rs.getInt(1);
				String usrName = rs.getString(2);
				String pass = rs.getString(3);
				String firstName = rs.getString(4);
				String lastName = rs.getString(5);
				String email = rs.getString(6);
				int indexedRecords = rs.getInt(7);
				int currentBatchID = rs.getInt(8);
				selectedUser = new User(id, pass, usrName, firstName, lastName, email, 
										indexedRecords, currentBatchID);
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not check User", e);
		}
		finally{
			Database.safeClose(stmt);
			Database.safeClose(rs);
		}
		return selectedUser;
	}
	/**
	 * This method will update the User's current batch Id when either downloading
	 * a batch or submitting one and also increment the number of indexed records if 
	 * we are submitting a batch
	 * @param currentID the ID to set the current batch id to.
	 * @param submitted a boolean to specify if we are downloading a batch or submitting one
	 * @throws DatabaseException if the operation fails for any reason
	 */
	public void updateID(User user, int currentID, boolean submitted, int numberOfRecords) throws DatabaseException{
		logger.entering("server.database.users", "updateID");
		
		PreparedStatement stmt = null;
		
		try{
			String query = "update users set currentBatchID=?, indexedRecords=? where id=?";
			stmt = db.getConnection().prepareStatement(query);
			
			if (submitted){//if the user has submitted a batch
				stmt.setInt(1, -1);
				stmt.setInt(2, user.getIndexedNum()+numberOfRecords);
			}
			else{
				stmt.setInt(1, currentID);
				stmt.setInt(2, user.getIndexedNum());
			}
			
			stmt.setInt(3, user.getId());
			
			if (stmt.executeUpdate() != 1){
				throw new DatabaseException("Could not update User");
			}
		}
		catch(SQLException e){
			throw new DatabaseException("Could not update User", e);
		}
		finally{
			Database.safeClose(stmt);
		}
	}

}
