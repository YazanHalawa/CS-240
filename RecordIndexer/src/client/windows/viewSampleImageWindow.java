
package client.windows;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import client.drawing.DrawingComponent;
import client.facade.clientFacade;
import client.main.Main;

/**
 * This class contains all the methods for viewing a sample Image
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("serial")
public class viewSampleImageWindow extends JDialog implements ActionListener {
	
	// Init variables
	private JButton cancelButton;
	private DrawingComponent drawingComp;

	/**
	 * The constructor for the View Sample Image Window
	 */
	public viewSampleImageWindow(String path, String projectName) {

		
		// Set the window's title
		this.setTitle("Sample Image from " + projectName);

		// Create the top level content pane
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		// Create the Image Object
		drawingComp = new DrawingComponent(path, false);
		
		// Draw the Image
		contentPane.add(drawingComp);
		
		// Add the button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		contentPane.add(buttonPanel);
		
		// Display the Window
		this.setModal(true);
		this.setResizable(false);
		this.pack();
		
		// Set the location and size of the window on the desktop
		this.setLocation(300, 300);
		this.setSize(800, 600);
		
		//close the window if the x button is pressed
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton){
			// Close the window if the cancel button is pressed
			this.dispose();
		}
	}

}
