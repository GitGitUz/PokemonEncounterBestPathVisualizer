package model;

import java.io.IOException;
import javafx.fxml.*;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		launch(args); 
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/Layout.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Pokemon Path Visualizer");
			primaryStage.setScene(scene);				
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
