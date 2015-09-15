package client.windows;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import client.drawing.DrawingImage;
import client.facade.clientFacade;
import client.synchronization.BatchState;
import client.synchronization.BatchStateListener;
import client.synchronization.Cell;
import shared.modelClasses.Field;

/**
 * This class contains all the methods for the Field Help window
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
public class FieldHelp extends JComponent implements BatchStateListener{

	// Init the variables
	private Image targetImage;
	private FieldHelp fieldHelp;
	private ArrayList<Field> fields;
	private int width;
	private int height;
	private clientFacade cltFacade;
	private DrawingImage helpImage;
	private JEditorPane jEditorPane;
	
	/**
	 * The constructor for the Field Help class
	 * @param cltFacade
	 * @param fields
	 */
	public FieldHelp(clientFacade cltFacade, ArrayList<Field> fields) {
		
		this.cltFacade = cltFacade;
		// Add to list of listeners
		BatchState.getListeners().add(this);
		
		// create jeditorpane
        jEditorPane = new JEditorPane();
        
        // make it read-only
        jEditorPane.setEditable(false);
        
        // create a scrollpane; modify its attributes as desired
        JScrollPane scrollPane = new JScrollPane(jEditorPane);
        
        // add an html editor kit
        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane.setEditorKit(kit);
        
        int fieldID = BatchState.getSelectedCell().getField();
		String helpHTML = cltFacade.getClientCommunicator().getUrlBase() + "/" + fields.get(fieldID).getHelpHtml();
		Document doc = kit.createDefaultDocument();
        jEditorPane.setDocument(doc);
        try {
			jEditorPane.setPage(helpHTML);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the jEditorPane
	 */
	public JEditorPane getjEditorPane() {
		return jEditorPane;
	}

	/**
	 * @param jEditorPane the jEditorPane to set
	 */
	public void setjEditorPane(JEditorPane jEditorPane) {
		this.jEditorPane = jEditorPane;
	}

	@Override
	public void valueChanged(Cell cell, String newValue) {
		// Do nothing

	}

	@Override
	public void selectedCellChanged(Cell newSelectedCell) {
		int fieldID = BatchState.getSelectedCell().getField();
		String helpHTML = cltFacade.getClientCommunicator().getUrlBase() + "/" + BatchState.getFieldList().get(fieldID).getHelpHtml();
		try {
			jEditorPane.setPage(helpHTML);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	@Override
	public void highlightRed(int recordID, int fieldID) {
		// Do nothing
		
	}

	@Override
	public void unhighlightRed(int recordID, int fieldID) {
		// Do nothing
		
	}
}