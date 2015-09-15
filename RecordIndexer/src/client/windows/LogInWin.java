
package client.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.facade.clientFacade;
import client.main.Main;

/**
 * This class implements a Log-in window for the record Indexer client
 * @author Yazan Halawa
 * 
 */
@SuppressWarnings("serial")
public class LogInWin extends JDialog implements ActionListener{
	
	// Init variables
	private clientFacade cltFacade;
	private JButton logInButton;
	private JButton exitButton;
	private JTextField usernameText;
	private JPasswordField passwordText;
	private String username;
	private String password;
	private Main mainClass;
	
	/**
	 * The constructor for the LogInWin Class
	 */
	public LogInWin(clientFacade cltFacade, Main mainClass) {
		
		// Set the client Facade and the main class
		this.cltFacade = cltFacade;
		this.mainClass = mainClass;
		
		// Set the window's title
		this.setTitle("Log-In to Indexer");
		
		// Create the top level content pane
		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
		
		// Set the content Pane's layout
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        
		// Add subcomponents to the window
		JPanel usernamePanel = new JPanel();
		contentPane.add(usernamePanel);
		JPanel passwordPanel = new JPanel();
		contentPane.add(passwordPanel);
		JPanel buttonsPanel = new JPanel();
		contentPane.add(buttonsPanel);
		
		//Add subcomponents to each Panel
		usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.LINE_AXIS));
		JLabel usernameLabel = new JLabel(" Username: ");
		usernamePanel.add(usernameLabel);
		usernameText = new JTextField();
		usernamePanel.add(usernameText);
		
		passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.LINE_AXIS));
		JLabel passwordLabel = new JLabel(" Password: ");
		passwordPanel.add(passwordLabel);
		passwordText = new JPasswordField();
		passwordPanel.add(passwordText);
		
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		logInButton = new JButton("Log in");
		logInButton.addActionListener(this);
		buttonsPanel.add(logInButton);
		exitButton = new JButton("exit");
		exitButton.addActionListener(this);
		buttonsPanel.add(exitButton);

		// Display the Window with the correct size
		this.pack();
		
		// Set the location and size of the window on the desktop
		this.setLocation(600, 400);
		this.setSize(220, 110);
		
		//make the window modal and non-resizable
		this.setModal(true);
		this.setResizable(false);
		
		//close the window if the x button is pressed
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * This method performs the actions according to the events on the listeners
	 * @param e the action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == logInButton){
			username = usernameText.getText();
			password = new String(passwordText.getPassword());
			String result = cltFacade.validateUser(username, password);
			if (!result.equals("Failed")){
				//Display the pop up welcome message
				JOptionPane.showMessageDialog(this, result);

				// Toggle Windows
				mainClass.LoggingIn();
			}
			else{
				JOptionPane.showMessageDialog(this, "Error! Invalid username or password. Please try again");
			}
		}
		else if (e.getSource() == exitButton){
			//exit the program
			this.dispose();
		}
	}
}
