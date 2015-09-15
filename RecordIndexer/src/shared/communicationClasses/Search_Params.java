
package shared.communicationClasses;

import java.util.ArrayList;

/**
 * This method holds the information for the Search method parameters
 * It holds the userName, userPassword, an array of fields IDs, and an array of values
 * @author Yazan Halawa
 *
 */
public class Search_Params {
	private String userName;
	private String userPassword;
	private ArrayList<Integer> fields;
	private ArrayList<String> values;
	/**
	 * The constructor for the Search_Params class
	 */
	public Search_Params() {
		userName = null;
		userPassword = null;
		fields = null;
		values = null;
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
	 * @return the fields
	 */
	public ArrayList<Integer> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Integer> fields) {
		this.fields = fields;
	}
	/**
	 * @return the values
	 */
	public ArrayList<String> getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

}
