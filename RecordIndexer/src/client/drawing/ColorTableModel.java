package client.drawing;

import shared.modelClasses.Field;

import java.util.ArrayList;

import javax.swing.table.*;

import client.synchronization.BatchState;
import client.synchronization.Cell;

/**
 * This class contains all the methods for managing a table model
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
public class ColorTableModel extends AbstractTableModel {
	
	// Init variables
	private java.util.List<TableRow> rows;
	private ArrayList<Field> fields;
	private ColorTable table;

	/**
	 * The constructgor for the ColorTableModel class
	 * @param table
	 * @param rows
	 * @param fields
	 */
	public ColorTableModel(ColorTable table, java.util.List<TableRow> rows, ArrayList<Field> fields) {
		// Set the variables
		this.rows = rows;
		this.fields = fields;
		this.table = table;
	}

	/**
	 * This method returns the column count for the table
	 */
	@Override
	public int getColumnCount() {
		return fields.size()+1;
	}

	/**
	 * This method returns the target column's name
	 */
	@Override
	public String getColumnName(int column) {

		String result = null;
		if (column == 0)
			result = "Record Number";
		else if (column >= 1 && column < getColumnCount()) {
			result = fields.get(column-1).getTitle();

		} else {
			throw new IndexOutOfBoundsException();
		}

		return result;
	}

	/**
	 * This method returns the row count
	 */
	@Override
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * This method decides whether a cell is editable or not
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 0)
			return false;
		else
			return true;
	}

	/**
	 * This method returns the value at the specified cell
	 * @param the row number of the target cell
	 * @param the column number of the target cell
	 */
	@Override
	public Object getValueAt(int row, int column) {

		Object result = null;

		if (row >= 0 && row < getRowCount() && column >= 0
				&& column < getColumnCount()) {

			if (column == 0)//If this is the ID column
				result = String.valueOf(row+1);	
			else{
				result = BatchState.getValues()[row][column-1];
			}	
		} else {
			throw new IndexOutOfBoundsException();
		}

		return result;
	}

	/**
	 * This method sets the value at the specified cell
	 * @param the new value
	 * @param the row number of the target cell
	 * @param the column number of the target cell
	 */
	@Override
	public void setValueAt(Object value, int row, int column) {

		if (row >= 0 && row < getRowCount() && column >= 1
				&& column < getColumnCount()) {

			if ((BatchState.getValues()[row][column-1] == null || 
					!BatchState.getValues()[row][column-1].equals((String)value))){
				BatchState.setCaller(table);
				BatchState.setValue(new Cell(row, column-1), (String)value);
			}

			this.fireTableCellUpdated(row, column);

		} else {
			throw new IndexOutOfBoundsException();
		}		
	}

}