import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;

public class Controller {
	
	@FXML
	GridPane terrainGrid;
	@FXML	
	private Button clearBtn, resetBtn, searchBtn;
	@FXML
	private ComboBox<String> tileChoices, terrainChoices;
	
	@FXML
	public void initialize() {
		tileChoices.setItems(FXCollections.observableArrayList("Source", "Goal", "Wall", "Erase"));
		terrainChoices.setItems(FXCollections.observableArrayList("Safe", "Route", "Short Grass", "Tall Grass", "Cave", "Desert Sand"));

	}
	
	public void btnController() {
		
		clearBtn.setOnAction(e -> {
			System.out.println("Clear");
		});
		resetBtn.setOnAction(e -> {
			System.out.println("Reset");
		});
		searchBtn.setOnAction(e -> {
			System.out.println("Search");
		});
		tileChoices.setOnAction(e -> {
			System.out.println(tileChoices.getSelectionModel().getSelectedItem());
		});
	}
}
