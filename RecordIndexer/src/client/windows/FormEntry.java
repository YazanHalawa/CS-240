package client.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.synchronization.BatchState;
import client.synchronization.BatchStateListener;
import client.synchronization.Cell;
import shared.modelClasses.Field;

/**
 * This class contains all the methods for the Form Entry window
 * @author Yazan
 *
 */
@SuppressWarnings("serial")
public class FormEntry extends JPanel implements BatchStateListener{

	// Init the variables
	private JList<Integer> myList;
	private BorderLayout layout;
	private ArrayList<JTextField> fieldValues;
	private FormEntry fEntry;
	private JTextField text;
	private int rowNum;
	private int columnNum;
	
	/**
	 * The constructor for the Form Entry class
	 * @param numOfRows
	 * @param fields
	 * @param width
	 * @param height
	 */
	public FormEntry(int numOfRows, ArrayList<Field> fields, int width, int height) {
		
		// Sets the variables
		fEntry = this;
		BatchState.getListeners().add(this);
		fieldValues = new ArrayList<JTextField>();
		layout = new BorderLayout();
		this.setLayout(layout);
		
		// ADD the JList
		DefaultListModel<Integer> model = new DefaultListModel<Integer>();
		myList = new JList<Integer>(model);
		for (int i = 0; i < numOfRows; i++){
			model.addElement(i+1);
		}
		myList.addMouseListener(mouseAdapter);
		myList.setFixedCellWidth(width/3);
		this.add(myList, BorderLayout.WEST);

		// ADD the Labels and text fields
		JPanel rows = new JPanel();
		Dimension standardRowDim = new Dimension(2*width/3, height*4);
		rows.setMinimumSize(standardRowDim);
		rows.setPreferredSize(standardRowDim);
		rows.setMaximumSize(standardRowDim);
		rows.setLayout(new BoxLayout(rows, BoxLayout.PAGE_AXIS));
		boolean atFirstField = true;
		
		// Go through the fields and create the text areas
		for (Field field: fields){
			Dimension standardDim = new Dimension(width/3, 4*height/fields.size());
			JPanel tempRow = new JPanel();
			tempRow.setLayout(new BoxLayout(tempRow, BoxLayout.LINE_AXIS));

			JLabel label = new JLabel(field.getTitle());
			label.setMinimumSize(standardDim);
			label.setPreferredSize(standardDim);
			label.setMaximumSize(standardDim);
			tempRow.add(label);

			text = new JTextField();
			text.setMinimumSize(standardDim);
			text.setPreferredSize(standardDim);
			text.setMaximumSize(standardDim);
			text.addMouseListener(mouseAdapter);
			text.addKeyListener(keyboardAdapter);
			text.setFocusTraversalKeysEnabled(false);
			if (atFirstField){
				text.requestFocus();
				atFirstField = false;
			}
			tempRow.add(text);
			fieldValues.add(text);
			rows.add(tempRow);
		}

		// Add the components to the form Entry
		this.add(rows, BorderLayout.EAST);
		Dimension standardListDim = new Dimension(width/3, height*4);
		myList.setMinimumSize(standardListDim);
		myList.setPreferredSize(standardListDim);
		myList.setMaximumSize(standardListDim);

		Dimension standardPanelDim = new Dimension(width, height*4);
		this.setMinimumSize(standardPanelDim);
		this.setPreferredSize(standardPanelDim);
		this.setMaximumSize(standardPanelDim);

		// Set the model for the list
		myList.setSelectedIndex(0);
		myList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myList.addListSelectionListener(listSelectionListener);

	}

	/**
	 * This variables handles the selection of list items
	 */
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int recordNum = myList.getSelectedIndex();
			int fieldNum = 0;
			for (JTextField tf: fieldValues){
				tf.setText(BatchState.getValues()[recordNum][fieldNum]);
				if (BatchState.isRedHighlightedCell(new Cell(recordNum, fieldNum)))
					tf.setBackground(Color.RED);
				else
					tf.setBackground(Color.WHITE);
				fieldNum++;
			}
			fieldValues.get(0).requestFocus();
		}
	};

	/**
	 * This method handles moving between cells by pressing tab
	 */
	private KeyAdapter keyboardAdapter = new KeyAdapter(){

		@Override
		public void keyReleased(KeyEvent e){
			if (e.getKeyCode() == KeyEvent.VK_TAB){
				int recordNum = myList.getSelectedIndex();
				int fieldNum = 0;
				boolean foundFocus = false;
				boolean changedFocus = false;
				
				// iterate through the fields and change the focus accordingly
				for (JTextField tf: fieldValues){
					if (foundFocus){
						tf.requestFocus();
						changedFocus = true;
						break;
					}
					if (tf.hasFocus()){
						foundFocus = true;
						if ((tf.getText() != null && !tf.getText().equals("")) &&
								(BatchState.getValues()[recordNum][fieldNum] == null ||
								!BatchState.getValues()[recordNum][fieldNum].equals(tf.getText()))){
							BatchState.setCaller(fEntry);
							BatchState.setValue(new Cell(recordNum, fieldNum), tf.getText());
						}
					}
					fieldNum++;
				}
				if (changedFocus){
					BatchState.setCaller(fEntry);
					BatchState.setSelectedCell(new Cell(recordNum, fieldNum));
				}
			}
		}
	};

	/**
	 * This variables handles all the mouse events
	 */
	private MouseAdapter mouseAdapter = new MouseAdapter() {


		@Override
		public void mouseReleased(MouseEvent e) {

			if (!e.isPopupTrigger()){
				int recordNum = myList.getSelectedIndex();
				int fieldNum = 0;
				
				for (JTextField tf: fieldValues){
					if (tf.hasFocus()){
						if ((tf.getText() != null && !tf.getText().equals("")) &&
								(BatchState.getValues()[recordNum][fieldNum] == null ||
								!BatchState.getValues()[recordNum][fieldNum].equals(tf.getText()))){
							BatchState.setCaller(fEntry);
							BatchState.setValue(new Cell(recordNum, fieldNum), tf.getText());
						}
						break;
					}
					fieldNum++;
				}
				if (fieldNum >=0 && fieldNum < fieldValues.size()){
					BatchState.setCaller(fEntry);
					BatchState.setSelectedCell(new Cell(recordNum, fieldNum));
				}
				
			}
		}

		@Override 
		public void mousePressed(MouseEvent e){
			if (e.isPopupTrigger()) {

				rowNum = myList.getSelectedIndex();
				columnNum = fieldValues.indexOf(e.getSource());
				
				if (((JTextField)e.getComponent()).getBackground() == Color.RED){

					JPopupMenu menu = new JPopupMenu("popup");
					JMenuItem suggestions = new JMenuItem("See Suggestions");
					suggestions.addActionListener(okListener);
					menu.add(suggestions);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}

			}
			else if (e.getSource().getClass() == text.getClass()){
				int recordNum = myList.getSelectedIndex();
				int fieldNum = 0;
				
				for (JTextField tf: fieldValues){
					if (tf.hasFocus()){
						if ((tf.getText() != null && !tf.getText().equals("")) &&
								(BatchState.getValues()[recordNum][fieldNum] == null ||
								!BatchState.getValues()[recordNum][fieldNum].equals(tf.getText()))){
							BatchState.setCaller(fEntry);
							BatchState.setValue(new Cell(recordNum, fieldNum), tf.getText());
						}
						break;
					}
					fieldNum++;
				}
			}
		}
	};

	/**
	 * This method handles creating suggestions
	 */
	private ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e2) {							
			SuggestionsWindow suggestionsWin = new SuggestionsWindow(columnNum, rowNum, true);
			String newValue = suggestionsWin.getSelectedSuggestion();
			if (newValue != null){
				fieldValues.get(columnNum).setText(newValue);
				BatchState.setCaller(fEntry);
				BatchState.setValue(new Cell(rowNum, columnNum), newValue);
			}
		}					
	};
	
	@Override
	public void valueChanged(Cell cell, String newValue) {
		myList.setSelectedIndex(cell.getRecord());
		final int fieldNum = cell.getField();
		final String value = newValue;
		fieldValues.get(fieldNum).setText(value);
	}

	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		myList.setSelectedIndex(newSelectedCell.getRecord());
		fieldValues.get(newSelectedCell.getField()).requestFocus();
	}


	public void setFocusToField(){
		myList.setSelectedIndex(BatchState.getSelectedCell().getRecord());
		int fieldNum = BatchState.getSelectedCell().getField();
		if (fieldNum >= 0 && fieldNum < fieldValues.size())
			fieldValues.get(fieldNum).requestFocus();
	}

	@Override
	public void highlightRed(int recordID, int fieldID) {
		if (myList.getSelectedIndex() == recordID)
			fieldValues.get(fieldID).setBackground(Color.RED);
	}

	@Override
	public void unhighlightRed(int recordID, int fieldID) {
		if (myList.getSelectedIndex() == recordID)
			fieldValues.get(fieldID).setBackground(Color.WHITE);


	}

}
