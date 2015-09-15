
package client.facade;

import java.util.ArrayList;

import shared.modelClasses.*;
import antlr.collections.List;
import client.communicator;
import client.synchronization.BatchState;
import server.ServerException;
import server.database.Database;
import server.database.DatabaseException;
import shared.communicationClasses.*;

/**
 * This class contains all the methods of communicating between the client and the server
 * @author Yazan Halawa
 *
 */
public class clientFacade {
	
	// Init the variables
	private transient communicator clientCommunicator;
	private String username;
	private String password;
	private Database db;
	private int batchID;
	private boolean hasImageAssigned;
	
	/**
	 * The constructor of the clientFacade class
	 * @param host A string which represents the host address
	 * @param port A string which represents the port number
	 */
	public clientFacade(String host, String port) {
		clientCommunicator = new communicator(host, port);
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the clientCommunicator
	 */
	public communicator getClientCommunicator() {
		return clientCommunicator;
	}

	/**
	 * @param clientCommunicator the clientCommunicator to set
	 */
	public void setClientCommunicator(communicator clientCommunicator) {
		this.clientCommunicator = clientCommunicator;
	}
	
	/**
	 * This method accesses the ValidateUser method in the client Communicator class
	 * @param params a VaildateUser_Params object which contains the User's name
	 * and password.
	 * @return the user's information if he exists, else False if he does not exist, 
	 * else a failed string if the operation fails for any reason. 
	 * @throws ServerException if the operation fails for any reason
	 */
	public String validateUser(String userName, String userPassword){
		username = userName;
		password = userPassword;
		
		ValidateUser_Params params = new ValidateUser_Params();
		params.setUserName(username);
		params.setUserPassword(password);
		ValidateUser_Result results = new ValidateUser_Result();
		
		try {
			results = clientCommunicator.validateUser(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (results == null || results.toString().equals("False\n") ||
														results.toString().equals("Failed\n"))
			return "Failed";
		
		return "Welcome " + results.getFirstName() + " " + results.getLastName() +
							". You have indexed " + results.getNumOfRecords() + 
							" records\n";
	}

	public String[] getProjects(){
		// Grab projects
		GetProjects_Result results = grabProjectList();
		
		// Convert to Strings
		return convertToStrings(results);
	}

	/**
	 * @return
	 */
	public GetProjects_Result grabProjectList() {
		GetProjects_Params params= new GetProjects_Params();
		params.setUserName(username);
		params.setUserPassword(password);
		GetProjects_Result results = new GetProjects_Result();
		
		try {
			results = clientCommunicator.getProjects(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * @param results
	 * @return
	 */
	public String[] convertToStrings(GetProjects_Result results) {
		String[] listOfProjects = new String[results.getProjects().size()];
		int index = 0;
		for (Project proj: results.getProjects()){
			listOfProjects[index] = proj.getTitle();
			index++;
		}
		return listOfProjects;
	}
	
	/**
	 * This method gets a sample image from the project
	 * @param projectID the ID of the project 
	 * @return a String which represents the URL Path of the image
	 */
	public String getSampleImage(int projectID){
		GetSampleImage_Params params = new GetSampleImage_Params();
		params.setUserName(username);
		params.setUserPassword(password);
		params.setProject_id(projectID);
		GetSampleImage_Result results = new GetSampleImage_Result();
		
		try {
			results = clientCommunicator.getSampleImage(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String imageURL = results.getImageURL();
		return imageURL;
	}
	
	/**
	 * This method downloads the first available batch in the project
	 * @param projectID the ID of the target project
	 * @return a String which represents the URL Path of the image
	 */
	public String downloadImage(int projectID){
		DownloadBatch_Params params = new DownloadBatch_Params();
		DownloadBatch_Result results = new DownloadBatch_Result();
		params.setUserName(username);
		params.setUserPassword(password);
		params.setTargetProjectId(projectID);
		
		try {
			results = clientCommunicator.downloadBatch(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String imageURL = results.getMyBatch().getDataPath();
		batchID = results.getMyBatch().getId();
		return imageURL;

	}
	
	/**
	 * This method gets all the fields in the project
	 * @param projectID the ID of the target project
	 * @return a list of all the fields in the project
	 */
	public ArrayList<Field> getAllFields(int projectID){
		GetFields_Params params = new GetFields_Params();
		GetFields_Result results = new GetFields_Result();
		params.setUserName(username);
		params.setUserPassword(password);
		params.setProject_id(projectID);
		
		try {
			results = clientCommunicator.getFields(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results.getFields();
	}
	
	/**
	 * This method will grab a specific project from the list of projects
	 * @param projectID the ID of the target project
	 * @return the target Project
	 */
	public Project getProject(int projectID){
		// Grab all projects
		GetProjects_Result results = grabProjectList();
		
		// Get specific project
		return results.getProjects().get(projectID-1);
	}
	
	/**
	 * This method will submit the batch and correctly store the information in
	 * the data base
	 */
	public void SubmitBatch(){
		SubmitBatch_Params params = new SubmitBatch_Params();
		params.setUserName(username);
		params.setUserPassword(password);
		params.setBatch_id(batchID);
		ArrayList<ArrayList<String>> cellValues = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < BatchState.getValues().length; i++){
			ArrayList<String> record = new ArrayList<String>();
			for (int j = 0; j < BatchState.getValues()[i].length; j++){
				record.add(BatchState.getValues()[i][j]);
			}
			cellValues.add(record);
		}
		params.setFieldValues(cellValues);
		
		try {
			clientCommunicator.submitBatch(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
