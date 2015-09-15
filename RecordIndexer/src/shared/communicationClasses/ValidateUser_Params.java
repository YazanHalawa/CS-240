
package shared.communicationClasses;

/**
 * This method holds the information for the ValidateUser method parameters
 * It holds the userName, and userPassword
 * @author Yazan Halawa
 *
 */
public class ValidateUser_Params {
	private String userName;
	private String userPassword;
	/**
	 * The constructor for the ValidateUser_Params class
	 */
	public ValidateUser_Params() {
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
