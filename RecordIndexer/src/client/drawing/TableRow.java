package client.drawing;


import java.awt.*;

/**
 * This class holds the information of a table row
 * @author Yazan
 *
 */
public class TableRow {

	private String name;

	
	public TableRow(String name) {
		setName(name);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
