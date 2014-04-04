package model;

import java.io.Serializable;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Alarm implements Serializable {

	private static final long serialVersionUID = 1729409145617866229L;
	private static int numberObAlarms = 0;
	private boolean active;
	private transient final StringProperty alertTime;
	private transient final StringProperty alarmName;
	private final Boolean[] alertDays;

	public Alarm() {
		numberObAlarms++;
		alertTime = new SimpleStringProperty();
		alarmName = new SimpleStringProperty("Alarm " + numberObAlarms);
		alertDays = new Boolean[7];
		for (int i = 0; i < alertDays.length; i++) {
			alertDays[i] = true;
		}
	}

	public StringProperty alertTimeProperty() {
		return alertTime;
	}

	public Boolean[] getAlertDays() {
		return alertDays;
	}

	public Property<String> nameProperty() {
		return alarmName;
	}

	public int getAlertHour() {
		return 0;
	}

	public int getAlertMinute() {
		return 0;
	}

	public String getAlarmCommand() {
		return null;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(Boolean newValue) {
		this.active = newValue;
	}

	@Override
	public String toString() {
		return alarmName.get();
	}

}