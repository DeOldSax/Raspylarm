package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import model.Alarm;
import model.Alarms;

public class RaspyLarmUIController {

	private final Controller controller;

	public RaspyLarmUIController(final Controller controller, Button createNewAlarm, Button activateAlarmOnRaspy,
			final ListView<Alarm> alarms) {

		this.controller = controller;
		createNewAlarm.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				createNewAlarm();
			}
		});

		alarms.setItems(Alarms.getInstance().getAlarms());
		alarms.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Alarm>() {
			public void changed(ObservableValue<? extends Alarm> observableValue, Alarm oldValue, Alarm newValue) {
				controller.getAlarmController().setAlarm(newValue);
			}
		});
		alarms.setEditable(true);

		activateAlarmOnRaspy.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				activateAlarmOnRaspy();
			}
		});

	}

	private void activateAlarmOnRaspy() {
		new Thread(new RaspController()).start();
	}

	private void createNewAlarm() {
		final Alarm newAlarm = new Alarm();
		Alarms.getInstance().getAlarms().add(newAlarm);
		controller.getAlarmController().setAlarm(newAlarm);
	}

	private class EditingListCell extends ListCell<Alarm> {

	}

}
