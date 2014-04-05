package server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import model.Alarm;

import org.apache.log4j.Logger;

class AlarmTask implements Runnable {

	private final Logger LOGGER = Logger.getLogger(getClass());
	private final Alarm alarm;
	private boolean canceled;

	public AlarmTask(Alarm alarm) {
		this.alarm = alarm;
	}

	public void run() {
		if (alarm.isActivatePrompt()) {
			startAlarmRing();
			return;
		}
		while (!canceled) {
			shedule();
		}
		Logger.getLogger(getClass()).debug(
				"Alarm " + alarm.toString() + "<time <" + alarm.getAlarmHour() + "><" + alarm.getAlarmMinute() + "> canceled.");
	}

	private void shedule() {
		int currentHour = Calendar.HOUR_OF_DAY;
		int currentMinute = Calendar.MINUTE;
		int currentDay = Calendar.DAY_OF_WEEK;

		List<Boolean> days = Arrays.asList(alarm.getAlertDays());
		boolean rightHour = currentHour == alarm.getAlarmHour();
		boolean rightMinute = currentMinute == alarm.getAlarmMinute();
		boolean rightDay = days.contains(currentDay);

		while (!rightHour && !rightMinute && !rightDay) {
			if (canceled) {
				return;
			}
			currentHour = Calendar.HOUR_OF_DAY;
			currentMinute = Calendar.MINUTE;
			currentDay = Calendar.DAY_OF_WEEK;

			rightHour = currentHour != alarm.getAlarmHour();
			rightMinute = currentMinute != alarm.getAlarmMinute();
			rightDay = !days.contains(currentDay);
		}
		startAlarmRing();
		// FIXME 60 seconds executing the same command...
	}

	private void startAlarmRing() {
		LOGGER.debug("Execute alarmCommand: " + alarm.getCommand() + ".");
		try {
			Runtime.getRuntime().exec(alarm.getCommand());
		} catch (IOException e) {
			LOGGER.error("Executing alarmCommand failed.", e);
		}
	}

	protected void cancel() {
		canceled = true;
	}
}