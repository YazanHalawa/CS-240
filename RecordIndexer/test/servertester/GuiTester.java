package servertester;

import java.awt.*;

import servertester.controllers.Controller;
import servertester.views.IndexerServerTesterFrame;

public class GuiTester {

	public static void main(String[] args) {
		EventQueue.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						IndexerServerTesterFrame frame = new IndexerServerTesterFrame();			
						Controller controller = new Controller();
						frame.setController(controller);			
						controller.setView(frame);
						controller.initialize();
						frame.setVisible(true);
					}
				}
		);

	}

}

