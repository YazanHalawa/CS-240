package server;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import server.facade.*;

import com.sun.net.httpserver.*;

public class Server {

	private static int SERVER_PORT_NUMBER = 8080;//Default Port Number
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private static Logger logger;
	
	public Server(int portNum){
		SERVER_PORT_NUMBER = portNum;
	}
	
	static {
		try {
			initLog();
		}
		catch (IOException e) {
			System.out.println("Could not initialize log: " + e.getMessage());
		}
	}
	
	private static void initLog() throws IOException {
		
		Level logLevel = Level.FINE;
		
		logger = Logger.getLogger("recordIndexer"); 
		logger.setLevel(logLevel);
		logger.setUseParentHandlers(false);
		
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(logLevel);
		consoleHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(consoleHandler);

		FileHandler fileHandler = new FileHandler("log.txt", false);
		fileHandler.setLevel(logLevel);
		fileHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fileHandler);
	}

	
	@SuppressWarnings("restriction")
	private HttpServer server;
	
	private Server() {
		return;
	}
	
	@SuppressWarnings("restriction")
	private void run() {
		
		logger.info("Initializing Model");
		
		try {
			ServerFacade.initialize();		
		}
		catch (ServerException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		logger.info("Initializing HTTP Server");
		
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
											MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);			
			return;
		}

		server.setExecutor(null); // use the default executor
		
		server.createContext("/validateUser", validateUserHandler);
		server.createContext("/getProjects", getProjectsHandler);
		server.createContext("/getSampleImage", getSampleImageHandler);
		server.createContext("/downloadBatch", downloadBatchHandler);
		server.createContext("/submitBatch", submitBatchHandler);
		server.createContext("/getFields", getFieldsHandler);
		server.createContext("/search", searchHandler);
		server.createContext("/", downloadFileHandler);
		
		logger.info("Starting HTTP Server");

		server.start();
	}

	@SuppressWarnings("restriction")
	private HttpHandler validateUserHandler = new validateUserHandler();
	
	@SuppressWarnings("restriction")
	private HttpHandler getProjectsHandler = new getProjectsHandler();
	
	@SuppressWarnings("restriction")
	private HttpHandler getSampleImageHandler = new getSampleImageHandler();
	
	@SuppressWarnings("restriction")
	private HttpHandler downloadBatchHandler = new downloadBatchHandler();
	
	@SuppressWarnings("restriction")
	private HttpHandler submitBatchHandler = new submitBatchHandler();
	
	@SuppressWarnings("restriction")
	private HttpHandler getFieldsHandler = new getFieldsHandler();
	
	@SuppressWarnings("restriction")
	private HttpHandler searchHandler = new searchHandler();
	
	@SuppressWarnings("restriction")
	private HttpHandler downloadFileHandler = new downloadFileHandler();
	
	
	public static void main(String[] args) {
		Server myServer;
		
		if (args.length == 1)
			myServer = new Server(Integer.parseInt(args[0]));
		else//If not given a port number, result to the default
			myServer = new Server();
		
		try {
			Server.initLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		myServer.run();
	}
}
