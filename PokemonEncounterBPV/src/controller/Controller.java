package controller;

import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.SearchAlgo;
import model.Tile;
import model.TileType;

public class Controller {
	
	
	private static final int dimensions = 10;
	private static final int T = (int) Math.pow(dimensions, 2);
	
	private Integer sourceX;
	private Integer sourceY;
	private int goalX;
	private int goalY;
	private boolean searching;
	Map<String, String> imagePaths;
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
		
		linkImagePaths(imagePaths);
		resetMap();
		
		algo.printAdjacencyList();

		clearBtn.setOnMouseClicked(e->{clearClick();});
		resetBtn.setOnMouseClicked(e->{resetClick();});
		searchBtn.setOnMouseClicked(e->{searchClick();});
		terrainGrid.setOnMouseClicked(e ->{gridClick(e);});
		terrainChoices.setOnAction(e->{terrainChosen();});
		tileChoices.setOnAction(e->{tileChosen();});
	}
	
	public void clearClick() {
		System.out.println("Clear");
	}
	
	public void resetClick() {
		resetMap();
		terrainChoices.getSelectionModel().clearSelection();
		tileChoices.getSelectionModel().clearSelection();
		System.out.println("Reset");
	}
	
	public void searchClick() {
		System.out.println("Search");
		//should have at least one tile on the board
	}
	
	public void gridClick(MouseEvent e){

		if(terrainChoices.getValue()!= null || tileChoices.getValue() != null) {
			Node n = (Node) e.getTarget();
			if(n instanceof ImageView) {
				
				//logic for different combobox choices here
				
				String terrain = terrainChoices.getValue();
				String tile = tileChoices.getValue();
				
				if(terrain!= null) {
					changeTerrain(n, terrain);
				}else {
					changeTile(n, tile);
				}
				
//				algo.printAdjacencyList();
//				System.out.println(isCellOccupied(terrainGrid, col, row));
				System.out.println("Grid Nodes: "+ terrainGrid.getChildren().size());
			}
		}
		
	}
	
	public void terrainChosen() {
		if(terrainChoices.getValue() != null) {
			tileChoices.getSelectionModel().clearSelection();
			//add functionality to preview image for user before they click on grid
			System.out.println("Tile RESET");
//			System.out.println(terrainChoices.getSelectionModel().getSelectedItem());
		}
	}
	
	public void tileChosen() {
		if(tileChoices.getValue() != null) {
			terrainChoices.getSelectionModel().clearSelection();
			
//			add functionality to preview image for user before they click on grid
			System.out.println("Terrain RESET");
//			System.out.println(tileChoices.getSelectionModel().getSelectedItem());
		}
	}

	private void changeTerrain(Node n, String terrain) {
		int col = GridPane.getColumnIndex(n);
		int row = GridPane.getRowIndex(n);
		
		//if user tries to set a tile to a type it already is, show an alert
		if(algo.tileGrid[row][col].getTileType().equalsIgnoreCase(terrain.replaceAll("\\s","")) || (algo.tileGrid[row][col].getTileType().equalsIgnoreCase("null") && terrain.equalsIgnoreCase("wall"))) {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("Invalid Tile Placement");
			a.setHeaderText("The selected tile is already a " + terrain);
			a.setContentText("Choose a different terrain or tile to place a " + terrain);
			a.show();
		}else {
			
			ImageView imageView = getImageView(imagePaths.get(terrain.replaceAll("\\s","").toUpperCase()));
			
			terrainGrid.getChildren().remove(n);
			terrainGrid.add(imageView, col, row);
			
			//increase number of wall tiles in grid only if 
			if(terrain.equalsIgnoreCase("wall")) {
				algo.inceaseWallTiles();
			}else {
				algo.decreaseWallTiles();
			}
			System.out.println(algo.getWallTiles());

			algo.tileGrid[row][col].setTileType(terrain.replaceAll("\\s",""));
		}
		
		
	}
	
	private void changeTile(Node n, String tile) {
		int col = GridPane.getColumnIndex(n);
		int row = GridPane.getRowIndex(n);
		
		if((tile.equalsIgnoreCase("Source") || tile.equalsIgnoreCase("Goal")) && (algo.tileGrid[row][col].getCellType() != 0)) {
			
		}
	}
	
	private ImageView getImageView(String fp) {
		Image i = new Image(fp);
		ImageView imageView = new ImageView(i);
        imageView.fitHeightProperty().bind(terrainGrid.heightProperty().subtract(25).divide(10));
        imageView.fitWidthProperty().bind(terrainGrid.widthProperty().subtract(25).divide(10));
        imageView.setPreserveRatio(false);
        return imageView;
	}
			
	private static boolean isCellOccupied(GridPane gridPane, int column, int row){
	    return gridPane.getChildren().stream().filter(Node::isManaged).anyMatch(n -> Objects.equals(GridPane.getRowIndex(n), row) && Objects.equals(GridPane.getColumnIndex(n), column));
	}
	
	//resets map and data structures to initial state with all tiles as walls (empty white squares)
	private void resetMap() {
		algo = new SearchAlgo(T);
		int id = 0;
		for (int x = 0; x < dimensions; x++) {
		    for (int y = 0; y < dimensions; y++) {
		    	Tile t = new Tile(id,x,y,0,"wall");
//				initially, all tiles will be walls aka empty white squares
		    	algo.tileGrid[x][y] = t;
		    	List<Tile> tile = new ArrayList<>();
//				create adjacency list for all tiles (NOTE: "wall" neighbors will be ignored during Dijkstra)
				algo.tile_AdjList.add(tile);
//				map of tiles with their IDs as keys
				algo.tilesMap.put(t.getTileID(), t); 
				
		    	ImageView imageView = getImageView(imagePaths.get("WALL"));
 		        terrainGrid.add(imageView, y, x);
 		        id++;
		    }
		}
		
		algo.populateAdjacencyList();
		algo.printAdjacencyList();
		
		sourceX = -1;
		sourceY = -1;
		goalX = -1;
		goalY = -1;
		searching = false;
	}
	
	private void linkImagePaths(Map<String, String> images) {
		imagePaths = new HashMap<>();
		for(TileType tt : TileType.values()) {
			imagePaths.put(tt.toString(), tt.getFilePath());
		}
	}
	

}
