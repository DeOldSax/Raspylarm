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
	private final HashMap<Alarm, AlarmTask> runningAlarmTasks;
	private ServerSocket server;

	protected RaspyLarmServer(int port) {
		LOGGER.info("Server initialized with port: " + port);
		runningAlarmTasks = new HashMap<Alarm, AlarmTask>();
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
		final AlarmTask alarmTask = new AlarmTask(alarm);
		System.out.println(alarm.hashCode());
		runningAlarmTasks.put(alarm, alarmTask);
		alarmTask.run();
	}

	protected void stopAlarm(Alarm alarm) {
		final AlarmTask alarmSheduler = runningAlarmTasks.get(alarm);
		if (alarmSheduler != null) {
			LOGGER.debug("Call alarm cancel method.");
			alarmSheduler.cancel();
		} else {
			System.out.println(alarm.hashCode());
			LOGGER.info("Alarm was null");
		}
	}
}
