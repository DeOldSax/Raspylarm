package server;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import model.Alarm;

import org.apache.log4j.Logger;

class AlarmTimer extends Timer {

	private final Logger LOGGER = Logger.getLogger(getClass());
	private final Alarm alarm;
	private long delay;

	public AlarmTimer(Alarm alarm) {
		this.alarm = alarm;
		if (alarm.isActivatePrompt()) {
			delay = 0;
		} else {
			final int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			final int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
			final int currentSecond = Calendar.getInstance().get(Calendar.SECOND);
			int alarmHour = alarm.getAlarmHour();
			int alarmMinute = alarm.getAlarmMinute();

			delay = (alarmHour - currentHour) * 60 * 60 * 1000;
			delay += (alarmMinute - currentMinute) * 60 * 1000;
			delay += -currentSecond * 1000;
		}
	}

	public void start() {
		super.schedule(new AlarmRinger(), delay);
		LOGGER.info("Alarm " + alarm.toString() + " will be executed in " + delay + " milliseconds.");
	}

	@Override
	public void cancel() {
		super.cancel();
		Logger.getLogger(getClass()).debug(
				"Alarm " + alarm.toString() + "<time <" + alarm.getAlarmHour() + "><" + alarm.getAlarmMinute() + "> canceled.");
	}

	private class AlarmRinger extends TimerTask {
		@Override
		public void run() {
			LOGGER.debug("Execute alarmCommand: " + alarm.getCommand() + ". Current Time: " + Calendar.getInstance().getTime());
			try {
				Runtime.getRuntime().exec(alarm.getCommand());
			} catch (IOException e) {
				LOGGER.error("Executing alarmCommand failed.", e);
			}
		}
	}
}