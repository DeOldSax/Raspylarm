package controller;

import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Alarm;
import model.AlarmManager;

public class RaspyLarmUIController {

	private ListView<Alarm> alarms;
	private Controller controller;

	public RaspyLarmUIController(final Controller controller, Button createNewAlarm, Button activateAlarmOnRaspy,
			Button synchronizeButton, final ListView<Alarm> alarms) {

		this.controller = controller;
		this.alarms = alarms;
		createNewAlarm.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				controller.getAlarmController().createNewAlarm();
			}
		});

		alarms.setItems(AlarmManager.getInstance().getAlarms());
		alarms.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Alarm>() {
			public void changed(ObservableValue<? extends Alarm> observableValue, Alarm oldValue, Alarm newValue) {
				controller.getAlarmController().setAlarm(newValue);
			}
		});
		alarms.setEditable(true);

		alarms.setCellFactory(new Callback<ListView<Alarm>, ListCell<Alarm>>() {
			public ListCell<Alarm> call(ListView<Alarm> list) {
				return new AlarmListCell();
			}
		});

		activateAlarmOnRaspy.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				activateAlarmOnRaspy();
			}
		});
		
		synchronizeButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				synchronize(); 
			}
		});
	}
	
	private void synchronize() {
		AlarmManager.getInstance().synchronize(); 
	}

	private void activateAlarmOnRaspy() {
		new Thread(new RaspController()).start();
	}

	private class AlarmListCell extends ListCell<Alarm> {
		@Override
		protected void updateItem(final Alarm alarm, boolean empty) {
			super.updateItem(alarm, empty);
			if (alarm != null) {
				BorderPane box = new BorderPane();

				final CheckBox checkBox = new CheckBox();
				checkBox.selectedProperty().bindBidirectional(alarm.activeProperty());

				Label label = new Label();
				label.textProperty().bind(createTimeBinding(alarm));

				Label label2 = new Label(); 
				label2.textProperty().bindBidirectional(alarm.nameProperty());
				
				box.setLeft(checkBox);
				box.setRight(label);
				box.setCenter(label2);
				setGraphic(box);
			}
		}

		private StringBinding createTimeBinding(final Alarm alarm) {
			return Bindings.createStringBinding(new Callable<String>() {

				public String call() throws Exception {
					final int hour = alarm.alarmHourProperty().get();
					String hourString = String.valueOf(hour);
					final int minute = alarm.alarmMinuteProperty().get();
					String minuteString = String.valueOf(minute);
					if (hour < 10) {
						hourString = "0" + hourString;
					}
					if (minute < 10) {
						minuteString = "0" + minuteString;
					}
					return hourString + ":" + minuteString;
				}
			}, alarm.alarmHourProperty(), alarm.alarmMinuteProperty());
		}
	}

	public void select(Alarm alarm) {
		alarms.getSelectionModel().select(alarm);
	}
}
