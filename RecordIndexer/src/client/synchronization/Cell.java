package client.synchronization;

/**
 * This class holds the information of a Cell
 * @author Yazan
 *
 */
public class Cell{
	
	// Init variables
	private int record;
	private int field;

	/**
	 * The constructor for the Cell class
	 * @param record
	 * @param field
	 */
	public Cell(int record, int field) {
		this.record = record;
		this.field = field;
	}

	/**
	 * @return the record
	 */
	public int getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(int record) {
		this.record = record;
	}

	/**
	 * @return the field
	 */
	public int getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(int field) {
		this.field = field;
	}
	

}
