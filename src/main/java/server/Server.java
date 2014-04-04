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

public class Server {

	private final HashMap<Alarm, AlarmSheduler> runningAlarmTasks;
	private final int port;

	public Server(int port) {
		this.port = port;
		runningAlarmTasks = new HashMap<Alarm, AlarmSheduler>();
	}

	public Socket waitForConnection() throws IOException {
		ServerSocket server = new ServerSocket(port);
		return server.accept();
	}

	public Alarm readAlarmTask(Socket client) throws IOException, ClassNotFoundException {
		ObjectInputStream reader = new ObjectInputStream(client.getInputStream());
		return (Alarm) reader.readObject();
	}

	public void startAlarmSheduling(Alarm alarm) {
		final AlarmSheduler alarmSheduler = new AlarmSheduler(alarm);
		runningAlarmTasks.put(alarm, alarmSheduler);
		alarmSheduler.run();
	}

	public void stopAlarm(Alarm alarm) {
		final AlarmSheduler alarmSheduler = runningAlarmTasks.get(alarm);
		if (alarmSheduler != null) {
			alarmSheduler.cancel();
		}
	}

	private class AlarmSheduler implements Runnable {

		private final Alarm alarm;
		private boolean canceled;

		public AlarmSheduler(Alarm alarm) {
			this.alarm = alarm;
		}

		public void run() {
			shedule();

			if (!canceled) {
				shedule();
				startAlarmRing();
			}
		}

		private void shedule() {
			int currentHour = Calendar.HOUR_OF_DAY;
			int currentMinute = Calendar.MINUTE;
			int currentDay = Calendar.DAY_OF_WEEK;

			List<Boolean> days = Arrays.asList(alarm.getAlertDays());
			boolean rightHour = currentHour != alarm.getAlertHour();
			boolean rightMinute = currentMinute != alarm.getAlertMinute();
			boolean rightDay = !days.contains(currentDay);

			while (!canceled && rightHour && rightMinute && rightDay) {
				System.out.println("shedule");
				currentHour = Calendar.HOUR_OF_DAY;
				currentMinute = Calendar.MINUTE;
				currentDay = Calendar.DAY_OF_WEEK;

				rightHour = currentHour != alarm.getAlertHour();
				rightMinute = currentMinute != alarm.getAlertMinute();
				rightDay = !days.contains(currentDay);
			}
		}

		private void startAlarmRing() {
			try {
				Runtime.getRuntime().exec(alarm.getAlarmCommand());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void cancel() {
			canceled = true;
		}
	}
}
