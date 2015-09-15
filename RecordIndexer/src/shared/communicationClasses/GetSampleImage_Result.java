
package shared.communicationClasses;

/**
 * This method holds the information for the GetSampleImage method results
 * It holds the imageURL
 * @author Yazan Halawa
 *
 */
public class GetSampleImage_Result {
	private String imageURL;
	
	/**
	 * The constructor for the GetSampleImage_Results class
	 */
	public GetSampleImage_Result() {
		imageURL = null;
	}
	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}
	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	@Override
	public String toString(){
		StringBuilder output = new StringBuilder();
		if (imageURL != null){
			output.append(imageURL + "\n");
		}
		else{
			return "Failed\n";
		}
		return output.toString();
	}
}
