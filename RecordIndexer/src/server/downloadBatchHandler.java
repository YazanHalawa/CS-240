
package server;

import shared.communicationClasses.*;
import server.facade.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;




/**
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("restriction")
public class downloadBatchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordIndexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public downloadBatchHandler() {
	}

	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		DownloadBatch_Params params = (DownloadBatch_Params) xmlStream.fromXML(exchange.getRequestBody());
		DownloadBatch_Result results = new DownloadBatch_Result();
		
		try{
			results = ServerFacade.downloadBatch(params);
		}
		catch (ServerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			return;
		}
		
		exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
		xmlStream.toXML(results, exchange.getResponseBody());
		exchange.getResponseBody().close();
	}
}
