package client.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import client.facade.clientFacade;
import client.qualityChecker.ISpellCorrector.NoSimilarWordFoundException;
import client.qualityChecker.SpellCorrector;
import client.synchronization.BatchState;
import client.synchronization.Cell;

/**
 * This class contains all the methods for the Suggestion window
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
public class SuggestionsWindow extends JDialog implements ActionListener {

	// Init variables
	private clientFacade cltFacade;
	private JButton cancelButton;
	private JButton useSuggestionButton;
	private JList<String> suggestionsList;
	private ArrayList<String> suggestions;
	private String selectedSuggestion;
	private boolean notInKnownValues;

	/**
	 * The constructor for the SuggestionsWindow class
	 * @param visibility
	 */
	public SuggestionsWindow(int fieldID, int recordID, boolean visibility) {

		// Set the boolean
		notInKnownValues = false;

		// Set the client Facade
		this.cltFacade = BatchState.getCltFacade();

		// Set the title of the window
		this.setTitle("Suggestions");

		// Set the content pane
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);

		// make it modal and nonresizable
		this.setResizable(false);
		this.setModal(true);

		// Set the layout and add the components
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		DefaultListModel<String> model = new DefaultListModel<String>();
		suggestionsList = new JList<String>(model);
		Dimension listDimension = new Dimension(300, 100);
		suggestionsList.setMinimumSize(listDimension);
		suggestionsList.setPreferredSize(listDimension);
		suggestionsList.setMaximumSize(listDimension);
		suggestionsList.addListSelectionListener(listSelectionListener);
		JScrollPane scrollableList = new JScrollPane(suggestionsList);
		JPanel buttons = new JPanel();
		contentPane.add(scrollableList);
		contentPane.add(buttons);

		// Create the list of suggestions
		String knownDataFileName = BatchState.getFieldList().get(fieldID).getKnownData();
		if (knownDataFileName != null && !knownDataFileName.equals("")){
			String dictionaryFileName = cltFacade.getClientCommunicator().getUrlBase() +
					"/" + knownDataFileName;
			String inputWord = BatchState.getValues()[recordID][fieldID];
			SpellCorrector myCorrector = new SpellCorrector();

			if (inputWord != null && dictionaryFileName != null && !dictionaryFileName.equals("")){
				try {
					myCorrector.useDictionary(dictionaryFileName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					suggestions = myCorrector.suggestSimilarWords(inputWord.toLowerCase());
					notInKnownValues = myCorrector.isNotInKnownValues();
				} catch (NoSimilarWordFoundException e) {
					notInKnownValues = true;
				}

				// Add the suggestions to the List
				if (suggestions != null){
					for (int i = 0; i < suggestions.size(); i++){
						model.addElement(suggestions.get(i));
					}
				}
			}
		}

		suggestionsList.setFixedCellWidth(300);
		suggestionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// create each button
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		useSuggestionButton = new JButton("Use Suggestion");
		useSuggestionButton.setEnabled(false);
		useSuggestionButton.addActionListener(this);
		buttons.add(cancelButton);
		buttons.add(useSuggestionButton);

		// display the window
		this.setLocation(300, 400);
		this.pack();
		this.setSize(300, 150);
		this.setVisible(visibility);

		// Set the X button
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	/**
	 * @return the notInKnownValues
	 */
	public boolean isNotInKnownValues() {
		return notInKnownValues;
	}

	/**
	 * @param notInKnownValues the notInKnownValues to set
	 */
	public void setNotInKnownValues(boolean notInKnownValues) {
		this.notInKnownValues = notInKnownValues;
	}

	/**
	 * @return the suggestions
	 */
	public ArrayList<String> getSuggestions() {
		return suggestions;
	}

	/**
	 * @param suggestions the suggestions to set
	 */
	public void setSuggestions(ArrayList<String> suggestions) {
		this.suggestions = suggestions;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			// Close the window
			this.dispose();
		}

		else if (e.getSource() == useSuggestionButton){
			selectedSuggestion = suggestionsList.getSelectedValue();
			this.dispose();
		}

	}
	
	/**
	 * This variables handles the selection of list items
	 */
	private ListSelectionListener listSelectionListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			useSuggestionButton.setEnabled(true);
		}
	};

	/**
	 * @return the selectedSuggestion
	 */
	public String getSelectedSuggestion() {
		return selectedSuggestion;
	}

	/**
	 * @param selectedSuggestion the selectedSuggestion to set
	 */
	public void setSelectedSuggestion(String selectedSuggestion) {
		this.selectedSuggestion = selectedSuggestion;
	}

}