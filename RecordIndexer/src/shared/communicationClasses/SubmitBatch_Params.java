
package shared.communicationClasses;

import java.util.ArrayList;

/**
 * This class holds the information for the SubmitBatch method parameters
 * It holds the userName, userPassword, and a 2d Array of fieldValues
 * @author Yazan Halawa
 *
 */
public class SubmitBatch_Params {
	private String userName;
	private String userPassword;
	private int batch_id;
	private ArrayList<ArrayList<String>> fieldValues;
	
	/**
	 * The constructor for the SubmitBatch_Params class
	 */
	public SubmitBatch_Params() {
		userName = null;
		userPassword = null;
		batch_id = -1;
		fieldValues = null;
	}
	/**
	 * @return the batch_id
	 */
	public int getBatch_id() {
		return batch_id;
	}
	/**
	 * @param batch_id the batch_id to set
	 */
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the userPassword
	 */
	public String getUserPassword() {
		return userPassword;
	}
	/**
	 * @param userPassword the userPassword to set
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	/**
	 * @return the fieldValues
	 */
	public ArrayList<ArrayList<String>> getFieldValues() {
		return fieldValues;
	}
	/**
	 * @param fieldValues the fieldValues to set
	 */
	public void setFieldValues(ArrayList<ArrayList<String>> fieldValues) {
		this.fieldValues = fieldValues;
	}

}
