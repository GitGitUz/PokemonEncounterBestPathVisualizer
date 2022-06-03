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
	
	SearchAlgo algo;
	
	@FXML
	private GridPane terrainGrid;
	@FXML	
	private Button clearBtn, resetBtn, searchBtn;
	@FXML
	private ComboBox<String> setChoices, terrainChoices;
	@FXML
	private Label pLength, eProb;
	
	@FXML
	public void initialize() {
		
		//reset the map and fill it with wall tiles
		resetMap();
		
//		algo.printAdjacencyList();
		
		//setting the listeners on every interactive component
		clearBtn.setOnMouseClicked(e->{clearClick();});
		resetBtn.setOnMouseClicked(e->{resetClick();});
		searchBtn.setOnMouseClicked(e->{searchClick();});
		terrainGrid.setOnMouseClicked(e ->{gridClick(e);});
		terrainChoices.setOnAction(e->{terrainChosen();});
		setChoices.setOnAction(e->{setChosen();});
	}
	
	//A valid clear click resets the visual path of the previous Dijkstra run, does nothing if Dijkstra has not been run
	public void clearClick() {
		//clear the path effects/outline from previous Dijkstra
		if(algo.bestPath.size() != 0) {
//			System.out.println("Num Tiles in Path: "+algo.bestPath.size());
			pLength.setText("Path Length:");
			eProb.setText("Encounter Probability:");
			terrainChoices.setDisable(false);
			setChoices.setDisable(false);
			searchBtn.setDisable(false);
			
			InnerShadow isSrc = new InnerShadow(BlurType.ONE_PASS_BOX, Color.GREEN, 0, 1, 0, 0 );
			isSrc.setHeight(25);
			isSrc.setWidth(25);
			
			for(int i=0; i < algo.bestPath.size(); i++) {
//				System.out.println(algo.bestPath.get(i).getTileID());
				Node temp = getNodeFromGridPane(terrainGrid, algo.bestPath.get(i).getY(), algo.bestPath.get(i).getX());
				if(i == 0) {
					temp.setEffect(isSrc);
				}else if(i == algo.bestPath.size()-1){
					InnerShadow isGoal = new InnerShadow(BlurType.ONE_PASS_BOX, Color.RED, 0, 1, 0, 0 );
					isGoal.setHeight(25);
					isGoal.setWidth(25);
					temp.setEffect(isGoal);
				}else {
					temp.setEffect(null);

				}
			}
			algo.resetDijkstra();
			clearBtn.setDisable(true);
		}
	}
	
	//Resets the map to initial state and resets all common values in the backend
	public void resetClick() {
		resetMap();
		terrainChoices.getSelectionModel().clearSelection();
		setChoices.getSelectionModel().clearSelection();
//		System.out.println("Reset");
	}
	
	//Calls the Dijkstra algorithm if criteria are met
	public void searchClick() {
		if((sourceX == -1 && sourceY == -1) || (goalX == -1 && goalY == -1)) {	//Search not ran if there isn't at least one source/goal tile
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Cannot Find Best Path");
			a.setHeaderText("No source/goal tile specified");
			a.setContentText("Please place at least one source and one goal tile");
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a.show();
		}else {		//valid run whether goal reachable or not
//			System.out.println();
//			System.out.println("REACHED VALID RUN!!!");
//			System.out.printf("Source: %d   SourceX: %d   SourceY: %d\n",algo.tileGrid[sourceX][sourceY].getTileID(), sourceX, sourceY);
//			System.out.printf("Goal: %d    GoalX: %d   GoalY: %d\n",algo.tileGrid[goalX][goalY].getTileID(), goalX, goalY);
			showBestPath(algo.dijsktra(algo.tile_AdjList, algo.tileGrid[sourceX][sourceY].getTileID(), algo.tileGrid[goalX][goalY].getTileID()));
			eProb.setText("Encounter Probability: "+ algo.getProbPercentFromTile(algo.tileGrid[goalX][goalY].getTileID())+"%");
			clearBtn.setDisable(false);		//user can now click on the clear button
		} 
	}

	//Method for if user clicks on a tile in GridPane
	public void gridClick(MouseEvent e) {
		if(terrainChoices.getValue()!= null || setChoices.getValue() != null) {		//only a valid click if changing a tile's information
			Node n = (Node) e.getTarget();
			if(n instanceof ImageView) {
				
//				System.out.printf("Clicked On: R[%d]C[%d]\n", GridPane.getRowIndex(n), GridPane.getColumnIndex(n));
								
				String terrain = terrainChoices.getValue();
				String tile = setChoices.getValue();
				
				//user can only set either a tile's terrain or it's type with each click, send to proper methods based on selection
				if(terrain!= null) {
					changeTerrain(n, terrain);
				}else {
					changeTile(n, tile);
				}
//				algo.printAdjacencyList();
//				System.out.println(isCellOccupied(terrainGrid, col, row));
//				System.out.println("Grid Nodes: "+ terrainGrid.getChildren().size());
//				System.out.println();
			}
		}
	}
	
	//if item chosen from terrainChoices ComboBox, reset value of setChoices selection
	public void terrainChosen() {
		//TODO: add functionality to let user choose variety of images for chosen terrain before they click on grid
		if(terrainChoices.getValue() != null) {
			setChoices.getSelectionModel().clearSelection();
//			System.out.println("Tile RESET");
//			System.out.println(terrainChoices.getSelectionModel().getSelectedItem());
		}
	}
	
	//if item chosen from setChoices ComboBox, reset value of terrainChoices selection
	public void setChosen() {
		if(setChoices.getValue() != null) {
			terrainChoices.getSelectionModel().clearSelection();
//			System.out.println("Terrain RESET");
//			System.out.println(setChoices.getSelectionModel().getSelectedItem());
		}
	}
	
	//Method for changing a tile's terrain type (options classified in TileType enum)
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
		}else {		//valid change
			
			ImageView imageView = getImageView(TileType.valueOf(terrain.replaceAll("\\s","").toUpperCase()).getFilePath());
			
			terrainGrid.getChildren().remove(n);
			terrainGrid.add(imageView, col, row);
			
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
	
	//Method for changing a tile's cellType from 1(Regular), 2(Source), 3(Goal) to either 2 or 3
	private void changeTile(Node n, String setChoice) {
		int col = GridPane.getColumnIndex(n);
		int row = GridPane.getRowIndex(n);
		
		//walls cannot be a source or a goal
		if(algo.tileGrid[row][col].getCellType() != 0) {
			
			InnerShadow is = new InnerShadow(BlurType.ONE_PASS_BOX, null, 0, 1, 0, 0 );
			is.setHeight(25);
			is.setWidth(25);
			
//			System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
//			System.out.printf("GoalX: %d    GoalY: %d\n\n\n",goalX,goalY);
			
			//if a source is set to goal or vice versa reset old values to -1
			if(setChoice.equalsIgnoreCase("Source")){	//setting a proper source node				
				is.setColor(Color.GREEN);
				
				if(goalX == row && goalY == col) {		//if setting a goal to source, reset goal values
					goalX = -1;
					goalY = -1;
				}
				
				if(sourceX == -1 & sourceY == -1) {		//no prior source tile
					algo.tileGrid[row][col].setCellType(2);
					n.setEffect(is);	//sets new source effect to green outline
					sourceX = row;
					sourceY = col;
					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);
				}else {		//prior source exists
					algo.tileGrid[sourceX][sourceY].setCellType(1);
					algo.tileGrid[row][col].setCellType(2);
					
					Node s = getNodeFromGridPane(terrainGrid, sourceY, sourceX);	//sets previous source effect to null
					System.out.printf("OLD SOURCE: %d %d\n", GridPane.getRowIndex(s), GridPane.getColumnIndex(s));
					s.setEffect(null);
					n.setEffect(is);	//sets new source effect to green outline
					
					sourceX = row;
					sourceY = col;
					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);
				}
			}else {		//setting a proper goal node				
				is.setColor(Color.RED);
				
				if(sourceX == row && sourceY == col) {	//if setting a source to goal, reset source coordinates
					sourceX = -1;
					sourceY = -1;
				}
				if(goalX == -1 & goalY == -1) {		//no prior goal tile
					algo.tileGrid[row][col].setCellType(3);
					n.setEffect(is);	//sets new goal effect to red outline
					goalX = row;
					goalY = col;
					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);

				}else {		//prior goal exists
					algo.tileGrid[goalX][goalY].setCellType(1);
					algo.tileGrid[row][col].setCellType(3);
					
					Node g = getNodeFromGridPane(terrainGrid, goalY, goalX);
					System.out.printf("OLD GOAL: %d %d\n", GridPane.getRowIndex(g), GridPane.getColumnIndex(g));
					g.setEffect(null);	//sets previous goal effect to null
					
					n.setEffect(is);	//sets new goal effect to red outline
					
					goalX = row;
					goalY = col;
//					System.out.printf("SourceX: %d    SourceY: %d\n",sourceX,sourceY);
//					System.out.printf("GoalX: %d    GoalY: %d\n",goalX,goalY);
				}
			}
		}else {		//trying to set a wall as a source or goal
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Invalid " + setChoice +" Placement");
			a.setHeaderText("A wall cannot be set as a " + setChoice);
			a.setContentText("Choose a valid terrain tile to make a " + setChoice);
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a.show();
		}
	}
	
	//visualizes the best path after running Dijkstra, as a green path
	private void showBestPath(List<Tile> bp) {
		InnerShadow is = new InnerShadow(BlurType.ONE_PASS_BOX, Color.GREEN, 0, 1, 0, 0 );
		is.setHeight(25);
		is.setWidth(25);
		
        for(Tile t : bp) {
	        Node temp = getNodeFromGridPane(terrainGrid, t.getY(), t.getX());
	        temp.setEffect(is);
        }

		pLength.setText("Path Length: " + bp.size());
		
		//these will re-enabled once user presses Clear button
		setChoices.setDisable(true);
		terrainChoices.setDisable(true);
		searchBtn.setDisable(true);
	}
	
	//returns an imageView given a String representing the filePath
	private ImageView getImageView(String fp) {
		Image i = new Image(fp);
		ImageView imageView = new ImageView(i);
        imageView.fitHeightProperty().bind(terrainGrid.heightProperty().subtract(25).divide(10));
        imageView.fitWidthProperty().bind(terrainGrid.widthProperty().subtract(25).divide(10));
        imageView.setPreserveRatio(false);
        return imageView;
	}
	
	//resets map and data structures to initial state with all tiles as walls
	private void resetMap() {
		algo = new SearchAlgo(T);
		int id = 0;
		if(terrainGrid.getChildren().size() > 1) {
    		terrainGrid.getChildren().removeAll(terrainGrid.getChildren());
    	}
//	    System.out.println(terrainGrid.getChildren().size());
		for (int x = 0; x < dimensions; x++) {
		    for (int y = 0; y < dimensions; y++) {
		    	Tile t = new Tile(id,x,y,0,"wall");
		    	algo.tileGrid[x][y] = t;
		    	List<Tile> tile = new ArrayList<>();
		    	
				//create adjacency list for all tiles (NOTE: "wall" neighbors will be ignored during Dijkstra)
				algo.tile_AdjList.add(tile);
				//map all tiles with their IDs as keys
				algo.tilesMap.put(t.getTileID(), t); 
				
		    	ImageView imageView = getImageView(TileType.WALL.getFilePath());
		    	
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
		eProb.setText("Encounter Probability:");
		pLength.setText("Path Length:");
		clearBtn.setDisable(true);
	}
	
	//GridPane structure has no way to directly get a specific node by it's row and column, so I wrote a workaround method
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
	    return null;
	}
/*  
 * Helper method to see if a cell on the gridPane is occupied
 * private static boolean isCellOccupied(GridPane gridPane, int column, int row){
 * 		return gridPane.getChildren().stream().filter(Node::isManaged).anyMatch(n -> Objects.equals(GridPane.getRowIndex(n), row) && Objects.equals(GridPane.getColumnIndex(n), column));
 * }
 */	

}
