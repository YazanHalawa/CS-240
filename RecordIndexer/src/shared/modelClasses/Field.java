
package shared.modelClasses;

/**
 * This class will store all the information about the Fields in the data base
 * 
 * @author Yazan Halawa
 */
public class Field {
	private int id;
	private String title;
	private int project_id;
	private int width;
	private int columnNum;
	private int firstXCoord;
	private String helpHtml;
	private String knownData;
	
	/**
	 * The constructor for the Field class
	 * @param knownData
	 * @param helpHtml
	 * @param firstXCoordinate 
	 * @param columnNum 
	 * @param width 
	 * @param project_id 
	 * @param title 
	 * @param id 
	 */
	public Field(int id, String title, int project_id, int width, int columnNum, int firstXCoordinate,
				String helpHtml, String knownData) {
		this.id = id;
		this.title = title;
		this.project_id = project_id;
		this.width = width;
		this.columnNum = columnNum;
		this.firstXCoord = firstXCoordinate;
		this.helpHtml = helpHtml;
		this.knownData = knownData;
	}
	/**
	 * @return the helpHtml
	 */
	public String getHelpHtml() {
		return helpHtml;
	}
	/**
	 * @param helpHtml the helpHtml to set
	 */
	public void setHelpHtml(String helpHtml) {
		this.helpHtml = helpHtml;
	}
	/**
	 * @return the knownData
	 */
	public String getKnownData() {
		return knownData;
	}
	/**
	 * @param knownData the knownData to set
	 */
	public void setKnownData(String knownData) {
		this.knownData = knownData;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the project_id
	 */
	public int getProject_id() {
		return project_id;
	}
	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return the columnNum
	 */
	public int getColumnNum() {
		return columnNum;
	}
	/**
	 * @param columnNum the columnNum to set
	 */
	public void setColumnNum(int columnNum) {
		this.columnNum = columnNum;
	}
	/**
	 * @return the firstXCoord
	 */
	public int getFirstXCoord() {
		return firstXCoord;
	}
	/**
	 * @param firstXCoord the firstXCoord to set
	 */
	public void setFirstXCoord(int firstXCoord) {
		this.firstXCoord = firstXCoord;
	}

}
