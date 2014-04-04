package controller;

import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.Alarm;
import client.RaspyLarmClient;

public class AlarmController {

	private final String ALARM_DAY_ACTIVE_COLOR = "-fx-background-color: red";
	final String ALARM_DAY_INACTIVE_COLOR = "-fx-background-color: blue";
	private final Button[] weekdays;
	private Alarm alarm;
	private final TextField alarmName;

	public AlarmController(Button monday, Button tuesday, Button wednesday, Button thursday, Button friday, Button saturday, Button sunday,
			Button plusHour, Button plusMinute, Button minusHour, Button minusMinute, TextField alarmName, CheckBox alarmActivated) {

		this.alarmName = alarmName;
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
					changeAlertDayValue((Button) event.getSource());
				}
			});
		}

		alarmActivated.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if (alarm != null) {
					handleAlarm(newValue);
				}
			}

		});
	}

	private void changeAlertDayValue(Button clickedDay) {
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

	private void handleAlarm(Boolean newValue) {
		alarm.setActive(newValue);
		if (newValue) {
			try {
				RaspyLarmClient.getInstance().startAlarm(alarm);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				RaspyLarmClient.getInstance().stopAlarm(alarm);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setAlarm(Alarm alarm) {
		for (int i = 0; i < weekdays.length; i++) {
			final boolean alarmDayIsActive = alarm.getAlertDays()[i];
			if (alarmDayIsActive) {
				weekdays[i].setStyle(ALARM_DAY_ACTIVE_COLOR);
			} else {
				weekdays[i].setStyle(ALARM_DAY_INACTIVE_COLOR);
			}
		}
		this.alarm = alarm;
	}
}
