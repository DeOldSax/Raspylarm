package model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Alarm implements Serializable {
	
	private final UUID uuid = UUID.randomUUID();
	private static final long serialVersionUID = 1729409145617866229L;
	private static int numberObAlarms = 0;
	private boolean active = true;
	private int alarmHour;
	private int alarmMinute;
	private transient IntegerProperty alarmHourProperty;
	private transient IntegerProperty alarmMinuteProperty;
	private transient StringProperty alarmName;
	private transient BooleanProperty activeProperty;
	private final Boolean[] alertDays;
	private boolean activatePrompt = false;
	private transient StringProperty commandProperty;
	private String command;

	public Alarm() {
		numberObAlarms++;
		alarmHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		alarmMinute = Calendar.getInstance().get(Calendar.MINUTE);
		alarmHourProperty = new SimpleIntegerProperty(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		alarmMinuteProperty = new SimpleIntegerProperty(Calendar.getInstance().get(Calendar.MINUTE));
		activeProperty = new SimpleBooleanProperty(true);
		alarmName = new SimpleStringProperty("Alarm " + numberObAlarms);
		commandProperty = new SimpleStringProperty();
		
		alertDays = new Boolean[7];
		for (int i = 0; i < alertDays.length; i++) {
			alertDays[i] = false;
		}

		alarmHourProperty.addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				int newTime = checkTimeFormat((Integer)newValue, -1, 24, alarmHourProperty); 
				alarmHour = newTime; 
			}
		});

		alarmMinuteProperty.addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
				int newTime = checkTimeFormat((Integer)newValue, -1, 60, alarmMinuteProperty); 
				alarmMinute = newTime; 
			}
		});
		commandProperty.addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				command = arg2;
			}
		});
		activeProperty.addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				active = arg2; 
			}
		});
	}
	
	private int checkTimeFormat(int time, int minValue, int maxValue, IntegerProperty p) {
		int value = p.get(); 
		if (time == maxValue) {
			p.set(0);
		} else if (time == minValue) {
			p.set(maxValue-1);
			value = maxValue-1; 
		}
		return value; 
	}

	public Boolean[] getAlertDays() {
		return alertDays;
	}

	public IntegerProperty alarmHourProperty() {
		return alarmHourProperty;
	}

	public IntegerProperty alarmMinuteProperty() {
		return alarmMinuteProperty;
	}

	public Property<String> nameProperty() {
		return alarmName;
	}

	public int getAlarmHour() {
		return alarmHour;
	}

	public void setAlarmHour(int alarmHour) {
		this.alarmHour = alarmHour;
	}

	public int getAlarmMinute() {
		return alarmMinute;
	}

	public void setAlarmMinute(int alarmMinute) {
		this.alarmMinute = alarmMinute;
	}

	public Property<String> commandProperty() {
		return commandProperty;
	}

	public String getCommand() {
		return command;
	}

	public boolean isActive() {
		return active;
	}

	public Property<Boolean> activeProperty() {
		return activeProperty;
	}

	public boolean isActivatePrompt() {
		return activatePrompt;
	}
	
	public void setActivatePrompt(boolean activatePrompt) {
		this.activatePrompt = activatePrompt;
	}

	public static int getNumberOfAlarms() {
		return numberObAlarms;
	}

	@Override
	public String toString() {
		if (alarmName == null) {
			return "";
		}
		return alarmName.get();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Alarm) {
				return uuid.equals(((Alarm) obj).uuid);
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

}
