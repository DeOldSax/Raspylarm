package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ArgumentManager;

import org.apache.log4j.PropertyConfigurator;

public class RaspyLarmUI extends Application {

	public static void main(String[] args) {
		PropertyConfigurator.configure(RaspyLarmUI.class.getResourceAsStream("log4j.properties"));
		ArgumentManager.getInstance().setArguments(args);
		launch();
	}

	@Override
	public void start(Stage stage) throws Exception {
		final Parent root = FXMLLoader.load(getClass().getResource("raspylarm.fxml"));
		Scene scene = new Scene(root);
		stage.setTitle("Raspylarm");
		stage.setScene(scene);
		stage.show();
	}
}
