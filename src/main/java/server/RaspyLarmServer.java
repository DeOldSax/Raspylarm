package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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

	private class AlarmTask implements Runnable {

		private final Alarm alarm;
		private boolean canceled;

		public AlarmTask(Alarm alarm) {
			this.alarm = alarm;
		}

		public void run() {
			while (!canceled) {
				shedule();
			}
			Logger.getLogger(getClass()).debug(
					"Alarm " + alarm.toString() + "<time <" + alarm.getAlertHour() + "><" + alarm.getAlertMinute() + "> canceled.");
		}

		private void shedule() {
			int currentHour = Calendar.HOUR_OF_DAY;
			int currentMinute = Calendar.MINUTE;
			int currentDay = Calendar.DAY_OF_WEEK;

			List<Boolean> days = Arrays.asList(alarm.getAlertDays());
			boolean rightHour = currentHour == alarm.getAlertHour();
			boolean rightMinute = currentMinute == alarm.getAlertMinute();
			boolean rightDay = days.contains(currentDay);

			while (!rightHour && !rightMinute && !rightDay) {
				if (canceled) {
					return;
				}
				currentHour = Calendar.HOUR_OF_DAY;
				currentMinute = Calendar.MINUTE;
				currentDay = Calendar.DAY_OF_WEEK;

				rightHour = currentHour != alarm.getAlertHour();
				rightMinute = currentMinute != alarm.getAlertMinute();
				rightDay = !days.contains(currentDay);
			}
			startAlarmRing();
			// FIXME 60 seconds executing the same command...
		}

		private void startAlarmRing() {
			LOGGER.debug("Execute alarmCommand: " + alarm.getAlarmCommand() + ".");
			try {
				Runtime.getRuntime().exec(alarm.getAlarmCommand());
			} catch (IOException e) {
				LOGGER.error("Executing alarmCommand failed.", e);
			}
		}

		protected void cancel() {
			canceled = true;
		}
	}

}
