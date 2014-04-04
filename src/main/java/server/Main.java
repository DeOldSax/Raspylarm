package server;

import java.io.IOException;
import java.net.Socket;

import model.Alarm;

public class Main {
	public static void main(String[] args) {
		int port = 8080;
		Server server = new Server(port);

		while (true) {
			Socket client;
			try {
				client = server.waitForConnection();
				final Alarm alarm = server.readAlarmTask(client);
				if (alarm.isActive()) {
					server.startAlarmSheduling(alarm);
				} else {
					server.stopAlarm(alarm);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
}
