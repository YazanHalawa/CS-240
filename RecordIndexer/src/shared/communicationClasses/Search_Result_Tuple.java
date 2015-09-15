
package shared.communicationClasses;

/**
 * This class holds the batch_id, imageURL, recordNum, and field_id
 * @author Yazan Halawa
 *
 */
public class Search_Result_Tuple {
	private int batch_id;
	private String imageURL;
	private int recordNum;
	private int field_id;
	private String urlBase;
	
	/**
	 * constructor for the class
	 */
	public Search_Result_Tuple() {
		batch_id = -1;
		imageURL = null;
		recordNum = -1;
		field_id = -1;
		urlBase = null;
	}
	
	/**
	 * @return the urlBase
	 */
	public String getUrlBase() {
		return urlBase;
	}

	/**
	 * @param urlBase the urlBase to set
	 */
	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
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
	/**
	 * @return the recordNum
	 */
	public int getRecordNum() {
		return recordNum;
	}
	/**
	 * @param recordNum the recordNum to set
	 */
	public void setRecordNum(int recordNum) {
		this.recordNum = recordNum;
	}
	/**
	 * @return the field_id
	 */
	public int getField_id() {
		return field_id;
	}
	/**
	 * @param field_id the field_id to set
	 */
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	@Override
	public String toString(){
		StringBuilder output = new StringBuilder();
		if (batch_id != -1){
			output.append(batch_id + "\n" + imageURL + "\n" +
							recordNum + "\n" + field_id + "\n");
		}
		else{
			return "Failed\n";
		}
		return output.toString();
	}
}
