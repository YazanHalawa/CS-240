package server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.junit.* ;

import static org.junit.Assert.* ;

public class ServerUnitTests {
	private static Logger logger;

	static {
		logger = Logger.getLogger("recordIndexer");
	}
	
	@Before
	public void setup() throws ParserConfigurationException {
		
		//This code will clean up and empty the data base
		File xmlFile = new File("database/Records/Records.xml");
		File dest = new File("Records");
		File emptydb = new File("backup" + File.separator + "recordindexer.sqlite");
		File currentdb = new File("database" + File.separator + "recordindexer.sqlite");

		/*
		 * (**APACHE**)
		 */
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!xmlFile.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);

			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(xmlFile.getParentFile(), dest);

			//	Overwrite my existing *.sqlite database with an empty one.  Now, my
			//	database is completely empty and ready to load with data.
			FileUtils.copyFile(emptydb, currentdb);
		}
		catch (IOException e)
		{
			logger.log(Level.SEVERE, "Unable to deal with the IOException thrown", e);
		}
		/*
		 * (**APACHE**)
		 */
	}
	
	@After
	public void teardown() {
	}
	
	@Test
	public void test_1() {
		assertEquals("OK", "OK");
		assertTrue(true);
		assertFalse(false);
	}

	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
				"server.ServerUnitTests", "server.TestBatchDAO", "server.TestCellDAO", "server.TestFieldDAO", "server.TestProjectDAO",
				"server.TestRecordDAO", "server.TestUserDAO", "client.CommunicatorTest"
				};

		org.junit.runner.JUnitCore.main(testClasses);
	}
	
}

