package client.drawing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import client.synchronization.BatchState;
import client.synchronization.Cell;


/**
 * This class contains all the method related to rendering a Table cell
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
class ColorCellRenderer extends JLabel implements TableCellRenderer {

	// Init variables
	private Border unselectedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
	private Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
	private ColorTable table;
	
	/**
	 * The constructor for the ColorCellRenderer class
	 * @param table
	 */
	public ColorCellRenderer(ColorTable table) {
		this.table = table;
		setOpaque(true);
		setFont(getFont().deriveFont(12.0f));
	}

	/**
	 * This method handles rendering the cell
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
		this.setBackground(Color.WHITE);

		if (isSelected) {
			if (column != 0){// If a cell is selected, notify the Batch State
				BatchState.setCaller(this.table);
				BatchState.setSelectedCell(new Cell(row, column-1));
			}
			this.setBorder(selectedBorder);
			this.setBackground(Color.BLUE);
		}
		else {
			this.setBorder(unselectedBorder);
		}
		
		// If the cell's value is not in the known values and it should be highlighted red, do so
		if (BatchState.isRedHighlightedCell(new Cell(row, column-1))){
			this.setBackground(Color.RED);
		}
		
		this.setText((String)value);
		
		return this;
	}
}
