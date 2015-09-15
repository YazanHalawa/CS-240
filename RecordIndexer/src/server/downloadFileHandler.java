
package server;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.logging.*;

import com.sun.net.httpserver.*;

/**
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("restriction")
public class downloadFileHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordIndexer"); 
	
	public downloadFileHandler() {
	}

	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try{
			File file = new File("./Records" + File.separator + exchange.getRequestURI().getPath());
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			Files.copy(file.toPath(), exchange.getResponseBody());
			exchange.getResponseBody().close();
		}
		catch(Exception e){
			logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			e.printStackTrace();
		}
	}
}
