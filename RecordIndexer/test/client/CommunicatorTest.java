
package client;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Server;
import server.importer.ApacheImporter;
import shared.communicationClasses.*;
import client.communicator;

/**
 * This class tests all the methods in the Client Communicator Class
 * @author Yazan Halawa
 *
 */
public class CommunicatorTest {

	client.communicator communicator;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Call the importer to restore the data base
		ApacheImporter myImporter = new ApacheImporter();
		String fileName = "database/Records/Records.xml";
		
		try {
			myImporter.importData(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String[] args = {"8080"};
		Server.main(args);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		communicator = new communicator("localhost", "8080");	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link client.communicator#validateUser
	 * (Shared.communicationClasses.ValidateUser_Params)}.
	 */
	@Test
	public void testValidateUser() {
		ValidateUser_Params params = new ValidateUser_Params();
		ValidateUser_Result results = new ValidateUser_Result();
		
		//First test invalid User
		params.setUserName("invalidusername");
		params.setUserPassword("invalidpassword");
		
		try {
			results = communicator.validateUser(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//the data variables are supposed to be unchanged if it failed
		assertEquals(null, results.getFirstName());
		assertEquals(null, results.getLastName());
		assertEquals(-1, results.getNumOfRecords());

		//Test a valid User
		params.setUserName("sheila");
		params.setUserPassword("parker");

		try {
			results = communicator.validateUser(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals("Sheila", results.getFirstName());
		assertEquals("Parker", results.getLastName());
		assertEquals(0, results.getNumOfRecords());

	}

	/**
	 * Test method for {@link client.communicator#getProjects
	 * (Shared.communicationClasses.GetProjects_Params)}.
	 */
	@Test
	public void testGetProjects() {
		GetProjects_Params params = new GetProjects_Params();
		GetProjects_Result results = new GetProjects_Result();
		
		//First test invalid User
		params.setUserName("yazan");
		params.setUserPassword("halawa");
		
		try {
			results = communicator.getProjects(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(null, results.getProjects());

		//Now test with valid User
		params.setUserName("sheila");
		params.setUserPassword("parker");
		try {
			results = communicator.getProjects(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals(3, results.getProjects().size());
	}

	/**
	 * Test method for {@link client.communicator#getSampleImage
	 * (Shared.communicationClasses.GetSampleImage_Params)}.
	 */
	@Test
	public void testGetSampleImage() {
		GetSampleImage_Params params = new GetSampleImage_Params();
		GetSampleImage_Result results = new GetSampleImage_Result();
		
		//First test invalid User
		params.setUserName("yazan");
		params.setUserPassword("halawa");
		
		try {
			results = communicator.getSampleImage(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(null, results.getImageURL());

		//Now test with valid User
		params.setUserName("sheila");
		params.setUserPassword("parker");
		params.setProject_id(2);
		
		try {
			results = communicator.getSampleImage(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals("http://localhost:8080/images/1900_image0.png", results.getImageURL());
	}

	/**
	 * Test method for {@link client.communicator#downloadBatch
	 * (Shared.communicationClasses.DownloadBatch_Params)}.
	 */
	@Test
	public void testDownloadBatch() {
		DownloadBatch_Params params = new DownloadBatch_Params();
		DownloadBatch_Result results = new DownloadBatch_Result();
		
		//First test invalid User
		params.setUserName("yazan");
		params.setUserPassword("halawa");
		params.setTargetProjectId(1);
		
		try {
			results = communicator.downloadBatch(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertEquals("Failed\n", results.toString());

		//Now test with valid User
		params.setUserName("sheila");
		params.setUserPassword("parker");
		params.setTargetProjectId(2);
		
		try {
			//downloads the first batch and make it unavailable
			results = communicator.downloadBatch(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals("http://localhost:8080/images/1900_image0.png", results.getMyBatch().getDataPath());
	}

	/**
	 * Test method for {@link client.communicator#submitBatch
	 * (Shared.communicationClasses.SubmitBatch_Params)}.
	 */
	@Test
	public void testSubmitBatch() {
		SubmitBatch_Params params = new SubmitBatch_Params();
		SubmitBatch_Result results = new SubmitBatch_Result();
		
		//First test invalid User
		params.setUserName("yazan");
		params.setUserPassword("halawa");
		
		try {
			results = communicator.submitBatch(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(false, results.isSuccess());

		//Now test with valid User
		params.setUserName("sheila");
		params.setUserPassword("parker");
		params.setBatch_id(1);
		params.setFieldValues(new ArrayList<ArrayList<String>>());

		try {
			results = communicator.submitBatch(params);//downloads the first batch and make it unavailable
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals("FAILED\n", results.toString());
	}

	/**
	 * Test method for {@link client.communicator#getFields
	 * (Shared.communicationClasses.GetFields_Params)}.
	 */
	@Test
	public void testGetFields() {
		GetFields_Params params = new GetFields_Params();
		GetFields_Result results = new GetFields_Result();
		
		//First test invalid User
		params.setUserName("yazan");
		params.setUserPassword("halawa");
		try {
			results = communicator.getFields(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(null, results.getFields());

		//Now test with valid User
		params.setUserName("sheila");
		params.setUserPassword("parker");
		params.setProject_id(2);
		try {
			//downloads the first batch and make it unavailable
			results = communicator.getFields(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(5, results.getFields().size());
	}

	/**
	 * Test method for {@link client.communicator#search(Shared.communicationClasses.Search_Params)}.
	 */
	@Test
	public void testSearch() {
		Search_Params params = new Search_Params();
		Search_Result results = new Search_Result();
		
		//First test invalid User
		params.setUserName("yazan");
		params.setUserPassword("halawa");
		
		params.setFields(new ArrayList<Integer>());
		
		params.setValues(new ArrayList<String>());
		
		try {
			results = communicator.search(params);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(results.getMatches().isEmpty());

		//Now test with valid User
		params.setUserName("sheila");
		params.setUserPassword("parker");
		
		ArrayList<String> values = new ArrayList<String>();
		values.add("JEROME");
		params.setValues(values);
		
		ArrayList<Integer> fields = new ArrayList<Integer>();
		fields.add(11);
		fields.add(12);
		params.setFields(fields);
		
		try {
			results = communicator.search(params);//downloads the first batch and make it unavailable
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(1, results.getMatches().size());
	}

}
