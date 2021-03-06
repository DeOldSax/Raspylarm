package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Alarm;

public class Controller implements Initializable {

	@FXML
	private Button activateAlarmOnRaspy;

	@FXML
	private Button createNewAlarm;

	@FXML
	private ListView<Alarm> alarms;

	@FXML
	private TextField alarmName;

	@FXML
	private Button monday;

	@FXML
	private Button tuesday;

	@FXML
	private Button wednesday;

	@FXML
	private Button thursday;

	@FXML
	private Button friday;

	@FXML
	private Button saturday;

	@FXML
	private Button sunday;

	@FXML
	private Button plusHour;

	@FXML
	private Button plusMinute;

	@FXML
	private Button minusHour;

	@FXML
	private Button minusMinute;

	@FXML
	private TextField alarmCommand;

	@FXML
	private TextField alarmHour;

	@FXML
	private TextField alarmMinute;

	@FXML
	private Button executeCommand;

	@FXML
	private Button save;
	
	@FXML
	private Button synchronizeButton; 

	private AlarmController alarmController;
	
	private RaspyLarmUIController uiController; 

	public void initialize(URL url, ResourceBundle rb) {
		uiController = new RaspyLarmUIController(this, createNewAlarm, activateAlarmOnRaspy, synchronizeButton, alarms);
		alarmController = new AlarmController(monday, tuesday, wednesday, thursday, friday, saturday, sunday, plusHour, plusMinute,
				minusHour, minusMinute, alarmName, alarmCommand, alarmHour, alarmMinute, executeCommand, save);
	}

	public AlarmController getAlarmController() {
		return alarmController;
	}
	
	public RaspyLarmUIController getUiController() {
		return uiController;
	}
	

}
