import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;

public class Controller {
	
	private static final int dimensions = 10;
	private static final int T = (int) Math.pow(dimensions, 2);
	private Integer sourceX = -1;
	private Integer sourceY = -1;
	private int goalX = -1;
	private int goalY = -1;
	private boolean searching = false;
	
	SearchAlgo algo;
	
	@FXML
	private GridPane terrainGrid;
	@FXML	
	private Button clearBtn, resetBtn, searchBtn;
	@FXML
	private ComboBox<String> tileChoices, terrainChoices;
	@FXML
	private Label pLength, tChecked;
	
	@FXML
	public void initialize() {
		
		algo = new SearchAlgo(T);
		
		int id = 0;
		for (int x = 0; x < dimensions; x++) {
		    for (int y = 0; y < dimensions; y++) {
		    	algo.tileGrid[x][y] = new Tile(id,0,"empty");
		    	//will move from here because empty type tiles do not need to have an adjacency list
		    	List<Tile> tile = new ArrayList<Tile>();
				algo.tile_AdjList.add(tile);
				algo.tilesMap.put(id, new Tile(id, 0, "empty")); 
				
		    	ImageView imageView = getImageView("C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Empty.jpg");
 		        terrainGrid.add(imageView, y, x);
 		        System.out.println(id);
 		        id++;
		    }
		}
		
		clearBtn.setOnMouseClicked(e->{clearClick();});
		resetBtn.setOnMouseClicked(e->{resetClick();});
		searchBtn.setOnMouseClicked(e->{searchClick();});
		terrainChoices.setOnAction(e->{terrainChosen();});
		tileChoices.setOnAction(e->{tileChosen();});
		terrainGrid.setOnMouseClicked(e ->{gridClick(e);});
	}
	
	public void clearClick() {
		System.out.println("Clear");
	}
	public void resetClick() {
		System.out.println("Reset");
	}
	public void searchClick() {
		System.out.println("Search");
	}
	public void terrainChosen() {
		System.out.println(terrainChoices.getSelectionModel().getSelectedItem());
	}
	public void tileChosen() {
		System.out.println(tileChoices.getSelectionModel().getSelectedItem());
	}

	public void gridClick(MouseEvent e){
		Node n = (Node) e.getTarget();
		if(n instanceof ImageView) {
			int row = GridPane.getRowIndex(n);
			int col = GridPane.getColumnIndex(n);
			int index = terrainGrid.getChildren().indexOf(n);
			System.out.println("\nImage at Row: "+row+" Col: "+col+" Index: "+index);
			ImageView imageView = getImageView("C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Safe\\s1.png");
			terrainGrid.getChildren().remove(n);
			terrainGrid.add(imageView, col, row);
			algo.tileGrid[row][col].setTileType("Safe");
			algo.tileGrid[row][col].setCellType(1); 
			System.out.println(isCellOccupied(terrainGrid, col, row));
			System.out.println("Grid Nodes: "+ terrainGrid.getChildren().size());
		}
	}
	
	public ImageView getImageView(String filePath) {
		Image i = new Image(filePath);
		ImageView imageView = new ImageView(i);
        imageView.fitHeightProperty().bind(terrainGrid.heightProperty().subtract(20).divide(10));
        imageView.fitWidthProperty().bind(terrainGrid.widthProperty().subtract(20).divide(10));
        imageView.setPreserveRatio(false);
        return imageView;
	}
	
//	public Image getImage(String filePath) {
//		return new Image(filePath);
//	}
			
	private static boolean isCellOccupied(GridPane gridPane, int column, int row){
	    return gridPane.getChildren().stream().filter(Node::isManaged).anyMatch(n -> Objects.equals(GridPane.getRowIndex(n), row) && Objects.equals(GridPane.getColumnIndex(n), column));
	}
}
