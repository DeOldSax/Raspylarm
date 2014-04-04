package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Alarm;
import model.ArgumentManager;

import org.apache.log4j.Logger;

public class RaspyLarmClient {
	final Logger LOGGER = Logger.getLogger(getClass());
	private static RaspyLarmClient instance;
	private Socket socket;

	private RaspyLarmClient() {
	}

	public static RaspyLarmClient getInstance() {
		if (instance == null) {
			instance = new RaspyLarmClient();
		}
		return instance;
	}

	public void sendToServer(Alarm alarm) throws IOException {
		socket = createSocket();
		ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
		writer.writeObject(alarm);
		writer.flush();
		LOGGER.debug("Send Alarm " + alarm + " to server.");
	}

	private Socket createSocket() {
		final String ip = ArgumentManager.getInstance().getIp();
		final int port = ArgumentManager.getInstance().getPort();
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			LOGGER.error("Connection failed.", e);
		} catch (IOException e) {
			LOGGER.error("Connection failed.", e);
		}

		return socket;
	}

}
