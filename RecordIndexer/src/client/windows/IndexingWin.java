
package client.windows;

import shared.modelClasses.Field;
import shared.modelClasses.Project;
import client.serialization.serialData;
import client.synchronization.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.drawing.ColorTable;
import client.drawing.DrawingComponent;
import client.facade.clientFacade;
import client.main.Main;

/**
 * This class contains all the methods for the indexing window
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
public class IndexingWin extends JFrame implements ActionListener{
	// Init variables
	private JMenuItem logOut;
	private clientFacade cltFacade;
	private JMenuItem exit;
	private JMenuItem downloadBatch;
	private Main mainClass;
	private JButton zoomIn;
	private JButton zoomOut;
	private JButton invertImage;
	private JButton toggleHighlights;
	private JButton save;
	private JButton submit;
	private JPanel topView;
	private DrawingComponent drawingComp;
	private ArrayList<Field> fields;
	private Project project;
	private JPanel tableEntry;
	private JPanel formEntryTab;
	private JTabbedPane leftBottom;
	private FormEntry formEntry;
	private JPanel fieldHelp;
	private JTabbedPane rightBottom;
	private JScrollPane tableScroll;
	private JScrollPane formScroll;
	private JScrollPane fieldHelpScroll;
	private JSplitPane verticalSplit;
	private JSplitPane horizontalSplit;
	private BatchState bs;

	/**
	 * The constructor for the IndexingWin class
	 * @param mainClass 
	 */
	public IndexingWin(clientFacade cltFacade, Main mainClass, String imagePath) {

		// Add a window listener
		this.addWindowListener(windowAdapter);

		// Set the client facade and the main class
		this.mainClass = mainClass;
		this.cltFacade = cltFacade;

		// Create the top level content pane
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		// Set the window's title
		this.setTitle("Indexing Window");

		// Specify what should happen when the user clicks the window's close icon
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the location of the window on the desktop
		this.setLocation(100, 100);

		// Create the Menu Bar
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar);

		// Create the first Menu
		JMenu firstMenu = new JMenu("File");
		menuBar.add(firstMenu);
		menuBar.add(Box.createHorizontalGlue());

		// Create the Items in the first menu
		downloadBatch = new JMenuItem("Download Batch");
		downloadBatch.addActionListener(this);
		firstMenu.add(downloadBatch);

		logOut = new JMenuItem("Log out");
		logOut.addActionListener(this);
		firstMenu.add(logOut);

		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		firstMenu.add(exit);

		BatchState.setCltFacade(cltFacade);
		
		// Create the bottom panel part
		JPanel mainWindow = new JPanel();
		contentPane.add(mainWindow);

		// Create the Border Layout of the Window
		mainWindow.setLayout(new BorderLayout());

		// Set up the top-Buttons Panel
		JPanel topButtonPanel = new JPanel();
		mainWindow.add(topButtonPanel, BorderLayout.PAGE_START);

		zoomIn = new JButton("Zoom in");
		zoomIn.addActionListener(this);
		topButtonPanel.add(zoomIn);

		zoomOut = new JButton("Zoom out");
		zoomOut.addActionListener(this);
		topButtonPanel.add(zoomOut);

		invertImage = new JButton("Invert Image");
		invertImage.addActionListener(this);
		topButtonPanel.add(invertImage);

		toggleHighlights = new JButton("Toggle Highlights");
		toggleHighlights.addActionListener(this);
		topButtonPanel.add(toggleHighlights);

		save = new JButton("Save");
		save.addActionListener(this);
		topButtonPanel.add(save);

		submit = new JButton("Submit");
		submit.addActionListener(this);
		topButtonPanel.add(submit);

		// Set up the center Panel
		JPanel centerPanel = new JPanel();
		mainWindow.add(centerPanel, BorderLayout.CENTER);

		// If an image is to be displayed
		if (imagePath != null){
			enableButtons();
		}
		else{
			disableButtons();
		}

		// Set up the views within the center Panel
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		topView = new JPanel();
		drawingComp = new DrawingComponent(imagePath, true);
		topView.add(drawingComp, BorderLayout.CENTER);
		topView.setBackground(Color.DARK_GRAY);
		topView.setBorder(BorderFactory.createLineBorder(Color.BLACK));	

		JPanel bottomView = new JPanel();
		bottomView.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Split the center panel vertically
		verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topView, bottomView);
		verticalSplit.setDividerLocation(500);
		verticalSplit.setResizeWeight(0.5);
		centerPanel.add(verticalSplit);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 100);
		topView.setMinimumSize(minimumSize);
		bottomView.setMinimumSize(minimumSize);

		// Split the bottomView horizontally
		bottomView.setLayout(new BoxLayout(bottomView, BoxLayout.LINE_AXIS));

		leftBottom = new JTabbedPane();
		tableEntry = new JPanel();

		tableScroll = new JScrollPane(tableEntry);
		leftBottom.addTab("Table Entry", tableScroll);
		formEntryTab = new JPanel();
		formScroll = new JScrollPane(formEntryTab);
		leftBottom.addTab("Form Entry", formScroll);
		leftBottom.addChangeListener(changeListener);

		rightBottom = new JTabbedPane();
		fieldHelp = new JPanel();
		fieldHelpScroll = new JScrollPane(fieldHelp);
		rightBottom.addTab("Field Help", fieldHelpScroll);
		JPanel imageNavigation = new JPanel();
		rightBottom.addTab("Image Navigation", imageNavigation);

		horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftBottom, rightBottom);
		bottomView.add(horizontalSplit);
		horizontalSplit.setDividerLocation(400);
		horizontalSplit.setResizeWeight(0.5);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSizeBottom = new Dimension(50, 50);
		leftBottom.setMinimumSize(minimumSizeBottom);
		rightBottom.setMinimumSize(minimumSizeBottom);


		// Display the window with the correct size
		this.pack();		
		this.setSize(800, 700);
		Dimension minimumSizeWindow = new Dimension(300, 300);
		this.setMinimumSize(minimumSizeWindow);

	}

	/**
	 * 
	 */
	public void disableButtons() {
		zoomIn.setEnabled(false);
		zoomOut.setEnabled(false);
		invertImage.setEnabled(false);
		toggleHighlights.setEnabled(false);
		save.setEnabled(false);
		submit.setEnabled(false);
		downloadBatch.setEnabled(true);
	}

	/**
	 * 
	 */
	public void enableButtons() {
		zoomIn.setEnabled(true);
		zoomOut.setEnabled(true);
		invertImage.setEnabled(true);
		toggleHighlights.setEnabled(true);
		save.setEnabled(true);
		submit.setEnabled(true);
		downloadBatch.setEnabled(false);
	}

	private WindowListener windowAdapter = new WindowListener(){

		@Override
		public void windowClosing(WindowEvent e) {
			saveData();
			System.exit(0);
		}

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent e) {

		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

	};
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == logOut){
			// Toggle Windows
			mainClass.LoggingOut();
		}

		else if (e.getSource() == exit){
			saveData();
			System.exit(0);
		}

		else if (e.getSource() == downloadBatch){
			// Download Batch
			mainClass.downloadBatch();
			topView.add(drawingComp);
			repaint();
		}

		else if (e.getSource() == zoomIn){
			drawingComp.setScale(1.1 * drawingComp.getScale());
		}

		else if (e.getSource() == zoomOut){
			drawingComp.setScale(0.9 * drawingComp.getScale());
		}

		else if (e.getSource() == invertImage){
			RescaleOp op = new RescaleOp(-1.0f, 255f, null);
			drawingComp.setSelectedImage((Image)op.filter((BufferedImage) drawingComp.getSelectedImage(), null), true);
		}

		else if (e.getSource() == toggleHighlights){
			if (drawingComp.isHighlightsOn()){
				drawingComp.setHighlightsOn(false);
				drawingComp.getSelectedCell().setSelected(false);
				drawingComp.repaint();
			}	
			else{
				drawingComp.setHighlightsOn(true);
				drawingComp.getSelectedCell().setSelected(true);
				drawingComp.repaint();
			}
		}

		else if (e.getSource() == save){
			saveData();
		}

		else if (e.getSource() == submit){
			cltFacade.SubmitBatch();
			topView.removeAll();
			fieldHelp.removeAll();
			tableEntry.removeAll();
			formEntry.removeAll();
			repaint();
			disableButtons();

		}
	}

	/**
	 * 
	 */
	public void saveData() {
		// Store the important information in the BatchState 
		serialData data = new serialData();
		
		// Set the variables to be saved
		data.setBatchID(BatchState.getBatchID());
		data.setFieldList(BatchState.getFieldList());
		data.setProj(BatchState.getProj());
		data.setValues(BatchState.getValues());
		data.setSelectedCell(BatchState.getSelectedCell());
		data.setListeners(BatchState.getListeners());
		data.setCaller(BatchState.getCaller());
		data.setRedHighlightedCells(BatchState.getRedHighlightedCells());
		data.setScrollPosTableVert(tableScroll.getVerticalScrollBar().getValue());
		data.setScrollPosTableHorz(tableScroll.getHorizontalScrollBar().getValue());
		data.setScrollPosFormVert(formScroll.getVerticalScrollBar().getValue());
		data.setScrollPosFormHorz(formScroll.getHorizontalScrollBar().getValue());
		data.setScrollPosFieldVert(fieldHelpScroll.getVerticalScrollBar().getValue());
		data.setScrollPosFieldHorz(fieldHelpScroll.getHorizontalScrollBar().getValue());
		data.setZoomLevel(drawingComp.getScale());
		data.setHighlightsOn(drawingComp.isHighlightsOn());
		data.setInverting(drawingComp.isInverting());
		data.setImagePath(BatchState.getImagePath());
		data.setIndexingWinPos(this.getLocation());
		data.setIndexingWinSize(this.getSize());
		data.setHorizSplitPos(horizontalSplit.getDividerLocation());
		data.setVertSplitPos(verticalSplit.getDividerLocation());
		data.setxCoord(drawingComp.getW_translateX());
		data.setyCoord(drawingComp.getW_translateY());

		// Create the Data file to save the information in
		OutputStream outFile = null;
		try {
			outFile = new BufferedOutputStream(new FileOutputStream(cltFacade.getUsername() + ".xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		XStream xStream = new XStream(new DomDriver());
		xStream.toXML(data, outFile);
		try {
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the downloadBatch
	 */
	public JMenuItem getDownloadBatch() {
		return downloadBatch;
	}

	/**
	 * @param downloadBatch the downloadBatch to set
	 */
	public void setDownloadBatch(JMenuItem downloadBatch) {
		this.downloadBatch = downloadBatch;
	}

	public void drawImage(String imagePath, int projectID) throws IOException {

		// Set the BatchState information
		fields = cltFacade.getAllFields(projectID);
		project = cltFacade.getProject(projectID);
		if (bs == null)
			bs = new BatchState(cltFacade, project.getRecordsForImage(), fields.size(), fields, project);
		BatchState.setImagePath(imagePath);

		// Draw the Image
		drawingComp.setSelectedImage(ImageIO.read(new URL(imagePath)), false);
		BatchState.setDrawComp(drawingComp);
		// Draw the Table
		ColorTable myTable = new ColorTable("Table Entry", fields, project.getRecordsForImage());
		tableEntry.add(new JScrollPane(myTable.getTable()));

		// Draw the Form
		formEntry = new FormEntry(project.getRecordsForImage(), fields, formEntryTab.getWidth(),
				formEntryTab.getHeight());
		formEntryTab.add(formEntry, BorderLayout.WEST);

		// Draw the field help
		FieldHelp helpImage = new FieldHelp(cltFacade, fields);
		fieldHelp.add(helpImage.getjEditorPane());
		fieldHelp.setBackground(Color.WHITE);

		// Enable the buttons
		enableButtons();
	}

	private ChangeListener changeListener = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent e) {
			JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
			int index = sourceTabbedPane.getSelectedIndex();
			if (index == 1){
				formEntry.setFocusToField();
			}
		}
	};

	public void importFromSavedState(){

		InputStream inFile = null;
		boolean fileIsFound = true;
		try {
			inFile = new BufferedInputStream(new FileInputStream(cltFacade.getUsername() + ".xml"));
		} catch (FileNotFoundException e1) {
			fileIsFound = false;
		}
		if (fileIsFound){
			// import the data
			XStream xStream = new XStream(new DomDriver());
			serialData importedData = new serialData();
			importedData = (serialData)xStream.fromXML(inFile);
			
			// set the batch state variables
			BatchState.setBatchID(importedData.getBatchID());
			BatchState.setFieldList(importedData.getFieldList());
			BatchState.setProj(importedData.getProj());
			BatchState.setListeners(importedData.getListeners());
			BatchState.setCaller(importedData.getCaller());
			BatchState.setRedHighlightedCells(importedData.getRedHighlightedCells());
			BatchState.setScrollPosTableVert(importedData.getScrollPosTableVert());
			BatchState.setScrollPosTableHorz(importedData.getScrollPosTableHorz());
			BatchState.setScrollPosFormVert(importedData.getScrollPosFormVert());
			BatchState.setScrollPosFormHorz(importedData.getScrollPosFormHorz());
			BatchState.setScrollPosFieldVert(importedData.getScrollPosFieldVert());
			BatchState.setScrollPosFieldHorz(importedData.getScrollPosFieldHorz());
			BatchState.setImagePath(importedData.getImagePath());
			BatchState.setIndexingWinPos(importedData.getIndexingWinPos());
			BatchState.setIndexingWinSize(importedData.getIndexingWinSize());
			BatchState.setHorizSplitPos(importedData.getHorizSplitPos());
			BatchState.setVertSplitPos(importedData.getVertSplitPos());
			BatchState.setCltFacade(this.cltFacade);
			BatchState.setxCoord(importedData.getxCoord());
			BatchState.setyCoord(importedData.getyCoord());
			try {
				inFile.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				drawImage(BatchState.getImagePath(), BatchState.getProj().getId());
			} catch (IOException e) {
				e.printStackTrace();
			}
			BatchState.setValues(importedData.getValues());
			BatchState.getDrawComp().setScale(importedData.getZoomLevel());
			BatchState.getDrawComp().setHighlightsOn(importedData.isHighlightsOn());
			BatchState.getDrawComp().setInverting(importedData.isInverting());
			tableScroll.getVerticalScrollBar().setValue(BatchState.getScrollPosTableVert());
			tableScroll.getHorizontalScrollBar().setValue(BatchState.getScrollPosTableHorz());
			formScroll.getVerticalScrollBar().setValue(BatchState.getScrollPosFormVert());
			formScroll.getHorizontalScrollBar().setValue(BatchState.getScrollPosFormHorz());
			fieldHelpScroll.getVerticalScrollBar().setValue(BatchState.getScrollPosFieldVert());
			fieldHelpScroll.getHorizontalScrollBar().setValue(BatchState.getScrollPosFieldHorz());
			this.setLocation(BatchState.getIndexingWinPos());
			this.setSize(BatchState.getIndexingWinSize());
			horizontalSplit.setDividerLocation(BatchState.getHorizSplitPos());
			verticalSplit.setDividerLocation(BatchState.getVertSplitPos());
			drawingComp.setScale(BatchState.getDrawComp().getScale());
			drawingComp.setHighlightsOn(BatchState.getDrawComp().isHighlightsOn());
			drawingComp.setInverting(BatchState.getDrawComp().isInverting());
			
			if (drawingComp.isHighlightsOn()){
				drawingComp.getSelectedCell().setSelected(true);
				drawingComp.repaint();
			}	
			else{
				drawingComp.getSelectedCell().setSelected(false);
				drawingComp.repaint();
			}
			RescaleOp op = new RescaleOp(-1.0f, 255f, null);
			drawingComp.setSelectedImage((Image)op.filter((BufferedImage) drawingComp.getSelectedImage(), null), true);
			drawingComp.setW_translateX(BatchState.getxCoord());
			drawingComp.setW_translateY(BatchState.getyCoord());
			
			// repaint everything
			this.repaint();

		}

	}

}
