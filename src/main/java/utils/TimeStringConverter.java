package utils;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class TimeStringConverter extends StringConverter<Number> {
	private final TextField alarmField;
	private final int minTime;
	private final int maxTime;
	private final IntegerProperty timeProperty;

	public TimeStringConverter(IntegerProperty timeProperty, TextField alarmField, int minTime, int maxTime) {
		this.timeProperty = timeProperty;
		this.alarmField = alarmField;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	@Override
	public Number fromString(String arg0) {
		Integer value = Integer.valueOf(arg0);
		if (value < minTime || value > maxTime) {
			timeProperty.set(0);
			value = 0;
			alarmField.setText(String.valueOf("0" + value));
		}
		return value;
	}

	@Override
	public String toString(Number value) {
		if ((Integer) value == maxTime + 1) {
			value = 00;
			timeProperty.set(0);
			return "00";
		} else if ((Integer) value == minTime - 1) {
			timeProperty.set(maxTime);
			return String.valueOf(maxTime);
		}
		if ((Integer) value < 10) {
			return String.valueOf("0" + value);
		}
		return String.valueOf(value);
	}
}
