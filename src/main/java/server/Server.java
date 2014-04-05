package server;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import view.RaspyLarmUI;

public class Server {

	public static void main(String[] args) {
		Thread.currentThread().setName("Server");
		final Logger LOGGER = Logger.getLogger(Server.class);
		PropertyConfigurator.configure(RaspyLarmUI.class.getResourceAsStream("log4j.properties"));
		int port = Integer.valueOf(args[0]);
		RaspyLarmServer server = new RaspyLarmServer(port);

		while (true) {
			LOGGER.debug("Waiting for new alarms...");
			Socket client = null;
			try {
				client = server.waitForConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			new Thread(new Scheduler(client, server)).start();
		}
	}
}
