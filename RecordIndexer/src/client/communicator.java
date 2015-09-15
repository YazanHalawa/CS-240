package client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.communicationClasses.*;

/**
 * This class communicates with the server and perform operations
 * requested by the client
 * 
 * @author Yazan Halawa
 */
public class communicator {

	private String HOST_NUMBER;
	private String PORT_NUMBER;
	private String urlBase;
	/**
	 * The constructor for the client.communicator class
	 */
	public communicator(String host, String port) {
		HOST_NUMBER = host;
		PORT_NUMBER = port;
		StringBuilder myBuilder = new StringBuilder();
		myBuilder.append("http://" + HOST_NUMBER + ":" + PORT_NUMBER);
		urlBase = myBuilder.toString();
	}
	

	/**
	 * This method accesses the Users table and uses the select operation
	 * on the data base to check if the user exists and grab its information 
	 * if it does
	 * @param params a VaildateUser_Params object which contains the User's name
	 * and password.
	 * @return the user's information if he exists, else False if he does not exist, 
	 * else a failed string if the operation fails for any reason. 
	 * @throws IOException if the operation fails for any reason
	 */
	public ValidateUser_Result validateUser(ValidateUser_Params params) throws Exception {
		return (ValidateUser_Result) post("/validateUser", params);
	}
	
	/**
	 * This method validates the user first then accesses the projects table 
	 * in the data base and gets information about all available projects.
	 * @param params a GetProjects_Params object which contains the user name and password
	 * @return the title and id for every available project, else returns a failed string if the operation fails
	 * @throws IOException if the operation fails for any reason
	 */
	public GetProjects_Result getProjects(GetProjects_Params params) throws Exception{
		return (GetProjects_Result) post("/getProjects", params);	
	}
	
	/**
	 * This method validates the user first then accesses the batches table
	 * and finds the matching project Id and selects the first batch
	 * @param params a GetSampleImage_Params object which contains the user name 
	 * and password and the target project id
	 * @return the batch's URL, else a failed string if the operation fails for any reason
	 * @throws IOException if the operation fails for any reason
	 */
	public GetSampleImage_Result getSampleImage(GetSampleImage_Params params) throws Exception{
		GetSampleImage_Result result = new GetSampleImage_Result();
		result = (GetSampleImage_Result) post("/getSampleImage", params);
		
		if (result == null)
			result = new GetSampleImage_Result();
		
		if (result.getImageURL() != null)//Add the URL base to the image URL
			result.setImageURL(("http://" + HOST_NUMBER + ":" + PORT_NUMBER + "/") + result.getImageURL());
		
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
	 * @throws IOException if the operation fails for any reason
	 */
	public DownloadBatch_Result downloadBatch(DownloadBatch_Params params) throws Exception{
		DownloadBatch_Result result =  (DownloadBatch_Result) post("/downloadBatch", params);
		
		if (result == null)
			result = new DownloadBatch_Result();
		
		if (result.getMyBatch().getDataPath() != null){//Add the URL base to the image URL
			result.setUrlBase("http://" + HOST_NUMBER + ":" + PORT_NUMBER + "/");
			result.getMyBatch().setDataPath(result.getUrlBase() + result.getMyBatch().getDataPath());
		}
		
		return result;
	}
	
	/**
	 * This method validates the user first then accesses the users table
	 * and unassigns the user from the submitted batch by incrementing the number of records 
	 * indexed and updating the currentBatchID to the default value.
	 * @param params a SubmitBatch_id object which contains the user's name and password and
	 * batch ID and a 2d Array of strings which contain the field values for the batch.
	 * @return True if the operations succeeds, else a failed string
	 * @throws IOException if the operation fails for any reason
	 */
	public SubmitBatch_Result submitBatch(SubmitBatch_Params params) throws Exception{
		return (SubmitBatch_Result) post("/submitBatch", params);
	}
	
	/**
	 * This method validates the user first then accesses the fields table
	 * and returns the information about the fields relating to the specified project. If
	 * not project is specified, returns information about all of the fields for all 
	 * projects in the system.
	 * @param params a GetField_Params object which contains the user name
	 * and password and the project id
	 * @return the fields and their information, else a failed string if the operation fails for any reason
	 * @throws IOException if the operation fails for any reason
	 */
	public GetFields_Result getFields(GetFields_Params params) throws Exception{
		return (GetFields_Result) post("/getFields", params);
	}
	
	/**
	 * This method validates the user first then searches the indexed records for the specified strings
	 * in the specified fields. This is done by joining all the tables in the data base and accessing the resulting table
	 * @param params a Search_Params which contains the user name and password and a 
	 * array of Strings which contains the fields, and another array of Strings which contains the 
	 * the values to search for
	 * @return the batch id, image URL, record number and field id relating to the search, else
	 * a failed string if the operation fails for any reason.
	 * @throws IOException if the operation fails for any reason
	 */
	public Search_Result search(Search_Params params) throws Exception{
		Search_Result result = (Search_Result) post("/search", params);
		
		if (result != null){
			for (Search_Result_Tuple tuple: result.getMatches()){//Add the URL base to the image URL
				tuple.setImageURL(urlBase + "/" + tuple.getImageURL());
			}
		}
		
		return result;
	}
	
	/**
	 * @return the urlBase
	 */
	public String getUrlBase() {
		return urlBase;
	}


	/**
	 * @param urlBase the urlBase to set
	 */
	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}


	/**
	 * This method performs HTTP communication
	 * @param urlPath the url path to the target method
	 * @param data the parameters to the target method
	 * @return an Object which the result of the target method
	 * @throws Exception if the operation fails for any reason
	 */
	public Object post(String urlPath, Object data) throws Exception{
		XStream xs = new XStream (new DomDriver());
		URL url = new URL(urlBase + urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();
		
		xs.toXML(data, conn.getOutputStream());
		
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
			Object result = xs.fromXML(conn.getInputStream());
			return result;
		}
		
		return null;
	}

}
