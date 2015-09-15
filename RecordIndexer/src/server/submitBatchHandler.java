 
package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.*;

import server.facade.*;
import shared.communicationClasses.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("restriction")
public class submitBatchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordIndexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public submitBatchHandler() {
	}

	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		SubmitBatch_Params params = (SubmitBatch_Params) xmlStream.fromXML(exchange.getRequestBody());
		SubmitBatch_Result results = new SubmitBatch_Result();
		
		try{
			results = ServerFacade.submitBatch(params);
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
