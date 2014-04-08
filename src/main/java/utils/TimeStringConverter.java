package utils;

import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class TimeStringConverter extends StringConverter<Number> {
	private final TextField alarmField;
	private final int minTime;
	private final int maxTime;

	public TimeStringConverter(TextField alarmField, int minTime, int maxTime) {
		this.alarmField = alarmField;
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	@Override
	public Number fromString(String arg0) {
		Integer value = Integer.valueOf(arg0);
		if (value < minTime || value > maxTime) {
			value = 0;
			alarmField.setText(String.valueOf("0" + value));
			alarmField.setStyle("-fx-background-color: red");
		} else {
			alarmField.setStyle(null);
		}
		return value;
	}

	@Override
	public String toString(Number value) {
		if ((Integer) value == maxTime + 1) {
			value = 00;
			return "00";
		} else if ((Integer) value == minTime - 1) {
			return String.valueOf(maxTime);
		}
		if ((Integer) value < 10) {
			return String.valueOf("0" + value);
		}
		return String.valueOf(value);
	}
}
