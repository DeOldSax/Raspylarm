package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Alarms {
	private static Alarms instance;
	private final ObservableList<Alarm> alarms = FXCollections.observableArrayList();

	private Alarms() {
	}

	public static Alarms getInstance() {
		if (instance == null) {
			instance = new Alarms();
		}
		return instance;
	}

	public ObservableList<Alarm> getAlarms() {
		return alarms;
	}
}
