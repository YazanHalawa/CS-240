
package shared.modelClasses;

/**
 * This class will store all the information about the Records in the data base
 * 
 * @author Yazan Halawa
 */
public class Record {
	private int id;
	private int batch_id;
	private int rowNum;
	
	/**
	 * The constructor for the Record class
	 * @param rowNum 
	 * @param batch_id 
	 * @param id 
	 */
	public Record(int id, int batch_id, int rowNum) {
		this.id = id;
		this.batch_id = batch_id;
		this.rowNum = rowNum;
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
	 * @return the rowNum
	 */
	public int getRowNum() {
		return rowNum;
	}
	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

}
