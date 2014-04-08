package model;

import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import client.RaspyLarmClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AlarmManager {
	private Logger LOGGER = Logger.getLogger(getClass());
	private static AlarmManager instance;
	private final ObservableList<Alarm> alarms = FXCollections.observableArrayList();

	private AlarmManager() {
	}

	public static AlarmManager getInstance() {
		if (instance == null) {
			instance = new AlarmManager();
		}
		return instance;
	}

	public ObservableList<Alarm> getAlarms() {
		return alarms;
	}
	
	public void synchronize() {
		LOGGER.debug("Synchronize " + alarms.size() + " alarms ." + "Date: " + Calendar.getInstance().getTime());
		for (Alarm alarm : alarms) {
			synch(alarm);
		}
	}

	public void synchronize(Alarm alarm) {
		synch(alarm); 
	}

	private void synch(Alarm alarm) {
		try {
			RaspyLarmClient.getInstance().sendToServer(alarm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.debug("Send alarm " + toString() + " to Server.");
	}

	public void ring(Alarm alarm) {
		alarm.setActivatePrompt(true);
		synch(alarm);
		alarm.setActivatePrompt(false);
	}
}
