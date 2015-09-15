
package shared.modelClasses;

/**
 * This class will store all the information about the images in the data base
 * 
 * @author Yazan Halawa
 */
public class Batch {
	private int id;
	private int project_id;
	private String dataPath;
	private boolean available;
	
	/**
	 * The constructor for the Batch class
	 * @param available 
	 * @param dataPath 
	 * @param project_id 
	 * @param id 
	 */
	public Batch(int id, int project_id, String dataPath, boolean available) {
		this.id = id;
		this.project_id = project_id;
		this.dataPath = dataPath;
		this.available = available;
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
	 * @return the dataPath
	 */
	public String getDataPath() {
		return dataPath;
	}
	/**
	 * @param dataPath the dataPath to set
	 */
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}
	/**
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}
	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

}
