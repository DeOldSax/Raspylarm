package model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Alarm implements Serializable {
	
	private final UUID uuid = UUID.randomUUID();
	private static final long serialVersionUID = 1729409145617866229L;
	private static int numberObAlarms = 0;
	private transient Property<Number> alarmHourProperty;
	private transient Property<Number> alarmMinuteProperty;
	private transient Property<String> alarmNameProperty;
	private transient Property<Boolean> activeProperty;
	private transient Property<String> commandProperty;
	private int alarmHour;
	private int alarmMinute;
	private String alarmName; 
	private boolean active = true;
	private String command;
	private final Boolean[] alertDays;
	private boolean activatePrompt = false;

	public Alarm() {
		numberObAlarms++;
		alarmHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		alarmMinute = Calendar.getInstance().get(Calendar.MINUTE);
		alarmHourProperty = new SimpleIntegerProperty(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		alarmMinuteProperty = new SimpleIntegerProperty(Calendar.getInstance().get(Calendar.MINUTE));
		activeProperty = new SimpleBooleanProperty(true);
		alarmNameProperty = new SimpleStringProperty("Alarm " + numberObAlarms);
		alarmName = alarmNameProperty.getValue(); 
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
		alarmNameProperty.addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				alarmName = arg2; 
			}
		});
	}
	
	private int checkTimeFormat(int time, int minValue, int maxValue, Property<Number> p) {
		int value = (Integer)p.getValue(); 
		if (time == maxValue) {
			p.setValue(0);
		} else if (time == minValue) {
			p.setValue(maxValue-1);
			value = maxValue-1; 
		}
		return value; 
	}

	public Boolean[] getAlertDays() {
		return alertDays;
	}

	public Property<Number> alarmHourProperty() {
		return alarmHourProperty;
	}

	public Property<Number> alarmMinuteProperty() {
		return alarmMinuteProperty;
	}

	public Property<String> nameProperty() {
		return alarmNameProperty;
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
		return alarmName + " [alarmtime: " + alarmHour + ":" + alarmMinute + "]";
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
