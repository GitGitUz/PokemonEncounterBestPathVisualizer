import java.io.IOException;
import java.util.*;
import javafx.fxml.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application{
	
	private int dimensions = 20;
	private int tilesChecked = 0;
	private int pathLength = 0;
	private int sourceX = -1;
	private int sourceY = -1;
	private int goalX = -1;
	private int goalY = -1;
	private boolean searching = false;
		
	Tile[][] terrain;
	Random rand = new Random();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Pokemon Path Visualizer");
			primaryStage.setScene(scene);				
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
