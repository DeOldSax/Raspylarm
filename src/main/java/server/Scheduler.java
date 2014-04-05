package server;

import java.io.IOException;
import java.net.Socket;

import model.Alarm;

import org.apache.log4j.Logger;

public class Scheduler implements Runnable {

	private final Socket client;
	final Logger LOGGER = Logger.getLogger(getClass());
	private final RaspyLarmServer server;

	public Scheduler(Socket client, RaspyLarmServer server) {
		this.client = client;
		this.server = server;
	}

	public void run() {
		Alarm alarm = null;
		try {
			alarm = server.readAlarmTask(client);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.debug("Alarm " + alarm + " connected.");
		if (alarm.isActivatePrompt()) {
			new AlarmTimer(alarm).start();
			return;
		}
		if (alarm.isActive()) {
			LOGGER.debug("Call server startAlarm");
			server.startAlarmSheduling(alarm);
		} else {
			LOGGER.debug("Call server stopAlarm");
			server.stopAlarm(alarm);
		}
	}
}