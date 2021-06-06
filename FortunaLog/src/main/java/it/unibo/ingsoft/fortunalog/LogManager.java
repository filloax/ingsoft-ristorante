package it.unibo.ingsoft.fortunalog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LogManager {
	public static final int PORT = 2404; //default port
	public static final String DIR = "."; //default port

	public static void main(final String[] args) throws IOException {
		int port = -1;
		String dir = "";

		// Controllo argomenti
	    try {
	    	if (args.length == 2) {
	    		port = Integer.parseInt(args[0]);
	    		if (port < 1024 || port > 65535) {
	    			System.err.println("Usage: java " + args[0] + " [port] [logDir]");
	    			System.exit(-1);
	    		}
				dir = args[1];
	    	} else if (args.length == 1) {
	    		port = Integer.parseInt(args[0]);
	    		if (port < 1024 || port > 65535) {
	    			System.err.println("Usage: java " + args[0] + " [port] [logDir]");
	    			System.exit(-1);
	    		}
	    	} else if (args.length == 0) {
	    		port = PORT;
				dir = DIR;
	    	} else {
	    		System.err.println("Usage: java " + args[0] + " [port] [logDir]");
	    		System.exit(-1);
	    	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	System.err.println("Usage: java " + args[0] + " [port] [logDir]");
	    	System.exit(-1);
	    }

	    ServerSocket serverSocket = null;
	    Socket clientSocket = null;

	    try {
	    	serverSocket = new ServerSocket(port);
	    	serverSocket.setReuseAddress(true);
	    }
	    catch (Exception e) {
	    	System.err.println("LogServer: problemi nella creazione della server socket: " + e.getMessage());
	    	e.printStackTrace();
	    	System.exit(-2);
	    }

	    try {
	    	while (true) {
	    		System.err.println("LogServer: in attesa di richieste su porta " + port + "...\n");

	    		try {
	    			clientSocket = serverSocket.accept();
	    			clientSocket.setSoTimeout(120000);
	    			System.err.println("Server: connessione accettata: " + clientSocket);
	    		}
	    		catch (Exception e) {
	    			System.err.println("Server: problemi nella accettazione della connessione: " + e.getMessage());
	    			e.printStackTrace();
	    			continue;
	    		}

	    		// Serizio delegato ad un nuovo thread
	    		try {
	    			new LogManagerThread(clientSocket, dir).start();
	    		}
	    		catch (Exception e) {
	    			System.err.println("Server: problemi nel server thread: " + e.getMessage());
	    			e.printStackTrace();
	    			continue;
	    		}

	    	}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	System.exit(-3);
	    }
	    
	}
}