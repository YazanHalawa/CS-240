/**
 * 
 */
package shared.communicationClasses;

/**
 * This class holds the information for the GetProjects method parameters
 * It holds the userName and userPassword
 * @author Yazan Halawa
 *
 */
public class GetProjects_Params {
	private String userName;
	private String userPassword;
	
	/**
	 * The constructor for the GetProjects_Params class
	 */
	public GetProjects_Params() {
		userName = null;
		userPassword = null;
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

}
