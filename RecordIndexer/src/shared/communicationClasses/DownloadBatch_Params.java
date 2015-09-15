
package shared.communicationClasses;

/**
 * This class holds the information for the DownloadBatch method parameters.
 * It holds userName, userPassword, and targetProjectId
 * @author Yazan Halawa
 *
 */
public class DownloadBatch_Params {
	private String userName;
	private String userPassword;
	private int targetProjectId;
	/**
	 * The constructor for the class DownloadBatch_Params
	 */
	public DownloadBatch_Params() {
		userName = null;
		userPassword = null;
		targetProjectId = -1;
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
	 * @return the targetProjectId
	 */
	public int getTargetProjectId() {
		return targetProjectId;
	}
	/**
	 * @param targetProjectId the targetProjectId to set
	 */
	public void setTargetProjectId(int targetProjectId) {
		this.targetProjectId = targetProjectId;
	}

}
