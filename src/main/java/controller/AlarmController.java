package controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Alarm;
import model.AlarmManager;

import org.codefx.nesting.Nestings;
import utils.TimeStringConverter;

public class AlarmController {

	private final String ALARM_DAY_ACTIVE_COLOR = "-fx-background-color: red";
	final String ALARM_DAY_INACTIVE_COLOR = "-fx-background-color: white";
	private final Button[] weekdays;
	private ObjectProperty<Alarm> alarmProperty;
	private final TextField alarmName;
	private final TextField alarmCommand;
	private final TextField alarmHour;
	private final TextField alarmMinute;

	public AlarmController(Button monday, Button tuesday, Button wednesday, Button thursday, Button friday, Button saturday, Button sunday,
			Button plusHour, Button plusMinute, Button minusHour, Button minusMinute, TextField alarmName, TextField alarmCommand,
			TextField alarmHour, TextField alarmMinute, Button executeCommand, Button save) {

		this.alarmName = alarmName;
		this.alarmCommand = alarmCommand;
		this.alarmHour = alarmHour;
		this.alarmMinute = alarmMinute;
		weekdays = new Button[7];
		weekdays[0] = monday;
		weekdays[1] = tuesday;
		weekdays[2] = wednesday;
		weekdays[3] = thursday;
		weekdays[4] = friday;
		weekdays[5] = saturday;
		weekdays[6] = sunday;

		for (int i = 0; i < weekdays.length; i++) {
			weekdays[i].setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					toggleAlertDay((Button) event.getSource());
				}
			});
		}

		plusHour.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				changeAlarmHour(+1);
			}
		});

		minusHour.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				changeAlarmHour(-1);
			}
		});

		plusMinute.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				changeAlarmMinute(+1);
			}
		});

		minusMinute.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				changeAlarmMinute(-1);
			}
		});

		executeCommand.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				AlarmManager.getInstance().ring(alarmProperty.getValue()); 
			}
		});
		setAlarmSettingsPartVisible(false);
	}

	private void setAlarmSettingsPartVisible(boolean b) {
		// TODO
	}

	private void changeAlarmHour(int i) {
		alarmProperty.getValue().alarmHourProperty().setValue((Integer)alarmProperty.getValue().alarmHourProperty().getValue() + i);
	}

	private void changeAlarmMinute(int i) {
		alarmProperty.getValue().alarmMinuteProperty().setValue((Integer)alarmProperty.getValue().alarmMinuteProperty().getValue() + i);
	}

	private void toggleAlertDay(Button clickedDay) {
		if (alarmProperty.getValue() != null) {
			for (int j = 0; j < weekdays.length; j++) {
				if (clickedDay.equals(weekdays[j])) {
					final boolean oldValue = alarmProperty.getValue().getAlertDays()[j];
					alarmProperty.getValue().getAlertDays()[j] = !oldValue;
					if (!oldValue) {
						clickedDay.setStyle(ALARM_DAY_ACTIVE_COLOR);
					} else {
						clickedDay.setStyle(ALARM_DAY_INACTIVE_COLOR);
					}
				}
			}
		}
	}
	

	private void setAlertDays() {
		for (int i = 0; i < weekdays.length; i++) {
			final boolean alarmDayIsActive = alarmProperty.getValue().getAlertDays()[i];
			if (alarmDayIsActive) {
				weekdays[i].setStyle(ALARM_DAY_ACTIVE_COLOR);
			} else {
				weekdays[i].setStyle(ALARM_DAY_INACTIVE_COLOR);
			}
		}
	}

	protected Alarm createNewAlarm() {
		final Alarm newAlarm = new Alarm();
		System.out.println(Alarm.getNumberOfAlarms());
		AlarmManager.getInstance().getAlarms().add(newAlarm);
		if (Alarm.getNumberOfAlarms() == 1) {
			bind(newAlarm); 
		}
		return newAlarm; 
	}
	
	public void setAlarm(Alarm newAlarm) {
		if (newAlarm == null) {
			System.out.println("new alarm is null");
		}
		if (alarmProperty.getValue() == null) {
			System.out.println("oldalarm is null");
		}
		alarmProperty.setValue(newAlarm);
		setAlertDays();
	}

	/**
	 * Code for Nestings used from
	 * <a href="https://github.com/nicolaiparlog">nicolaiparlog</a>'s 
	 * <a href="https://github.com/nicolaiparlog/codefx">codefx</a> project.
	 * 
	 * <br>Attached codefx dependecy was created from <a href="https://github.com/nicolaiparlog/codefx/tree/feature/nesting" >nested branch</a>.
	 * 
	 * 
	 * @param alarm
	 */
	private void bind(Alarm alarm) {
		alarmProperty = new SimpleObjectProperty<Alarm>(alarm); 

		// bind alarmCommand
//		NestedObjectProperty<Alarm, String> command = new NestedObjectProperty<Alarm, String>(
//			alarmProperty,
//				new Java8F<Alarm, Property<String>>() {
//					@Override
//					public Property<String> apply(Alarm value) {
//						return value.commandProperty();
//					}
//				}, true); 
		Property<String> buildProperty = Nestings.on(alarmProperty).nest(alarmProperty -> alarmProperty.commandProperty()).buildProperty();
		alarmCommand.textProperty().bindBidirectional(buildProperty);
		
		// bind alarmName
//		NestedObjectProperty<Alarm, String> name = new NestedObjectProperty<Alarm, String>(alarmProperty,
//				new Java8F<Alarm, Property<String>>() {
//				@Override
//					public Property<String> apply(Alarm value) {
//						return value.nameProperty();
//					}
//				}, true); 
		Property<String> nestedName = Nestings.on(alarmProperty).nest(alarmProperty -> alarmProperty.nameProperty()).buildProperty();
		alarmName.textProperty().bindBidirectional(nestedName);
		
		// hour binding
//		NestedObjectProperty<Alarm, Number> nestedAlarmHour = new NestedObjectProperty<Alarm, Number>(alarmProperty,
//				new Java8F<Alarm, Property<Number>>() {
//				@Override
//					public Property<Number> apply(Alarm value) {
//						return value.alarmHourProperty(); 
//					}
//				}, true); 
		TimeStringConverter hourConverter = new TimeStringConverter(alarmHour, 0, 23);
		Property<Number> nestedAlarmHour = Nestings.on(alarmProperty).nest(alarmProperty -> alarmProperty.alarmHourProperty()).buildProperty();
		Bindings.bindBidirectional(alarmHour.textProperty(), nestedAlarmHour, hourConverter);
		
		// minute binding
//		NestedObjectProperty<Alarm, Number> nestedAlarmMinute = new NestedObjectProperty<Alarm, Number>(alarmProperty,
//				new Java8F<Alarm, Property<Number>>() {
//				@Override
//					public Property<Number> apply(Alarm value) {
//						return value.alarmMinuteProperty(); 
//					}
//				}, true); 
		Property<Number> nestedAlarmMinute = Nestings.on(alarmProperty).nest(alarmProperty -> alarmProperty.alarmMinuteProperty()).buildProperty();
		TimeStringConverter minuteConverter = new TimeStringConverter(alarmMinute, 0, 59);
		Bindings.bindBidirectional(alarmMinute.textProperty(), nestedAlarmMinute, minuteConverter);
	}
}
