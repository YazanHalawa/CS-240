
package client.synchronization;

/**
 * This is the interface for the Batch State
 * @author Yazan Halawa
 *
 */
public interface BatchStateListener {
	
	/**
	 * This method changes the value of the cell to the newly passed value
	 * @param cell the cell whose value is to be changed
	 * @param newValue the new value to which the cell was changed
	 */
	public void valueChanged(Cell cell, String newValue);
	
	/**
	 * This method changes the selected cell to the cell that is passed
	 * @param newSelectedCell the new selected cell
	 */
	public void selectedCellChanged(Cell newSelectedCell);

	/**
	 * This method highlights the Cell Red
	 * @param j 
	 * @param i 
	 */
	public void highlightRed(int i, int j);

	/**
	 * This method unhighlights the Cell red
	 * @param j 
	 * @param i 
	 */
	public void unhighlightRed(int i, int j);

}
