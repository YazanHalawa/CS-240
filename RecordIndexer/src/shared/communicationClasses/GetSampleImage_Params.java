
package shared.communicationClasses;

/**
 * This method holds the information for the GetSampleImage method parameters
 * It holds the userName, userPassword, and project_id
 * @author Yazan Halawa
 *
 */
public class GetSampleImage_Params {
	private String userName;
	private String userPassword;
	private int project_id;
	
	/**
	 * The constructor for the GetSampleImage class
	 */
	public GetSampleImage_Params() {
		userName = null;
		userPassword = null;
		project_id = -1;
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
	 * @return the project_id
	 */
	public int getProject_id() {
		return project_id;
	}
	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}


}
