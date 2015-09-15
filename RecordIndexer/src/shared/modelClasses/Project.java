package shared.modelClasses;

/**
 * This class will store all the information about the Projects in the data base
 * 
 * @author Yazan Halawa 
 */
public class Project {
	private int id;
	private String title;
	private int recordsForImage;
	private int firstYCoord;
	private int recordsHeight;
	
	/**
	 * The constructor for the Project class
	 */
	public Project(int id, String title, int recordsForImage, int firstYCoordinate, int recordsHeight) {
		this.id = id;
		this.title = title;
		this.recordsForImage = recordsForImage;
		this.firstYCoord = firstYCoordinate;
		this.recordsHeight = recordsHeight;
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
	 * @return the recordsForImage
	 */
	public int getRecordsForImage() {
		return recordsForImage;
	}

	/**
	 * @param recordsForImage the recordsForImage to set
	 */
	public void setRecordsForImage(int recordsForImage) {
		this.recordsForImage = recordsForImage;
	}

	/**
	 * @return the firstYCoord
	 */
	public int getFirstYCoord() {
		return firstYCoord;
	}

	/**
	 * @param firstYCoord the firstYCoord to set
	 */
	public void setFirstYCoord(int firstYCoord) {
		this.firstYCoord = firstYCoord;
	}

	/**
	 * @return the recordsHeight
	 */
	public int getRecordsHeight() {
		return recordsHeight;
	}

	/**
	 * @param recordsHeight the recordsHeight to set
	 */
	public void setRecordsHeight(int recordsHeight) {
		this.recordsHeight = recordsHeight;
	}

}
