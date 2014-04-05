package controller;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Alarm;
import utils.TimeStringConverter;

public class AlarmController {

	private final String ALARM_DAY_ACTIVE_COLOR = "-fx-background-color: red";
	final String ALARM_DAY_INACTIVE_COLOR = "-fx-background-color: white";
	private final Button[] weekdays;
	private Alarm alarm;
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
				alarm.ring();
			}
		});

		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				alarm.synchronize();
			};
		});
		setAlarmSettingsPartVisible(false);
	}

	private void setAlarmSettingsPartVisible(boolean b) {
		// TODO
	}

	private void changeAlarmHour(int i) {
		this.alarm.alarmHourProperty().set(alarm.alarmHourProperty().get() + i);
	}

	private void changeAlarmMinute(int i) {
		this.alarm.alarmMinuteProperty().set(alarm.alarmMinuteProperty().get() + i);
	}

	private void toggleAlertDay(Button clickedDay) {
		if (alarm != null) {
			for (int j = 0; j < weekdays.length; j++) {
				if (clickedDay.equals(weekdays[j])) {
					final boolean oldValue = alarm.getAlertDays()[j];
					alarm.getAlertDays()[j] = !oldValue;
					if (!oldValue) {
						clickedDay.setStyle(ALARM_DAY_ACTIVE_COLOR);
					} else {
						clickedDay.setStyle(ALARM_DAY_INACTIVE_COLOR);
					}
				}
			}
		}
	}

	public void setAlarm(final Alarm alarm) {
		// unbdinings
		if (this.alarm != null) {
			this.alarm.activeProperty().unbind();
			this.alarm.alarmHourProperty().unbind();
			this.alarm.alarmMinuteProperty().unbind();
			alarmHour.textProperty().unbind();
			alarmMinute.textProperty().unbind();
		}
		this.alarm = alarm;
		for (int i = 0; i < weekdays.length; i++) {
			final boolean alarmDayIsActive = alarm.getAlertDays()[i];
			if (alarmDayIsActive) {
				weekdays[i].setStyle(ALARM_DAY_ACTIVE_COLOR);
			} else {
				weekdays[i].setStyle(ALARM_DAY_INACTIVE_COLOR);
			}
		}
		alarm.commandProperty().bind(alarmCommand.textProperty());
		TimeStringConverter hourConverter = new TimeStringConverter(this.alarm.alarmHourProperty(), alarmHour, 0, 23);
		TimeStringConverter minuteConverter = new TimeStringConverter(this.alarm.alarmMinuteProperty(), alarmMinute, 0, 59);
		Bindings.bindBidirectional(alarmHour.textProperty(), this.alarm.alarmHourProperty(), hourConverter);
		Bindings.bindBidirectional(alarmMinute.textProperty(), this.alarm.alarmMinuteProperty(), minuteConverter);
	}
}
