
package server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.*;

import shared.communicationClasses.*;
import server.facade.*;

import com.sun.net.httpserver.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Yazan Halawa
 *
 */
@SuppressWarnings("restriction")
public class searchHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordIndexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public searchHandler() {
	}

	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Search_Params params = (Search_Params) xmlStream.fromXML(exchange.getRequestBody());
		Search_Result results = new Search_Result();
		
		try{
			results = ServerFacade.search(params);
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
