
package shared.communicationClasses;

/**
 * This method holds the information for the SubmitBatch method results
 * It holds a boolean to show if the result was a success or not
 * @author Yazan Halawa 
 *
 */
public class SubmitBatch_Result {
	private boolean Success;
	
	/**
	 * The constructor for the SubmitBatch_Result class
	 */
	public SubmitBatch_Result() {
		Success = false;
	}
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return Success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		Success = success;
	}
	
	@Override
	public String toString(){
		if (Success == true)
			return "TRUE\n";	
		else
			return "FAILED\n";
	}

}
