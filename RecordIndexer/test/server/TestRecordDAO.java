
package server;

import static org.junit.Assert.*;
import server.database.*;
import shared.modelClasses.*;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests all the methods in RecordDAO
 * @author Yazan Halawa
 *
 */
public class TestRecordDAO {

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
	private RecordDAO dbRecords;

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
		dbRecords = db.getRecordsDAO();
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
		dbRecords = null;
	}

	/**
	 * Test method for {@link server.database.RecordDAO#add(server.modelClasses.Record)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testAdd() throws DatabaseException {
		//Add two records to the data base

		Record testRecord1 = new Record(-1, 1, 1);
		dbRecords.add(testRecord1);
		
		Record testRecord2 = new Record(-1, 1, 2);
		dbRecords.add(testRecord2);

		//Check that the data base holds the two records

		List<Record> all = dbRecords.getAll(1);
		assertEquals(2, all.size());

		//Check that the records were added correctly
		boolean foundFirst = false;
		boolean foundSecond = false;

		for (Record record : all) {

			assertFalse(record.getId() == -1);

			if (!foundFirst) {
				foundFirst = areEqual(record, testRecord1, false);
			}		
			if (!foundSecond) {
				foundSecond = areEqual(record, testRecord2, false);
			}
		}

		assertTrue(foundFirst && foundSecond);
	}

	/**
	 * Test method for {@link server.database.RecordDAO#getAll(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetAll() throws DatabaseException {
		List<Record> all = dbRecords.getAll(1);
		assertEquals(0, all.size());
	}
	
	/**
	 * Test method for {@link server.database.RecordDAO#getRecord(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetRecord() throws DatabaseException {
		Record testRecord = new Record(-1, 1, 1);
		dbRecords.add(testRecord);
		
		Record testRecordDB = dbRecords.getRecord(1);
		assertTrue(areEqual(testRecord, testRecordDB, false));
	}
	
	/**
	 * Test method for an invalid add
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		Record invalidRecord = new Record(-1, -1, -1);
		dbRecords.add(invalidRecord);
	}
	
	/**
	 * Test method for an invalid GetALL
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGetAll() throws DatabaseException {
		dbRecords.getAll(-1);
	}
	
	/**
	 * Test method for an invalid GetRecord
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGet() throws DatabaseException {
		dbRecords.getRecord(-1);
	}
	
	/**
	 * This method compares two records and checks if they are equal
	 * @param a the first record
	 * @param b the second record
	 * @param compareIDs a flag to specify whether to compare IDs or not
	 * @return true if the two records are equal, else false
	 */
	private boolean areEqual(Record a, Record b, boolean compareIDs){
		if (compareIDs){
			if (a.getId() != b.getId()){
				return false;
			}
		}
		return (SafeEquals(a.getBatch_id(), b.getBatch_id()) &&
				SafeEquals(a.getRowNum(), b.getRowNum()));
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
