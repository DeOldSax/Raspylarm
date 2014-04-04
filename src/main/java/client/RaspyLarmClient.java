package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import model.Alarm;

public class RaspyLarmClient {
	private static RaspyLarmClient instance;
	private Socket socket;
	private final String ip = "";
	private final int port = 0;

	private RaspyLarmClient() {
		try {
			socket = new Socket(ip, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RaspyLarmClient getInstance() {
		if (instance == null) {
			instance = new RaspyLarmClient();
		}
		return instance;
	}

	public void startAlarm(Alarm alarm) throws IOException {
		sendToServer(alarm);
	}

	public void stopAlarm(Alarm alarm) throws IOException {
		sendToServer(alarm);
	}

	private void sendToServer(Alarm alarm) throws IOException {
		ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
		writer.writeObject(alarm);
		writer.flush();
	}

}
