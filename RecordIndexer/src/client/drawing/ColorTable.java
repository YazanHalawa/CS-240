package client.drawing;

import shared.modelClasses.*; 

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import client.synchronization.BatchState;
import client.synchronization.BatchStateListener;
import client.synchronization.Cell;
import client.windows.SuggestionsWindow;

import java.util.*;

/**
 * This class contains all the methods to handle displaying the table of values
 * @author Yazan
 *
 */
@SuppressWarnings("serial")
public class ColorTable extends JPanel implements BatchStateListener{

	// Init variables
	private ArrayList<TableRow> rows;
	private ArrayList<Field> fields;
	private TableColumnModel columnModel;
	private ColorTableModel tableModel;
	private JTable table;
	private boolean highlightRed;
	private int rowNum;
	private int columnNum;

	/**
	 * This constructor for the ColorTable class
	 * @param title
	 * @param fields
	 * @param numOfRecords
	 * @throws HeadlessException
	 */
	public ColorTable(String title, ArrayList<Field> fields, int numOfRecords) throws HeadlessException {
		// Set the private variables
		highlightRed = false;
		BatchState.getListeners().add(this);
		this.fields = fields;
		rows = generateSchemes(numOfRecords);
		tableModel = new ColorTableModel(this, rows, fields);

		// Set the layout for the table
		table = new JTable(tableModel);
		table.setRowHeight(21);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.addMouseListener(mouseAdapter);

		// Set the column headers for the table
		columnModel = table.getColumnModel();		
		for (int i = 0; i < tableModel.getColumnCount(); i++) {
			TableColumn column = columnModel.getColumn(i);
			if (i==0)
				column.setHeaderValue("Record Number");
			else
				column.setHeaderValue(fields.get(i-1).getTitle());
			column.setPreferredWidth(80);
			column.setCellRenderer(new ColorCellRenderer(this));
		}		
	}

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(JTable table) {
		this.table = table;
	}

	/**
	 * This method generates the rows for the table
	 * @param numOfRecords
	 * @return
	 */
	private ArrayList<TableRow> generateSchemes(int numOfRecords) {

		final int NUM_SCHEMES = numOfRecords;

		ArrayList<TableRow> result = new ArrayList<TableRow>();

		for (int i = 0; i < NUM_SCHEMES; ++i) {
			TableRow scheme;
			scheme = new TableRow("" + i+1);
			result.add(scheme);
		}

		return result;
	}

	/**
	 * This variable handles all the mouse events
	 */
	private MouseAdapter mouseAdapter = new MouseAdapter() {


		@Override
		public void mousePressed(MouseEvent e) {

			if (e.isPopupTrigger()) {

				// Get the cell coordinates on which the event occured
				rowNum = table.rowAtPoint(e.getPoint());
				columnNum = table.columnAtPoint(e.getPoint());

				// If the cell coordinates are valid, generate the popup
				if (rowNum >= 0 && rowNum < tableModel.getRowCount() &&
						columnNum >= 1 && columnNum < tableModel.getColumnCount()) {

					if (BatchState.isRedHighlightedCell(new Cell(rowNum, columnNum-1))){

						JPopupMenu menu = new JPopupMenu("popup");
						JMenuItem suggestions = new JMenuItem("See Suggestions");
						suggestions.addActionListener(okListener);
						menu.add(suggestions);
						menu.show(e.getComponent(), e.getX(), e.getY());
					}

				}
			}
		}

//		@Override
//		public void mouseReleased(MouseEvent e){
//			if (e.isPopupTrigger()) {
//
//				// Get the cell coordinates on which the event occured
//				rowNum = table.rowAtPoint(e.getPoint());
//				columnNum = table.columnAtPoint(e.getPoint());
//
//				// If the cell coordinates are valid, generate the popup
//				if (rowNum >= 0 && rowNum < tableModel.getRowCount() &&
//						columnNum >= 1 && columnNum < tableModel.getColumnCount()) {
//
//					if (BatchState.isRedHighlightedCell(new Cell(rowNum, columnNum-1))){
//
//						JPopupMenu menu = new JPopupMenu("popup");
//						JMenuItem suggestions = new JMenuItem("See Suggestions");
//						suggestions.addActionListener(okListener);
//						menu.add(suggestions);
//						menu.show(e.getComponent(), e.getX(), e.getY());
//					}
//
//				}
//			}
//		}

		/**
		 * This variable handles the action of displaying the suggestions
		 */
		private ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e2) {
				SuggestionsWindow suggestionsWin = null;
				suggestionsWin = new SuggestionsWindow(columnNum-1, rowNum, true);
				String newValue = suggestionsWin.getSelectedSuggestion();
				if (newValue != null){
					table.getModel().setValueAt(newValue, rowNum, columnNum);
				}
			}					
		};

	};

	/**
	 * This method handles changing the value of the target cell
	 */
	@Override
	public void valueChanged(Cell cell, String newValue) {
		tableModel.setValueAt(newValue, cell.getRecord(), cell.getField()+1);
	}

	/**
	 * This method handles changing the selected cell in the table
	 */
	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		table.changeSelection(newSelectedCell.getRecord(), newSelectedCell.getField()+1, true, false);
	}

	/**
	 * This method handles highlighting the target cell red
	 */
	@Override
	public void highlightRed(int recordID, int fieldID) {
		this.repaint();
	}

	/**
	 * This method handles removing the red highlight of the target cell
	 */
	@Override
	public void unhighlightRed(int recordID, int fieldID) {
		this.repaint();
	}
}
