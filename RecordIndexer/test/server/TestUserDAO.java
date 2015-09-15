
package server;

import static org.junit.Assert.*;
import server.database.*;
import shared.modelClasses.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class will test all the methods in the UserDAO class
 * @author Yazan Halawa
 *
 */
public class TestUserDAO {

	/**
	 * This method will be called before all the Tests to set up the architecture base for testing
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		//Load database driver
		Database.initialize();
	}

	/**
	 * this method will be called at the end after all the tests to clean up
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		return;
	}

	private Database db;
	private UserDAO dbUsers;
	
	/**
	 * This method will be called before each test to set up the data base
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		//Create a new Database
		db = new Database();
		//Prepare Database for test case
		db.startTransaction();
		dbUsers = db.getUsersDAO();
	}

	/**
	 * This method will be called after each test to clean up any data left
	 * in the data base from the last test
	 * @throws java.lang.Exception if the operation fails for any reason
	 */
	@After
	public void tearDown() throws Exception {
		
		//Roll back transaction so changes to database are undone
		db.endTransaction(false);
		
		db = null;
		dbUsers = null;
	}

	/**
	 * Test method for {@link server.database.UserDAO#addUser(server.modelClasses.User)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testAddUser() throws DatabaseException {
		
		//Add two users to the data base
		User Bob = new User(-1, "FirstPassCode", "BobAwesome", "Bob", "White",
							"BobWhite@gmail.com", 0, -1);
		dbUsers.addUser(Bob);
		
		User Amy = new User(-1, "SecondPassCode", "AmyAwesome", "Amy", "White",
							"AmyWhite@gmail.com", 0, -1);
		dbUsers.addUser(Amy);
		
		//Get the Users from the data base
		User bobInDataBase = dbUsers.checkUser("BobAwesome", "FirstPassCode");
		User amyInDataBase = dbUsers.checkUser("AmyAwesome", "SecondPassCode");
		
		//check that the Users are equals
		assertTrue(areEqual(Bob, bobInDataBase, true));
		assertTrue(areEqual(Amy, amyInDataBase, true));
		//check that their IDs are correct
		assertEquals(1, bobInDataBase.getId());
		assertEquals(2, amyInDataBase.getId());
	}

	/**
	 * Test method for {@link server.database.UserDAO#checkUser(java.lang.String, java.lang.String)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testCheckUser() throws DatabaseException {
		// check that it does not find users not in the data base 
		assertEquals(null, dbUsers.checkUser("Bob", "password"));
		/* The test for the Add user function already checked the case of getting a user
		 * in the data base */
	}

	/**
	 * Test method for {@link server.database.UserDAO#updateID(server.modelClasses.User, int, boolean)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testUpdateID() throws DatabaseException {
		User Amy = new User(-1, "SecondPassCode", "AmyAwesome", "Amy", "White",
				"AmyWhite@gmail.com", 0, -1);
		dbUsers.addUser(Amy);
		
		User amyInDataBase = dbUsers.checkUser("AmyAwesome", "SecondPassCode");
		
		// Check the number of indexed records before the alteration, and the currentBatchID
		
		assertEquals(0, amyInDataBase.getIndexedNum());
		assertEquals(-1, amyInDataBase.getCurrentBatchID());
		
		// in case of downloading a batch
		
		dbUsers.updateID(amyInDataBase, 2, false, 2);
		amyInDataBase = dbUsers.checkUser("AmyAwesome", "SecondPassCode");
		assertEquals(0, amyInDataBase.getIndexedNum());
		assertEquals(2, amyInDataBase.getCurrentBatchID());
		
		// in case of submitting a batch
		
		dbUsers.updateID(amyInDataBase, 2, true, 2);
		amyInDataBase = dbUsers.checkUser("AmyAwesome", "SecondPassCode");
		assertEquals(2, amyInDataBase.getIndexedNum());
		assertEquals(-1, amyInDataBase.getCurrentBatchID());
	}
	
	/**
	 * Test method for an invalid add
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		User invalidUser = new User (-1, null, null, null, null, null, -1, -1);
		dbUsers.addUser(invalidUser);
	}
	
	/**
	 * Test method for an invalid updateID
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidUpdate() throws DatabaseException {
		
		User invalidUser = new User (-1, null, null, null, null, null, -1, -1);
		dbUsers.updateID(invalidUser, -1, false, -1);
	}
	
	/**
	 * Test method for an invalid CheckUser
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidCheckUser() throws DatabaseException {
		dbUsers.checkUser(null, null);
	}
	
	/**
	 * This method compares two Users and checks if they are equal
	 * @param a the first user
	 * @param b the second user
	 * @param compareIDs a flag to specify whether to compare IDs or not
	 * @return true if the two users are equal, else false
	 */
	private boolean areEqual(User a, User b, boolean compareIDs){
		if (compareIDs){
			if (a.getId() != b.getId()){
				return false;
			}
		}
		return (SafeEquals(a.getCurrentBatchID(), b.getCurrentBatchID()) &&
				SafeEquals(a.getUserName(), b.getUserName()) &&
				SafeEquals(a.getPassword(), b.getPassword()) &&
				SafeEquals(a.getFirstName(), b.getFirstName()) &&
				SafeEquals(a.getLastName(), b.getLastName()) &&
				SafeEquals(a.getEmail(), b.getEmail()) &&
				SafeEquals(a.getIndexedNum(), b.getIndexedNum()));
	}

	/**
	 * This method is a safe equals that checks whether the two
	 * objects are null
	 * @param a the first object
	 * @param b the second object
	 * @return true if they are equal, else false
	 */
	private boolean SafeEquals(Object a, Object b) {
		if (a == null || b == null) {
			return (a == null && b == null);
		}
		else{
			return a.equals(b);
		}
	}

}
