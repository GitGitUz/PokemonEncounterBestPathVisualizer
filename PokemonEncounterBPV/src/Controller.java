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
	
	private Integer sourceX;
	private Integer sourceY;
	private int goalX;
	private int goalY;
	private boolean searching;
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
		
		resetMap();
		int i = 0;
		for(List<Tile> t: algo.tile_AdjList) {
			System.out.println("Neighbors of Tile "+i+" -- CellType:  " + algo.tilesMap.get(i).getCellType()+" -- TileType:  " + algo.tilesMap.get(i).getTileType() );
			printNeighbors(t);
			i++;
		}

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
	}
	
	public void gridClick(MouseEvent e){

		if(terrainChoices.getValue()!= null || tileChoices.getValue() != null) {
			Node n = (Node) e.getTarget();
			if(n instanceof ImageView) { 
				int row = GridPane.getRowIndex(n);
				int col = GridPane.getColumnIndex(n);
				
				//logic for different combobox choices here
				
				int index = terrainGrid.getChildren().indexOf(n);
				System.out.println("\nImage at Row: "+row+" Col: "+col+" Index: "+index);
				ImageView imageView = getImageView("C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Safe\\s1.png");
				terrainGrid.getChildren().remove(n);
				terrainGrid.add(imageView, col, row);
				algo.tileGrid[row][col].setTileType("Safe");
				algo.tileGrid[row][col].setCellType(1); 
				algo.tileGrid[row][col].setTileType("SAFE"); 

				System.out.println();
				System.out.println();
				System.out.println();
				System.out.println();
				
				int i = 0;
				for(List<Tile> t: algo.tile_AdjList) {
					System.out.println("Neighbors of Tile "+i+" -- CellType:  " + algo.tilesMap.get(i).getCellType()+" -- TileType:  " + algo.tilesMap.get(i).getTileType());
					printNeighbors(t);
					i++;
				}

//				System.out.println(isCellOccupied(terrainGrid, col, row));
//				System.out.println("Grid Nodes: "+ terrainGrid.getChildren().size());
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

	private ImageView getImageView(String filePath) {
		Image i = new Image(filePath);
		ImageView imageView = new ImageView(i);
        imageView.fitHeightProperty().bind(terrainGrid.heightProperty().subtract(25).divide(10));
        imageView.fitWidthProperty().bind(terrainGrid.widthProperty().subtract(25).divide(10));
        imageView.setPreserveRatio(false);
        return imageView;
	}
	
//	public Image getImage(String filePath) {
//		return new Image(filePath);
//	}
			
	private static boolean isCellOccupied(GridPane gridPane, int column, int row){
	    return gridPane.getChildren().stream().filter(Node::isManaged).anyMatch(n -> Objects.equals(GridPane.getRowIndex(n), row) && Objects.equals(GridPane.getColumnIndex(n), column));
	}
	
//	resets map to initial state with all tiles as walls (empty white squares)
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
				
		    	ImageView imageView = getImageView("C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Empty.jpg");
 		        terrainGrid.add(imageView, y, x);
 		        id++;
		    }
		}
		
		//might need to run this again and again so that encounter rates are accurate 
		//populate adjacency lists for every Tile, this is only done once during initialization or when reset button clicked
		for(int i = 0; i < T; i++) {
			algo.tile_AdjList.get(i).addAll(getNeighbors(algo.tilesMap.get(i).getX(), algo.tilesMap.get(i).getY()));
		}
		
		sourceX = -1;
		sourceY = -1;
		goalX = -1;
		goalY = -1;
		searching = false;
	}
	
	//gets neighbors of passed Tile from the tile array based on it's passed coordinates
	private List<Tile> getNeighbors(int r, int c) {
		List<Tile> neighborsList = new ArrayList<>();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(!(i==0 && j==0)) {	//ignore when i==j==0 because that's just the same Tile
					int x = r+i;
					int y = c+j;
					if((x > -1 && x < dimensions) && (y > -1 && y < dimensions)) {
						try {
							neighborsList.add(algo.tileGrid[x][y]);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}	
				}
			}
		}
		return neighborsList;
	}
	
	private void printNeighbors(List<Tile> t) {
		for(Tile tile : t) {
			System.out.println("TileID: "+tile.getTileID()+" CellType: "+tile.getCellType()+" TileType: "+tile.getTileType());
		}
		System.out.println();
	}
}
