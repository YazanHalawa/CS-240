
package shared.communicationClasses;

/**
 * This class holds the information for the ValidateUser method results
 * It holds the lastName, firstName, and numberofRecords
 * @author Yazan Halawa
 *
 */
public class ValidateUser_Result {
	private String lastName;
	private String firstName;
	private int numOfRecords;
	
	/**
	 * The constructor for the ValidateUser_Result class
	 */
	public ValidateUser_Result() {
		lastName = null;
		firstName = null;
		numOfRecords = -1;
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
	 * @return the numOfRecords
	 */
	public int getNumOfRecords() {
		return numOfRecords;
	}
	/**
	 * @param numOfRecords the numOfRecords to set
	 */
	public void setNumOfRecords(int numOfRecords) {
		this.numOfRecords = numOfRecords;
	}

	@Override
	public String toString(){
		StringBuilder output = new StringBuilder();
		if (firstName != null && lastName != null && numOfRecords != -1){
			output.append("True\n" + firstName + "\n" + 
						  lastName + "\n" + numOfRecords + "\n");
		}
		else{
			output.append("False\n");
		}
		return output.toString();
	}
}
