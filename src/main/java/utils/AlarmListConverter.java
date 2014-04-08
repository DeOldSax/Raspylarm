package utils;

import javafx.util.StringConverter;
import model.Alarm;

public class AlarmListConverter extends StringConverter<String> {
	private final Alarm alarm;

	public AlarmListConverter(Alarm alarm) {
		this.alarm = alarm;
	}

	@Override
	public String fromString(String string) {
		return string;
	}

	@Override
	public String toString(String string) {
		return alarm.getAlarmHour() + ":" + alarm.getAlarmMinute();
	}

}
