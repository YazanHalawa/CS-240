
package shared.modelClasses;

/**
 * This class will store all the information about the Cells in the data base
 * 
 * @author Yazan Halawa
 */
public class Cell {
	private int id;
	private int record_id;
	private int field_id;
	private String value;
	
	/**
	 * The constructor for the Cell class
	 * @param value 
	 * @param field_id 
	 * @param record_id 
	 * @param id 
	 */
	public Cell(int id, int record_id, int field_id, String value) {
		this.id = id;
		this.record_id = record_id;
		this.field_id = field_id;
		this.value = value;
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
	 * @return the record_id
	 */
	public int getRecord_id() {
		return record_id;
	}
	/**
	 * @param record_id the record_id to set
	 */
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
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
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
