
package client.main;

import java.awt.EventQueue;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import client.facade.clientFacade;
import client.windows.*;

/**
 * This is the main method which runs the client program
 * @author Yazan Halawa
 *
 */
public class Main {

	// Init the variables
	private static IndexingWin mainIndexingWindow;
	private static LogInWin logInWindow;
	private static clientFacade cltFacade;
	private static downloadBatchWindow downloadBatchWin;
	private static viewSampleImageWindow viewSampleImageWin;
	
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		
		EventQueue.invokeLater(new Runnable() {		
			public void run() {
				
				Main mainClass = new Main();
				// Grab the host and port Number and create a client facade
				cltFacade = new clientFacade(args[0], args[1]);
				
				// Create the Window
				logInWindow = new LogInWin(cltFacade, mainClass);

				// initial state
				logInWindow.setVisible(true);
	
			}
		});
	}

	/**
	 * This method performs the log in process of closing the log in window
	 * and displaying the main indexing window
	 */
	public void LoggingIn (){
		// Close the logInWindow
		logInWindow.dispose();
		
		// Create the indexing window
		mainIndexingWindow = new IndexingWin(cltFacade, this, null);
		
		// Check if there is a data file to import previous settings from
		mainIndexingWindow.importFromSavedState();		
		mainIndexingWindow.setVisible(true);
	}
	
	/**
	 * This method performs the log out process of closing the main indexing window
	 * and displaying the log in window
	 */
	public void LoggingOut (){
		mainIndexingWindow.dispose();
		logInWindow = new LogInWin(cltFacade, this);
		logInWindow.setVisible(true);
	}

	/**
	 * This method performs the process of displaying the download batch window
	 */
	public void downloadBatch() {
		downloadBatchWin = new downloadBatchWindow(this, cltFacade);
		downloadBatchWin.setVisible(true);
		if (downloadBatchWin.isImageDownloaded()){
			mainIndexingWindow.getDownloadBatch().setEnabled(false);
		}
	}

	public void viewSampleImage(String path, String projectName) {
		viewSampleImageWin = new viewSampleImageWindow(path, projectName);
		viewSampleImageWin.setVisible(true);
	}

	public void downloadImage(String imagePath, int projectID) {
		try {
			mainIndexingWindow.drawImage(imagePath, projectID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
