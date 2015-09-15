
package server;

import static org.junit.Assert.*;
import server.database.*;
import shared.modelClasses.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class will test all methods in the BatchDAO
 * @author Yazan Halawa
 *
 */
public class TestBatchDAO {

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
	private BatchDAO dbBatches;

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
		dbBatches = db.getBatchesDAO();
	}

	/**
	 * This method will be called after each test to clean up any data left
	 * in the data base from the last test
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		//Roll back transaction so changes to database are undone
		db.endTransaction(false);

		db = null;
		dbBatches = null;
	}

	/**
	 * Test method for {@link server.database.BatchDAO#getfirstBatch(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetfirstBatch() throws DatabaseException {
		Batch testBatch = new Batch(-1, 1, "path", false);
		dbBatches.add(testBatch);
		
		Batch batchFromDB = dbBatches.getfirstBatch(1);
		assertTrue(areEqual(testBatch, batchFromDB, false)); 
	}

	/**
	 * Test method for {@link server.database.BatchDAO#getfirstAvailableBatch(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetfirstAvailableBatch() throws DatabaseException {
		Batch testBatch1 = new Batch(-1, 1, "path1", false);
		dbBatches.add(testBatch1);
		
		Batch testBatch2 = new Batch(-1, 1, "path2", true);
		dbBatches.add(testBatch2);
		
		Batch batchFromDB = dbBatches.getfirstAvailableBatch(1);
		assertTrue(areEqual(testBatch2, batchFromDB, false)); 
	}

	/**
	 * Test method for {@link server.database.BatchDAO#add(server.modelClasses.Batch)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testAdd() throws DatabaseException {
		
		//Add two batches to the data base

		Batch testBatch1 = new Batch(-1, 1, "path1", true);
		dbBatches.add(testBatch1);
		
		Batch testBatch2 = new Batch(-1, 2, "path2", false);
		dbBatches.add(testBatch2);

		//Check that the data base holds the two batches

		List<Batch> all = new ArrayList<Batch>();
		all.add(dbBatches.getfirstBatch(1));
		all.add(dbBatches.getfirstBatch(2));
		assertEquals(2, all.size());

		//Check that the batches were added correctly
		boolean foundFirst = false;
		boolean foundSecond = false;

		for (Batch batch : all) {

			assertFalse(batch.getId() == -1);

			if (!foundFirst) {
				foundFirst = areEqual(batch, testBatch1, false);
			}		
			if (!foundSecond) {
				foundSecond = areEqual(batch, testBatch2, false);
			}
		}

		assertTrue(foundFirst && foundSecond);
	}
	
	/**
	 * Test method for {@link server.database.BatchDAO#getBatch(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetBatch() throws DatabaseException {
		Batch testBatch = new Batch(-1, 1, "path", true);
		dbBatches.add(testBatch);
		
		Batch testBatchDB = dbBatches.getBatch(1);
		assertTrue(areEqual(testBatch, testBatchDB, false));
	}
	
	/**
	 * Test method for {@link server.database.BatchDAO#setBatchAvailability(int, boolean)}.
	 * @throws DatabaseException of the operation fails for any reason
	 */
	@Test
	public void testSetBatchAvailability() throws DatabaseException {
		Batch testBatch = new Batch(-1, 1, "path", true);
		dbBatches.add(testBatch);
		dbBatches.setBatchAvailability(1, false);
		
		Batch testBatchDB = dbBatches.getBatch(1);
		assertFalse(testBatchDB.isAvailable());
	}

	/**
	 * Test method for an invalid add
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		Batch invalidBatch = new Batch (-1, -1, null, false);
		dbBatches.add(invalidBatch);
	}
	
	/**
	 * Test method for an invalid GetFirstBatch
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGetFirstBatch() throws DatabaseException {
		dbBatches.getfirstBatch(-1);
	}
	
	/**
	 * Test method for an invalid GetFirstAvailableBatch
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGetFirstAvailableBatch() throws DatabaseException {
		dbBatches.getfirstAvailableBatch(-1);
	}
	
	/**
	 * Test method for an invalid getBatch
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGet() throws DatabaseException {
		dbBatches.getBatch(-1);
	}
	
	/**
	 * This method compares two batches and checks if they are equal
	 * @param a the first batch
	 * @param b the second batch
	 * @param compareIDs a flag to specify whether to compare IDs or not
	 * @return true if the two batches are equal, else false
	 */
	private boolean areEqual(Batch a, Batch b, boolean compareIDs){
		if (compareIDs){
			if (a.getId() != b.getId()){
				return false;
			}
		}
		return (SafeEquals(a.getProject_id(), b.getProject_id()) &&
				SafeEquals(a.getDataPath(), b.getDataPath()));
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
