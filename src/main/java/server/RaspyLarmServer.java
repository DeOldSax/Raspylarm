package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import model.Alarm;

import org.apache.log4j.Logger;

public class RaspyLarmServer {

	final Logger LOGGER = Logger.getLogger(getClass());
	private final HashMap<Alarm, AlarmTimer> runningAlarmTasks;
	private ServerSocket server;

	protected RaspyLarmServer(int port) {
		LOGGER.info("Server initialized with port: " + port);
		runningAlarmTasks = new HashMap<Alarm, AlarmTimer>();
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Socket waitForConnection() throws IOException {
		return server.accept();
	}

	protected Alarm readAlarmTask(Socket client) throws IOException, ClassNotFoundException {
		ObjectInputStream reader = new ObjectInputStream(client.getInputStream());
		return (Alarm) reader.readObject();
	}

	protected void startAlarmSheduling(Alarm alarm) {
		AlarmTimer oldAlarm = runningAlarmTasks.get(alarm); 
		if (oldAlarm != null) {
			oldAlarm.cancel();
			runningAlarmTasks.remove(alarm); 
		}
		final AlarmTimer alarmTimer = new AlarmTimer(alarm);
		runningAlarmTasks.put(alarm, alarmTimer);
		alarmTimer.start();
	}

	protected void stopAlarm(Alarm alarm) {
		final AlarmTimer alarmTimer = runningAlarmTasks.get(alarm);
		if (alarmTimer != null) {
			alarmTimer.cancel();
			LOGGER.debug("Call alarm cancel method.");
			runningAlarmTasks.remove(alarm); 
		} else {
			LOGGER.error("Alarm " + alarm.toString() + " was cancel, but wasn't activated.");
		}
	}
}
