package server.importer;

import java.io.*;
import server.database.*;
import shared.modelClasses.*;
import java.util.logging.*;

/*
 *	This is the import you need to use the Apache Commons IO library.
 *	Look at the code marked {/*(**APACHE**)*/
//*/
import org.apache.commons.io.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

public class ApacheImporter
{
	private static Logger logger;
	private Database db; 
	private UserDAO dbUsers;
	private ProjectDAO dbProjects;
	private FieldDAO dbFields;
	private RecordDAO dbRecords;
	private BatchDAO dbBatches;
	private CellDAO dbCells;
	private int NumberOfPastFields;
	private int NumberOfPastRecords;
	private int NumberOfPastBatches;

	static {
		logger = Logger.getLogger("recordIndexer");
	}

	public void importData(String xmlFilename) throws Exception
	{

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File xmlFile = new File(xmlFilename);
		File dest = new File("./Records");

		File emptydb = new File("backup" + File.separator + "recordIndexer.sqlite");
		File currentdb = new File("database" + File.separator + "recordIndexer.sqlite");

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

		//Load database driver
		Database.initialize();

		//Create a new Database
		db = new Database();
		
		//Prepare Database for test case
		db.startTransaction();
		dbUsers = db.getUsersDAO();
		dbProjects = db.getProjectsDAO();
		dbFields = db.getFieldsDAO();
		dbBatches = db.getBatchesDAO();
		dbRecords = db.getRecordsDAO();
		dbCells = db.getCellsDAO();


		File parsefile = new File(dest.getPath() + File.separator + xmlFile.getName());
		Document doc = builder.parse(parsefile);


		NodeList users = doc.getElementsByTagName("user");
		parseUsers(users);

		logger.info("I'm getting to here");
		NodeList projects = doc.getElementsByTagName("project");
		NumberOfPastFields = 0; // init
		NumberOfPastRecords = 0;// init
		NumberOfPastBatches = 0; // init
		parseProjects(projects);
		
		//Commit changes to the data base
		db.endTransaction(true);

		db = null;
		dbBatches = null;
	}


	private void parseUsers(NodeList usersList)
	{
		/*
		 *	This is where you will iterate through all of the users in the
		 *	usersList NodeList and extract the information to create and insert
		 *	a user into your database.
		 */
		for (int i = 0; i < usersList.getLength(); i++){
			Element myElement = (Element) usersList.item(i);

			Element userNameElement = (Element) myElement.getElementsByTagName("username").item(0);
			String userName = userNameElement.getTextContent();

			Element passwordElement = (Element) myElement.getElementsByTagName("password").item(0);
			String password = passwordElement.getTextContent();

			Element firstNameElement = (Element) myElement.getElementsByTagName("firstname").item(0);
			String firstName = firstNameElement.getTextContent();

			Element lastNameElement = (Element) myElement.getElementsByTagName("lastname").item(0);
			String lastName = lastNameElement.getTextContent();

			Element emailElement = (Element) myElement.getElementsByTagName("email").item(0);
			String email = emailElement.getTextContent();

			Element indexedRecordsElement = (Element) myElement.getElementsByTagName("indexedrecords")
																							.item(0);
			int indexedRecords = Integer.parseInt(indexedRecordsElement.getTextContent());

			User user = new User(-1, password, userName, firstName, lastName, email, indexedRecords, -1);
			//Add user to data base
			try {
				dbUsers.addUser(user);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}

	}


	private void parseProjects(NodeList projectsList)
	{
		/*
		 *	This is where you will iterate through all of the users in the
		 *	projectsList NodeList and extract the information to create and
		 *	insert a project into your database.
		 */
		for (int i = 0; i < projectsList.getLength(); i++){
			Element myElement = (Element) projectsList.item(i);

			Element titleElement = (Element) myElement.getElementsByTagName("title").item(0);
			String title = titleElement.getTextContent();

			Element recordsperImageElement = (Element) myElement.getElementsByTagName("recordsperimage")
																							.item(0);
			int recordsperImage = Integer.parseInt(recordsperImageElement.getTextContent());

			Element firstYCoordElement = (Element) myElement.getElementsByTagName("firstycoord")
																							.item(0);
			int firstYCoord = Integer.parseInt(firstYCoordElement.getTextContent());

			Element recordHeightElement = (Element) myElement.getElementsByTagName("recordheight")
																							.item(0);
			int recordHeight = Integer.parseInt(recordHeightElement.getTextContent());

			Project project = new Project(-1, title, recordsperImage, firstYCoord, recordHeight);
			
			//Add project to dataBase
			try {
				dbProjects.addProject(project);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			
			//Iterate through the fields
			int columnNum = 1;
			NodeList myFields = myElement.getElementsByTagName("field");
			for (int j = 0; j < myFields.getLength(); j++){
				Element myField = (Element) myFields.item(j);
				parseField(columnNum, i+1, myField);
				columnNum++;
			}
			
			//Iterate through the batches
			NodeList myBatches = myElement.getElementsByTagName("image");
			for (int k = 0; k < myBatches.getLength(); k++){
				Element myBatch = (Element) myBatches.item(k);
				parseImage(NumberOfPastBatches+1, i+1, myBatch);
				NumberOfPastBatches++;
			}
			NumberOfPastFields += (columnNum-1);
		}
	}


	private void parseImage(int batch_id, int project_id, Element myBatch) {
		Element fileElement = (Element) myBatch.getElementsByTagName("file").item(0);
		String file = fileElement.getTextContent();

		Batch batch = new Batch(-1, project_id, file, true);
		
		//Add batch to database
		try {
			dbBatches.add(batch);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		//Iterate through the records
		int rowNum = 1;
		NodeList myRecords = myBatch.getElementsByTagName("record");
		for (int i = 0; i < myRecords.getLength(); i++){
			Element myRecord = (Element) myRecords.item(i);
			parseRecord(NumberOfPastRecords+1, rowNum, batch_id, myRecord);
			rowNum++;
			NumberOfPastRecords++;
		}
	}


	private void parseRecord(int record_id, int rowNum, int batch_id, Element myRecord) {
		Record record = new Record(-1, batch_id, rowNum);
		
		//Add record to data base
		try {
			dbRecords.add(record);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		//Iterate through the cells
		NodeList cells = myRecord.getElementsByTagName("value");
		int field_id = 1 + NumberOfPastFields;
		for (int i = 0; i < cells.getLength(); i++){
			Element myCell = (Element) cells.item(i);
			parseCell(record_id, field_id, myCell);
			field_id++;
		}

	}


	private void parseCell(int record_id, int field_id,  Element myCell) {
		String value = myCell.getTextContent();
		Cell cell = new Cell(-1, record_id, field_id, value);
		
		//Add cell to data base
		try {
			dbCells.add(cell);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}


	private void parseField(int columnNum, int project_id, Element myField) {
		Element titleElement = (Element) myField.getElementsByTagName("title").item(0);
		String title = titleElement.getTextContent();

		Element xCoordElement = (Element) myField.getElementsByTagName("xcoord").item(0);
		int xCoord = Integer.parseInt(xCoordElement.getTextContent());

		Element widthElement = (Element) myField.getElementsByTagName("width").item(0);
		int width = Integer.parseInt(widthElement.getTextContent());

		Element helpHtmlElement = (Element) myField.getElementsByTagName("helphtml").item(0);
		String helpHtml = helpHtmlElement.getTextContent();

		Element knownDataElement = (Element) myField.getElementsByTagName("knowndata").item(0);
		String knownData = null;

		if (knownDataElement != null){
			knownData = knownDataElement.getTextContent();
		}
		Field field = new Field(-1, title, project_id, width, columnNum, xCoord, helpHtml, knownData);
		
		//Add field to data base
		try {
			dbFields.add(field);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args){
		ApacheImporter myImporter = new ApacheImporter();
		String fileName;
		
		if (args.length > 0)
			fileName = args[0];
		else
			fileName = "database/Records/Records.xml";//Default xml file
		
		try {
			myImporter.importData(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}