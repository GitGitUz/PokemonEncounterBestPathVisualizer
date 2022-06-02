package controller;

import java.util.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import model.SearchAlgo;
import model.Tile;
import model.TileType;

public class Controller {
	
	private static final int dimensions = 10;
	private static final int T = (int) Math.pow(dimensions, 2);
	
	private int sourceX;
	private int sourceY;
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
		searching = true;
		if((sourceX == -1 && sourceY == -1) || (goalX == -1 && goalY == -1)) {	//Search not ran if there isn't at least one source/goal tile
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Cannot Find Best Path");
			a.setHeaderText("No source/goal tile specified");
			a.setContentText("Please place at least one source and one goal tile");
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a.show();
		}else {		//valid run whether goal reachable or not
			System.out.println();
			System.out.println("REACHED VALID RUN!!!");
			System.out.printf("Source: %d   SourceX: %d   SourceY: %d\n",algo.tileGrid[sourceX][sourceY].getTileID(), sourceX, sourceY);
			System.out.printf("Goal: %d    GoalX: %d   GoalY: %d\n",algo.tileGrid[goalX][goalY].getTileID(), goalX, goalY);
			algo.dijsktra(algo.tile_AdjList, algo.tileGrid[sourceX][sourceY].getTileID(), algo.tileGrid[goalX][goalY].getTileID());
			
			algo.resetDijkstra();
		} 
	}
	
	public void gridClick(MouseEvent e){

		if(terrainChoices.getValue()!= null || tileChoices.getValue() != null) {
			Node n = (Node) e.getTarget();
			if(n instanceof ImageView) {
				
				System.out.printf("Clicked On: R[%d]C[%d]\n", GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
				
				//logic for different combobox choices here
				
				String terrain = terrainChoices.getValue();
				String tile = tileChoices.getValue();
				
				if(terrain!= null) {
					changeTerrain(n, terrain);
				}else {
					changeTile(n, tile);
				}
				
				algo.printAdjacencyList();
//				System.out.println(isCellOccupied(terrainGrid, col, row));
				System.out.println("Grid Nodes: "+ terrainGrid.getChildren().size());
				System.out.println();
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
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a.show();
		}else {
			
			ImageView imageView = getImageView(imagePaths.get(terrain.replaceAll("\\s","").toUpperCase()));
			
			terrainGrid.getChildren().remove(n);
			terrainGrid.add(imageView, col, row);
			
			//increase number of wall tiles in grid only if 
			if(terrain.equalsIgnoreCase("wall")) {
				if(algo.tileGrid[row][col].getCellType()==2) {		//changing a source to a wall should reset the source coordinates
					sourceX = -1;
					sourceY = -1;
				}else if(algo.tileGrid[row][col].getCellType() == 3){	//changing a goal to a wall should reset the goal coordinates
					goalX = -1;
					goalY = -1;
				}
			}
			algo.tileGrid[row][col].setTileType(terrain.replaceAll("\\s",""));
		}
		
		
	}
	
	private void changeTile(Node n, String tile) {
		int col = GridPane.getColumnIndex(n);
		int row = GridPane.getRowIndex(n);
		
		//walls cannot be a source or a goal
		if(algo.tileGrid[row][col].getCellType() != 0) {
			
			InnerShadow is = new InnerShadow(BlurType.ONE_PASS_BOX, null, 0, 1, 0, 0 );
			is.setHeight(25);
			is.setWidth(25);
			
			//placing a valid tile 		CellType: 	1-> 2/3 	2->2/3 	   3->3/2
			
			System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
			System.out.printf("GoalX: %d    GoalY: %d\n\n\n",goalX,goalY);
			
			
			//if a source is set to goal or vice versa reset old values to -1
			if(tile.equalsIgnoreCase("Source")){	//setting a proper source node				
				is.setColor(Color.GREEN);
				
				if(goalX == row && goalY == col) {		//if setting a goal to source, reset goal values
					goalX = -1;
					goalY = -1;
				}
				
				if(sourceX == -1 & sourceY == -1) {
					algo.tileGrid[row][col].setCellType(2);
					n.setEffect(is);													//sets new source effect to red highlight
					sourceX = row;
					sourceY = col;
					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);

				}else {
					algo.tileGrid[sourceX][sourceY].setCellType(1);
					algo.tileGrid[row][col].setCellType(2);
					
					Node s = getNodeFromGridPane(terrainGrid, sourceY, sourceX);		//sets previous source effect to null
					System.out.printf("OLD SOURCE: %d %d\n", GridPane.getRowIndex(s), GridPane.getColumnIndex(s));
					s.setEffect(null);
					
					n.setEffect(is);														//sets new source effect to red highlight
					
					sourceX = row;
					sourceY = col;
					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);

				}
			}else {		//setting a proper goal node				
				is.setColor(Color.RED);
				
				if(sourceX == row && sourceY == col) {		//if setting a source to goal, reset source coordinates
					sourceX = -1;
					sourceY = -1;
				}
				
				if(goalX == -1 & goalY == -1) {
					algo.tileGrid[row][col].setCellType(3);
					n.setEffect(is);													//sets new goal effect to green highlight
					goalX = row;
					goalY = col;
					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);

				}else {
					algo.tileGrid[goalX][goalY].setCellType(1);
					algo.tileGrid[row][col].setCellType(3);
					
					Node g = getNodeFromGridPane(terrainGrid, goalY, goalX);
					System.out.printf("OLD GOAL: %d %d\n", GridPane.getRowIndex(g), GridPane.getColumnIndex(g));
					g.setEffect(null);//sets previous goal effect to null
					
					n.setEffect(is);													//sets new goal effect to green highlight
					
					goalX = row;
					goalY = col;
					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);
				}
			}
		}else {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Invalid " + tile +" Placement");
			a.setHeaderText("A wall cannot be set as a " + tile);
			a.setContentText("Choose a valid terrain tile to make a " + tile);
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a.show();
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
			
//	private static boolean isCellOccupied(GridPane gridPane, int column, int row){
//	    return gridPane.getChildren().stream().filter(Node::isManaged).anyMatch(n -> Objects.equals(GridPane.getRowIndex(n), row) && Objects.equals(GridPane.getColumnIndex(n), column));
//	}
	
	//resets map and data structures to initial state with all tiles as walls (empty white squares)
	private void resetMap() {
		algo = new SearchAlgo(T);
		int id = 0;
		if(terrainGrid.getChildren().size() > 1) {
    		terrainGrid.getChildren().removeAll(terrainGrid.getChildren());
    	}
	    System.out.println(terrainGrid.getChildren().size());
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
	
	private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
	    ObservableList<Node> children = gridPane.getChildren();
	    for (Node node : children) {
	        Integer columnIndex = GridPane.getColumnIndex(node);
	        Integer rowIndex = GridPane.getRowIndex(node);

	        if (columnIndex == null)
	            columnIndex = 0;
	        if (rowIndex == null)
	            rowIndex = 0;

	        if (columnIndex == col && rowIndex == row) {
	            return node;
	        }
	    }
	    System.out.println("im returning null lol");
	    return null;
	}
	

}
