
package shared.communicationClasses;

import java.util.ArrayList;
import shared.modelClasses.*;

/**
 * This class holds the information for the Download Batch method's result
 * It holds the batch_id, the project_id, imageURL, firstYCoord, recordHeight, numofRecords,
 * and numOfFields, and fields
 * @author Yazan Halawa
 *
 */
public class DownloadBatch_Result {
	private Batch myBatch;
	private Project myProj;
	ArrayList<Field> fields;
	private String urlBase;
	
	/**
	 * The constructor for the DownloadBatch_Result class
	 */
	public DownloadBatch_Result() {
		myBatch = null;
		myProj = null;
		fields = null;
		urlBase = null;
	}
	
	/**
	 * @return the myBatch
	 */
	public Batch getMyBatch() {
		return myBatch;
	}
	/**
	 * @param myBatch the myBatch to set
	 */
	public void setMyBatch(Batch myBatch) {
		this.myBatch = myBatch;
	}
	/**
	 * @return the myProj
	 */
	public Project getMyProj() {
		return myProj;
	}
	/**
	 * @param myProj the myProj to set
	 */
	public void setMyProj(Project myProj) {
		this.myProj = myProj;
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
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	@Override
	public String toString(){
		StringBuilder output = new StringBuilder();
		if (myBatch != null){
			output.append(myBatch.getId() + "\n" + myProj.getId() + "\n" +
							myBatch.getDataPath() + "\n" + myProj.getFirstYCoord() + "\n" +
							myProj.getRecordsHeight() + "\n" + myProj.getRecordsForImage() + "\n" +
							fields.size() + "\n");	
			if (fields != null){
				for (Field field: fields){
					output.append(field.getId() + "\n" + field.getColumnNum() + "\n" +
									field.getTitle() + "\n" + urlBase + field.getHelpHtml() + "\n" + 
									field.getFirstXCoord() + "\n" + field.getWidth() + "\n");
					if (field.getKnownData() != null){
						output.append(urlBase + field.getKnownData() + "\n");
					}
				}
			}
			else{
				return "Failed\n";
			}
		}
		else{
			return "Failed\n";
		}
		return output.toString();
	}
}
