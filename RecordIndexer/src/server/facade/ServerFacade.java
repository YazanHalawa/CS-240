package server.facade;

import java.util.*;

import server.*;
import server.database.*;
import shared.communicationClasses.*;
import shared.modelClasses.*;
public class ServerFacade {

	/**
	 * This method initializes the data base
	 * @throws ServerException if the operation fails for any reason
	 */
	public static void initialize() throws ServerException {		
		try {
			//Load data base driver
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}		
	}
	
	/**
	 * This method accesses the Users table and uses the select operation
	 * on the data base to check if the user exists and grab its information 
	 * if it does
	 * @param params a VaildateUser_Params object which contains the User's name
	 * and password.
	 * @return the user's information if he exists, else False if he does not exist, 
	 * else a failed string if the operation fails for any reason. 
	 * @throws ServerException if the operation fails for any reason
	 */
	public static ValidateUser_Result validateUser(ValidateUser_Params params) throws ServerException{
		Database db = new Database();
		User returnUser = null;
		
		try{
			db.startTransaction();
			returnUser = db.getUsersDAO().checkUser(params.getUserName(), params.getUserPassword());
		}
		catch (DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		
		db.endTransaction(true);
		
		//Create result object
		ValidateUser_Result result = new ValidateUser_Result();
		if (returnUser != null){
			result.setFirstName(returnUser.getFirstName());
			result.setLastName(returnUser.getLastName());
			result.setNumOfRecords(returnUser.getIndexedNum());
		}
		
		return result;
	}
	
	/**
	 * This method validates the user first then accesses the projects table 
	 * in the data base and gets information about all available projects.
	 * @param params a GetProjects_Params object which contains the user name and password
	 * @return the title and id for every available project, else returns a failed string if the operation fails
	 * @throws ServerException if the operation fails for any reason
	 */
	public static GetProjects_Result getProjects(GetProjects_Params params) throws ServerException{
		Database db = new Database();
		ArrayList<Project> projects = null;
		
		try{
			db.startTransaction();
			
			//Validate User first
			User validatedUser = db.getUsersDAO().checkUser(params.getUserName(),
															params.getUserPassword());
			
			if (validatedUser != null && validatedUser.getFirstName() != null &&
					validatedUser.getFirstName().length() > 0){
				projects = (ArrayList<Project>) db.getProjectsDAO().getAll();
			}
		}
		catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		
		db.endTransaction(true);
		
		//Create result object
		GetProjects_Result result = new GetProjects_Result();
		result.setProjects(projects);
		
		return result;
	}

	/**
	 * This method validates the user first then accesses the batches table
	 * and finds the matching project Id and selects the first batch
	 * @param params a GetSampleImage_Params object which contains the user name 
	 * and password and the target project id
	 * @return the batch's URL, else a failed string if the operation fails for any reason
	 * @throws ServerException if the operation fails for any reason
	 */
	public static GetSampleImage_Result getSampleImage(GetSampleImage_Params params)
																throws ServerException{
		Database db = new Database();
		String path = null;
		
		try{
			db.startTransaction();
			
			//Validate User first
			User validatedUser = db.getUsersDAO().checkUser(params.getUserName(),
															params.getUserPassword());
			
			if (validatedUser != null && validatedUser.getFirstName() != null &&
											validatedUser.getFirstName().length() > 0){
				path = db.getBatchesDAO().getfirstBatch(params.getProject_id()).getDataPath();
			}
		}
		catch(Exception e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		db.endTransaction(true);
		
		//Create result object
		GetSampleImage_Result result = new GetSampleImage_Result();
		result.setImageURL(path);
		
		return result;
	}
	
	/**
	 * This method validates the user first then accesses the batches table
	 * and finds the matching project Id and selects the first available batch and returns the
	 * information of that batch then accesses the Fields table and returns information related to 
	 * the field which the batch resides in. Then it updates the currentBatchID in the users table
	 * @param params a DownloadBatch_Params object which contains the username and password
	 * and the target project id
	 * @return the information of the batch and the related field, else a failed string 
	 * if the operation fails for any reason 
	 * @throws ServerException if the operation fails for any reason
	 */
	public static DownloadBatch_Result downloadBatch(DownloadBatch_Params params)
																	throws ServerException{
		Database db = new Database();
		User validatedUser = null;
		Batch firstBatch = null;
		Project referencedProject = null;
		DownloadBatch_Result result = new DownloadBatch_Result();
		
		try{
			db.startTransaction();
			
			//Validate User first
			validatedUser = db.getUsersDAO().checkUser(params.getUserName(), params.getUserPassword());
			
			if (validatedUser != null && validatedUser.getFirstName() != null &&
				validatedUser.getFirstName().length() > 0 && validatedUser.getCurrentBatchID() == -1){
				
				//Get the first available batch
				firstBatch = db.getBatchesDAO().getfirstAvailableBatch(params.getTargetProjectId());
				
				if (firstBatch != null){//If the batch is valid
					
					//Set the batch's availability to false and update the user's current batch id
					db.getBatchesDAO().setBatchAvailability(firstBatch.getId(), false);
					db.getUsersDAO().updateID(validatedUser, firstBatch.getId(), false, 0);
					
					result.setMyBatch(firstBatch);
					
					//Get the referenced Project
					referencedProject = db.getProjectsDAO().getProject(firstBatch.getProject_id());
					if (referencedProject != null){
						result.setMyProj(db.getProjectsDAO().getProject(firstBatch.getProject_id()));
						ArrayList<Field> fields = (ArrayList<Field>) db.getFieldsDAO()
													.getAllFieldsForProject(firstBatch.getProject_id());
						result.setFields(fields);
					}
				}
			}
		}
		catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		db.endTransaction(true);
		return result;
	}
	
	/**
	 * This method validates the user first then accesses the users table
	 * and unassigns the user from the submitted batch by incrementing the number of records 
	 * indexed and updating the currentBatchID to the default value.
	 * @param params a SubmitBatch_id object which contains the user's name and password and
	 * batch ID and a 2d Array of strings which contain the field values for the batch.
	 * @return True if the operations succeeds, else a failed string
	 * @throws ServerException if the operation fails for any reason
	 */
	public static SubmitBatch_Result submitBatch(SubmitBatch_Params params) throws ServerException{
		Database db = new Database();
		User validatedUser = null;
		SubmitBatch_Result result = new SubmitBatch_Result();
		result.setSuccess(false);
		
		try{
			db.startTransaction();
			
			//Validate User first
			validatedUser = db.getUsersDAO().checkUser(params.getUserName(), params.getUserPassword());
			
			if (validatedUser != null && validatedUser.getFirstName() != null &&
				validatedUser.getFirstName().length() > 0 && validatedUser.getCurrentBatchID() != -1
				&& params.getBatch_id() > 0 && !db.getBatchesDAO().getBatch(params.getBatch_id()).isAvailable()){
				
				//we first add the cells and records
				ArrayList<ArrayList<String>> values = params.getFieldValues();
				int i;
				for (i = 0; i < values.size(); i++){
					Record myRecord = new Record(-1, params.getBatch_id(), i+1);
					db.getRecordsDAO().add(myRecord);
					for (int j = 0; j < values.get(i).size(); j++){
						Cell myCell = new Cell(-1,  myRecord.getId(), j+1, values.get(i).get(j));
						db.getCellsDAO().add(myCell);
					}
				}
				
				//now we update the user id and number of indexed records
				db.getUsersDAO().updateID(validatedUser, -1, true, i);
				result.setSuccess(true);
				db.endTransaction(true);
			}
			else{
				db.endTransaction(false);
			}
		}
		catch(DatabaseException e){
			result.setSuccess(false);
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		
		return result;
	}
	
	/**
	 * This method validates the user first then accesses the fields table
	 * and returns the information about the fields relating to the specified project. If
	 * not project is specified, returns information about all of the fields for all 
	 * projects in the system.
	 * @param params a GetField_Params object which contains the user name
	 * and password and the project id
	 * @return the fields and their information, else a failed string if the operation fails for any reason
	 * @throws ServerException if the operation fails for any reason
	 */
	public static GetFields_Result getFields(GetFields_Params params) throws ServerException{
		Database db = new Database();
		ArrayList<Field> fields = null;
		User validatedUser = null;
		GetFields_Result result = new GetFields_Result();
		
		try{
			db.startTransaction();
			
			//Validate User first
			validatedUser = db.getUsersDAO().checkUser(params.getUserName(), params.getUserPassword());
			
			if (validatedUser != null && validatedUser.getFirstName() != null &&
					validatedUser.getFirstName().length() > 0){
				
				if (params.getProject_id() != -1){//If the project id is declared
					if (params.getProject_id() <= db.getProjectsDAO().getAll().size() && 
							params.getProject_id() > 0){
						fields = (ArrayList<Field>) db.getFieldsDAO().getAllFieldsForProject
								(params.getProject_id());
					}
				}	
				else
					fields = (ArrayList<Field>) db.getFieldsDAO().getAllFields();
				
			}
		}
		catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		db.endTransaction(true);
		result.setFields(fields);
		
		return result;
	}
	
	/**
	 * This method validates the user first then searches the indexed records for the specified strings
	 * in the specified fields. This is done by joining all the tables in the data base and accessing the resulting table
	 * @param params a Search_Params which contains the user name and password and a 
	 * array of Strings which contains the fields, and another array of Strings which contains the 
	 * the values to search for
	 * @return the batch id, image URL, record number and field id relating to the search, else
	 * a failed string if the operation fails for any reason.
	 * @throws ServerException if the operation fails for any reason
	 */
	public static Search_Result search(Search_Params params) throws ServerException{
		Database db = new Database();
		User validatedUser = null;
		ArrayList<Search_Result_Tuple> tuples = new ArrayList<Search_Result_Tuple>();
		Search_Result result = new Search_Result();
		
		try{
			db.startTransaction();
			
			//Validate User first
			validatedUser = db.getUsersDAO().checkUser(params.getUserName(), params.getUserPassword());
			
			if (validatedUser != null && validatedUser.getFirstName() != null &&
										validatedUser.getFirstName().length() > 0){
				
				Set<Record> selectedRecords = new HashSet<Record>();
				
				//Iterate through the fields selected
				for (int id: params.getFields()){
					
					//iterate through all their cells
					List<Cell> cells = db.getCellsDAO().getAll(id);
					for (Cell cell: cells){
						
						//Check whether the cell's value is one of the target ones
						boolean existsInList = false;
						for (String temp: params.getValues()){
							if (temp.equals(cell.getValue()) &&
									!selectedRecords.contains(db.getRecordsDAO().
											getRecord(cell.getRecord_id()))){
								existsInList = true;
								selectedRecords.add(db.getRecordsDAO().getRecord(cell.getRecord_id()));
							}
						}
						//If it is one of the target ones
						if (existsInList){
							Search_Result_Tuple tuple = new Search_Result_Tuple();
							tuple.setField_id(cell.getField_id());
							int batch_id = db.getRecordsDAO().getRecord(cell.getRecord_id())
																				.getBatch_id();
							tuple.setBatch_id(batch_id);
							tuple.setImageURL(db.getBatchesDAO().getBatch(batch_id).getDataPath());
							tuple.setRecordNum(db.getRecordsDAO().getRecord(cell.getRecord_id())
																				.getRowNum());
							tuples.add(tuple);
						}
					}
				}
			}
		}
		catch(DatabaseException e){
			db.endTransaction(false);
			throw new ServerException(e.getMessage(), e);
		}
		db.endTransaction(true);
		result.setMatches(tuples);
		
		return result;
	}
}
