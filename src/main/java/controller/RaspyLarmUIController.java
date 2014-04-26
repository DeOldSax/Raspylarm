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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
				Alarm newAlarm = controller.getAlarmController().createNewAlarm();
				alarms.getSelectionModel().select(newAlarm);
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

	private static class AlarmListCell extends ListCell<Alarm> {
		@Override
		protected void updateItem(final Alarm alarm, boolean empty) {
			super.updateItem(alarm, empty);
			if (alarm != null) {
				CheckBox checkBox = new CheckBox(); 
				checkBox.selectedProperty().bindBidirectional(alarm.activeProperty());
				
				Button removeAlarmBtn = createRemoveButton(alarm);
				
				BorderPane box = new BorderPane();

				Label label = new Label();
				label.textProperty().bind(createTimeBinding(alarm));

				Label label2 = new Label(); 
				label2.textProperty().bindBidirectional(alarm.nameProperty());
				
				StackPane pane = new StackPane(); 
				pane.getChildren().addAll(label, removeAlarmBtn); 
				
				box.setLeft(checkBox);
				box.setCenter(label2);
				box.setRight(label);
				setGraphic(box);
			}
		}

		private Button createRemoveButton(final Alarm alarm) {
			final Button removeAlarmBtn = new Button("X"); 
			removeAlarmBtn.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					AlarmManager.getInstance().getAlarms().remove(alarm); 
					
				}
			});
			this.setOnMouseEntered(new EventHandler<MouseEvent>() {

				public void handle(MouseEvent arg0) {
					removeAlarmBtn.setVisible(true);
				}
			});
			this.setOnMouseExited(new EventHandler<MouseEvent>() {

				public void handle(MouseEvent arg0) {
					removeAlarmBtn.setVisible(false);
				}
			});
			removeAlarmBtn.setVisible(false);
			return removeAlarmBtn;
		}

		private StringBinding createTimeBinding(final Alarm alarm) {
			return Bindings.createStringBinding(new Callable<String>() {

				public String call() throws Exception {
					final int hour = (Integer)alarm.alarmHourProperty().getValue();
					String hourString = String.valueOf(hour);
					final int minute = (Integer)alarm.alarmMinuteProperty().getValue();
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
}
