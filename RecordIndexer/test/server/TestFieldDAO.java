
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
 * This class will test all the methods in fieldDAO
 * @author Yazan Halawa
 *
 */
public class TestFieldDAO {

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
	private FieldDAO dbFields; 

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
		dbFields = db.getFieldsDAO();
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
		dbFields = null;
	}

	/**
	 * Test method for {@link server.database.FieldDAO#add(server.modelClasses.Field)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testAdd() throws DatabaseException {
		//Add two fields to the data base

		Field testField1 = new Field(-1, "test1", 1, 20, 1, 30, "html1", "data");
		dbFields.add(testField1);
		
		Field testField2 = new Field(-1, "test2", 2, 20, 2, 60, "html2", "data2");
		dbFields.add(testField2);

		//Check that the data base holds the two fields

		List<Field> all = dbFields.getAllFields();
		assertEquals(2, all.size());

		//Check that the fields were added correctly
		boolean foundFirst = false;
		boolean foundSecond = false;

		for (Field field : all) {

			assertFalse(field.getId() == -1);

			if (!foundFirst) {
				foundFirst = areEqual(field, testField1, false);
			}		
			if (!foundSecond) {
				foundSecond = areEqual(field, testField2, false);
			}
		}

		assertTrue(foundFirst && foundSecond);
	}

	/**
	 * Test method for {@link server.database.FieldDAO#getAllFieldsForProject(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetAllFieldsForProject() throws DatabaseException {
		Field testField1 = new Field(-1, "test1", 1, 20, 1, 30, "html1", "data1");
		dbFields.add(testField1);
		
		Field testField2 = new Field(-1, "test2", 2, 20, 2, 60, "html2", "data2");
		dbFields.add(testField2);
		
		List<Field> all = dbFields.getAllFieldsForProject(1);
		assertEquals(1, all.size());
	}

	/**
	 * Test method for {@link server.database.FieldDAO#getAllFields()}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetAllFields() throws DatabaseException {
		List<Field> all = dbFields.getAllFields();
		assertEquals(0, all.size());
	}
	
	/**
	 * Test method for an invalid add
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		Field invalidField = new Field (-1, null, -1, -1, -1, -1, null, null);
		dbFields.add(invalidField);
	}
	
	/**
	 * Test method for an invalid GetAllFieldsForProject
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGetAllFieldsForProject() throws DatabaseException {
		dbFields.getAllFieldsForProject(-1);
	}
	
	/**
	 * This method compares two fields and checks if they are equal
	 * @param a the first field
	 * @param b the second field
	 * @param compareIDs a flag to specify whether to compare IDs or not
	 * @return true if the two fields are equal, else false
	 */
	private boolean areEqual(Field a, Field b, boolean compareIDs){
		if (compareIDs){
			if (a.getId() != b.getId()){
				return false;
			}
		}
		return (SafeEquals(a.getTitle(), b.getTitle()) &&
				SafeEquals(a.getProject_id(), b.getProject_id()) &&
				SafeEquals(a.getWidth(), b.getWidth()) &&
				SafeEquals(a.getColumnNum(), b.getColumnNum()) &&
				SafeEquals(a.getFirstXCoord(), b.getFirstXCoord()));
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
