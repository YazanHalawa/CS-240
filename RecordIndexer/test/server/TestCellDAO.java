
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
 * This class tests all the methods in CellDAO
 * @author Yazan Halawa
 *
 */
public class TestCellDAO {

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
	private CellDAO dbCells;

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
		dbCells = db.getCellsDAO();
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
		dbCells = null;
	}

	/**
	 * Test method for {@link server.database.CellDAO#add(server.modelClasses.Cell)}.
	 * @throws DatabaseException if the operatino fails for any reason
	 */
	@Test
	public void testAdd() throws DatabaseException {
		//Add two cells to the data base

		Cell testCell1 = new Cell(-1, 1, 1, "val1");
		dbCells.add(testCell1);
		
		Cell testCell2 = new Cell(-1, 2, 1, "val2");
		dbCells.add(testCell2);

		//Check that the data base holds the two cells

		List<Cell> all = dbCells.getAll(1);
		assertEquals(2, all.size());

		//Check that the cells were added correctly
		boolean foundFirst = false;
		boolean foundSecond = false;

		for (Cell cell : all) {

			assertFalse(cell.getId() == -1);

			if (!foundFirst) {
				foundFirst = areEqual(cell, testCell1, false);
			}		
			if (!foundSecond) {
				foundSecond = areEqual(cell, testCell2, false);
			}
		}

		assertTrue(foundFirst && foundSecond);
	}

	/**
	 * Test method for {@link server.database.CellDAO#getAll(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetAll() throws DatabaseException {
		List<Cell> all = dbCells.getAll(1);
		assertEquals(0, all.size());
	}
	
	/**
	 * Test method for an invalid add
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		Cell invalidCell = new Cell(-1, -1, -1, null);
		dbCells.add(invalidCell);
	}
	
	/**
	 * Test method for an invalid GetAll
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGetAll() throws DatabaseException {
		dbCells.getAll(-1);
	}
	
	/**
	 * This method compares two cells and checks if they are equal
	 * @param a the first cell
	 * @param b the second cell
	 * @param compareIDs a flag to specify whether to compare IDs or not
	 * @return true if the two cells are equal, else false
	 */
	private boolean areEqual(Cell a, Cell b, boolean compareIDs){
		if (compareIDs){
			if (a.getId() != b.getId()){
				return false;
			}
		}
		return (SafeEquals(a.getRecord_id(), b.getRecord_id()) &&
				SafeEquals(a.getField_id(), b.getField_id()) &&
				SafeEquals(a.getValue(), b.getValue()));
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
