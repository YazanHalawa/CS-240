
package shared.modelClasses;

/**
 * This class will store all the information about the Users in the data base
 * 
 * @author Yazan Halawa
 */
public class User {
	private int id;
	private String password;
	private String userName;
	private String lastName;
	private String firstName;
	private String email;
	private int indexedNum;
	private int currentBatchID;
	
	/**
	 * The constructor for the User class
	 */
	public User(int id, String password, String userName, String firstName,
				String lastName, String email, int indexedNum, int currentBatchID) {
		this.id = id;
		this.password = password;
		this.userName = userName;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.indexedNum = indexedNum;
		this.currentBatchID = currentBatchID;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the indexedNum
	 */
	public int getIndexedNum() {
		return indexedNum;
	}
	/**
	 * @param indexedNum the indexedNum to set
	 */
	public void setIndexedNum(int indexedNum) {
		this.indexedNum = indexedNum;
	}
	/**
	 * @return the currentBatchID
	 */
	public int getCurrentBatchID() {
		return currentBatchID;
	}
	/**
	 * @param currentBatchID the currentBatchID to set
	 */
	public void setCurrentBatchID(int currentBatchID) {
		this.currentBatchID = currentBatchID;
	}

}
