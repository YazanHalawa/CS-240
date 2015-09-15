
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
 * This class will test all the methods in the ProjectDAO class
 * @author Yazan Halawa
 *
 */
public class TestProjectDAO {

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
	private ProjectDAO dbProjects;

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
		dbProjects = db.getProjectsDAO();
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
		dbProjects = null;
	}

	/**
	 * Test method for {@link server.database.ProjectDAO#addProject(server.modelClasses.Project)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testAddProject() throws DatabaseException {

		//Add two projects to the data base

		Project testProject1 = new Project(-1, "test1", 0, 0, 20);
		dbProjects.addProject(testProject1);
		
		Project testProject2 = new Project(-1, "test2", 0, 0, 40);
		dbProjects.addProject(testProject2);

		//Check that the data base holds the two projects

		List<Project> all = dbProjects.getAll();
		assertEquals(2, all.size());

		//Check that the projects were added correctly
		boolean foundFirst = false;
		boolean foundSecond = false;
		
		for (Project proj : all) {

			assertFalse(proj.getId() == -1);

			if (!foundFirst) {
				foundFirst = areEqual(proj, testProject1, false);
			}		
			if (!foundSecond) {
				foundSecond = areEqual(proj, testProject2, false);
			}
		}

		assertTrue(foundFirst && foundSecond);
	}

	/**
	 * Test method for {@link server.database.ProjectDAO#getAll()}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetAll() throws DatabaseException {
		List<Project> all = dbProjects.getAll();
		assertEquals(0, all.size());
	}

	/**
	 * Test method for {@link server.database.ProjectDAO#getProject(int)}.
	 * @throws DatabaseException if the operation fails for any reason
	 */
	@Test
	public void testGetProject() throws DatabaseException {
		Project testProject = new Project(-1, "test", 0, 0, 20);
		dbProjects.addProject(testProject);
		
		Project testProjDB = dbProjects.getProject(1);
		assertTrue(areEqual(testProject, testProjDB, false));
	}

	/**
	 * Test method for an invalid add
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException {
		Project invalidProject = new Project (-1, null, -1, -1, -1);
		dbProjects.addProject(invalidProject);
	}
	
	/**
	 * Test method for an invalid GetProject
	 * @throws DatabaseException because of the invalid input
	 */
	@Test(expected=DatabaseException.class)
	public void testInvalidGet() throws DatabaseException {
		dbProjects.getProject(-1);
	}
	
	/**
	 * This method compares two projects and checks if they are equal
	 * @param a the first project
	 * @param b the second project
	 * @param compareIDs a flag to specify whether to compare IDs or not
	 * @return true if the two projects are equal, else false
	 */
	private boolean areEqual(Project a, Project b, boolean compareIDs){
		if (compareIDs){
			if (a.getId() != b.getId()){
				return false;
			}
		}
		return (SafeEquals(a.getTitle(), b.getTitle()) &&
				SafeEquals(a.getRecordsForImage(), b.getRecordsForImage()) &&
				SafeEquals(a.getFirstYCoord(), b.getFirstYCoord()) &&
				SafeEquals(a.getRecordsHeight(), b.getRecordsHeight()));
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
