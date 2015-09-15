package client.windows;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.drawing.DrawingImage;
import client.facade.clientFacade;
import client.main.Main;


/**
 * This class contains all the methods for downloading a batch
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
public class downloadBatchWindow extends JDialog implements ActionListener{
	
	// Init the variables
	private Main mainClass;
	private clientFacade cltFacade;
	private JButton viewSampleButton;
	private JButton cancelButton;
	private JButton downloadButton;
	@SuppressWarnings("rawtypes")
	private JComboBox projectList;
	private boolean imageDownloaded;
	private String imagePath;
	
	/**
	 * The constructor for the downloadBatchWindow class
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public downloadBatchWindow(Main mainClass, clientFacade cltFacade) {
		
		// Set the client Facade and main
		this.mainClass = mainClass;
		this.cltFacade = cltFacade;
		
		// initialize the image Downloaded boolean to false
		imageDownloaded = false;
		
		// Set the title of the window
		this.setTitle("Download Batch");
		
		// Create the top level content pane
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		
		// Set the location of the window on the desktop
		this.setLocation(400, 400);
			
		// Create the content of the window
		JPanel selectedProjectPanel = new JPanel();
		contentPane.add(selectedProjectPanel);
		
		JPanel actionButtonsPanel = new JPanel();
		contentPane.add(actionButtonsPanel);
		
		// Create the content of the selected Project Panel
		selectedProjectPanel.setLayout(new BoxLayout(selectedProjectPanel, BoxLayout.LINE_AXIS));
		
		JLabel projectLabel = new JLabel(" Project: ");
		selectedProjectPanel.add(projectLabel);
		
		projectList = new JComboBox(cltFacade.getProjects());
		selectedProjectPanel.add(projectList);
		
		viewSampleButton = new JButton("View Sample");
		selectedProjectPanel.add(viewSampleButton);
		viewSampleButton.addActionListener(this);
		
		// Create the content of the action Buttons Panel
		actionButtonsPanel.setLayout(new BoxLayout(actionButtonsPanel, BoxLayout.LINE_AXIS));
		
		cancelButton = new JButton("Cancel");
		actionButtonsPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		
		downloadButton = new JButton("Download");
		actionButtonsPanel.add(downloadButton);
		downloadButton.addActionListener(this);
		
		// Set modal and nonresizable
		this.setModal(true);
		this.setResizable(false);
		this.pack();
		
		//close the window if the x button is pressed
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}
	/**
	 * @return the imageDownloaded
	 */
	public boolean isImageDownloaded() {
		return imageDownloaded;
	}
	/**
	 * @param imageDownloaded the imageDownloaded to set
	 */
	public void setImageDownloaded(boolean imageDownloaded) {
		this.imageDownloaded = imageDownloaded;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton){
			// Exit the window
			this.dispose();
		}
		
		else if (e.getSource() == viewSampleButton){
			// View a sample Image
			imagePath = cltFacade.getSampleImage(projectList.getSelectedIndex()+1);
			mainClass.viewSampleImage(imagePath, (String) projectList.getSelectedItem());
		}
		
		else if (e.getSource() == downloadButton){
			// Download the first available Image
			int ProjectID = projectList.getSelectedIndex()+1;
			imagePath = cltFacade.downloadImage(ProjectID);
			imageDownloaded = true;
			this.dispose();
			mainClass.downloadImage(imagePath, ProjectID);
		}
		
	}

}
