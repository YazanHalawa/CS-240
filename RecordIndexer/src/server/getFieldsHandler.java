
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
public class getFieldsHandler implements HttpHandler {

	private Logger logger = Logger.getLogger("recordIndexer"); 
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	public getFieldsHandler() {
	}

	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		GetFields_Params params = (GetFields_Params) xmlStream.fromXML(exchange.getRequestBody());
		GetFields_Result results = new GetFields_Result();
		
		try{
			results = ServerFacade.getFields(params);
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
