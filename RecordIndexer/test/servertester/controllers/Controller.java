package servertester.controllers;

import java.util.*;

import shared.communicationClasses.*;
import servertester.views.IView;
import client.communicator;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		//Get the host and port
		communicator comm = new communicator(_view.getHost(), _view.getPort());
		
		//Set the parameters
		ValidateUser_Params params = new ValidateUser_Params();
		String result;
		String[] paramValues = _view.getParameterValues();
		
		params.setUserName(paramValues[0]);
		params.setUserPassword(paramValues[1]);
		
		//Get the response
		try {
			result = comm.validateUser(params).toString();
		} catch (Exception e) {
			result = "Failed\n";
			e.printStackTrace();
		}
		_view.setResponse(result);
	}
	
	private void getProjects() {
		//Get the host and port
		communicator comm = new communicator(_view.getHost(), _view.getPort());
		
		//Set the parameters
		GetProjects_Params params = new GetProjects_Params();
		String result;
		String[] paramValues = _view.getParameterValues();
		
		params.setUserName(paramValues[0]);
		params.setUserPassword(paramValues[1]);
		
		//Get the response
		try{
			result = comm.getProjects(params).toString();
		}
		catch(Exception e){
			result = "Failed\n";
			e.printStackTrace();
		}
		_view.setResponse(result);
	}
	
	private void getSampleImage() {
		//Get the host and port
		communicator comm = new communicator(_view.getHost(), _view.getPort());
		
		//Set the parameters
		GetSampleImage_Params params = new GetSampleImage_Params();
		String result;
		String[] paramValues = _view.getParameterValues();
		
		params.setUserName(paramValues[0]);
		params.setUserPassword(paramValues[1]);
		params.setProject_id(Integer.parseInt(paramValues[2]));
		
		//Get the response
		try{
			result = comm.getSampleImage(params).toString();
		}
		catch(Exception e){
			result = "Failed\n";
			e.printStackTrace();
		}
		_view.setResponse(result);
	}
	
	private void downloadBatch() {
		//Get the host and port
		communicator comm = new communicator(_view.getHost(), _view.getPort());
		
		//Set the parameters
		DownloadBatch_Params params = new DownloadBatch_Params();
		String result;
		String[] paramValues = _view.getParameterValues();
		
		params.setUserName(paramValues[0]);
		params.setUserPassword(paramValues[1]);
		params.setTargetProjectId(Integer.parseInt(paramValues[2]));
		
		//Get the response
		try{
			result = comm.downloadBatch(params).toString();
		}
		catch(Exception e){
			result = "Failed\n";
			e.printStackTrace();
		}
		_view.setResponse(result);
	}
	
	private void getFields() {
		//Get the host and port
		communicator comm = new communicator(_view.getHost(), _view.getPort());
		
		//Set the parameters
		GetFields_Params params = new GetFields_Params();
		String result;
		String[] paramValues = _view.getParameterValues();
		
		params.setUserName(paramValues[0]);
		params.setUserPassword(paramValues[1]);
		if (!paramValues[2].equals("")){
			params.setProject_id(Integer.parseInt(paramValues[2]));
		}
		else
			params.setProject_id(-1);
		
		//Get the response
		try{
			result = comm.getFields(params).toString();
		}
		catch(Exception e){
			result = "Failed\n";
			e.printStackTrace();
		}
		_view.setResponse(result);
	}
	
	private void submitBatch() {
		//Get the host and port
		communicator comm = new communicator(_view.getHost(), _view.getPort());
		
		//Set the parameters
		SubmitBatch_Params params = new SubmitBatch_Params();
		String result;
		String[] paramValues = _view.getParameterValues();
		
		params.setUserName(paramValues[0]);
		params.setUserPassword(paramValues[1]);
		params.setBatch_id(Integer.parseInt(paramValues[2]));
		ArrayList<ArrayList<String>> cells = parseFieldString(paramValues[3]);
		params.setFieldValues(cells);
		
		//Get the response
		try{
			result = comm.submitBatch(params).toString();
		}
		catch(Exception e){
			result = "Failed\n";
			e.printStackTrace();
		}
		_view.setResponse(result);
	}
	
	/**
	 * This method will parse the Field values String into a 2d ArrayList of Strings
	 * @param string the field values
	 * @return a 2d arraylist of strings
	 */
	private ArrayList<ArrayList<String>> parseFieldString(String string) {
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		StringBuilder cell = new StringBuilder();
		ArrayList<String> tempValues = new ArrayList<String>();
		
		char [] charArray = string.toCharArray();
		
		for (int i = 0; i < charArray.length; i++){
			if (charArray[i] == ','){//If the end of a word
				tempValues.add(cell.toString());
				cell.delete(0, cell.length());
			}
			else if (charArray[i] == ';'){//If the end of a row
				tempValues.add(cell.toString());
				values.add(new ArrayList<String>(tempValues));
				tempValues.clear();
				cell.delete(0, cell.length());
			}
			else{
				cell.append(charArray[i]);
			}
		}
		//add the last values to the array
		tempValues.add(cell.toString());
		values.add(new ArrayList<String>(tempValues));
		
		return values;
	}

	private void search() {
		//Get the host and port
		communicator comm = new communicator(_view.getHost(), _view.getPort());
		
		//Set the parameters
		Search_Params params = new Search_Params();
		String result;
		String[] paramValues = _view.getParameterValues();
		
		params.setUserName(paramValues[0]);
		params.setUserPassword(paramValues[1]);
		
		String fieldIDs = paramValues[2];
		ArrayList<Integer> fieldValues = parsefieldIDs(fieldIDs);
		params.setFields(fieldValues);
		
		String values = paramValues[3];
		ArrayList<String> searchValues = parseSearchValues(values);
		params.setValues(searchValues);
		
		//Get the response
		try{
			result = comm.search(params).toString();
		}
		catch(Exception e){
			result = "Failed\n";
			e.printStackTrace();
		}
		_view.setResponse(result);
	}

	/**
	 * This method will parse the search values string into an ArrayList of strings
	 * @param values the String of search key words
	 * @return an ArrayList of Strings which represents the words to search for
	 */
	private ArrayList<String> parseSearchValues(String values) {
		ArrayList<String> returnArray = new ArrayList<String>();
		StringBuilder tempWord = new StringBuilder();
		
		char [] charArray = values.toCharArray();
		
		for (int i = 0; i < charArray.length; i++){
			if (charArray[i] != ',')//If the end of a word
				tempWord.append(charArray[i]);
			else{
				returnArray.add(tempWord.toString());
				tempWord.delete(0, tempWord.length());
			}
		}
		//Add the last word
		returnArray.add(tempWord.toString());
		
		return returnArray;
	}

	/**
	 * This method will parse the fieldIDs string into an ArrayList of Integers
	 * @param fieldIDs the String of field IDs
	 * @return an ArrayList of Integers which represents the field IDs to search in
	 */
	private ArrayList<Integer> parsefieldIDs(String fieldIDs) {
		ArrayList<Integer> returnArray = new ArrayList<Integer>();
		StringBuilder tempWord = new StringBuilder();
		
		char [] charArray = fieldIDs.toCharArray();
		
		for (int i = 0; i < charArray.length; i++){
			if (charArray[i] != ',')//If the end of a word
				tempWord.append(charArray[i]);
			else{
				returnArray.add(Integer.parseInt(tempWord.toString()));
				tempWord.delete(0, tempWord.length());
			}
		}
		//Add the last field ID
		returnArray.add(Integer.parseInt(tempWord.toString()));
		
		return returnArray;
	}

}

