import java.util.*;

public class SearchAlgo {
	
	//hash function for tile coordinates (103*row + 97*col)
	private final int hashRow = 103;
	private final int hashCol = 97;
	
	private int pathLength = 0;
	Random rand = new Random();
	
	//following needed for Dijkstra
	int numTiles;
	int wallTiles;
	double prob[];
	int prev[];
	Set<Integer> visitedTiles;
	Map<Integer, Tile> tilesMap;
	PriorityQueue<Tile> tqueue;
	List<List<Tile>> tile_AdjList;
		
	//needed to match with GridPane nodes--no searching used on this array, only access and update
	Tile[][] tileGrid;
	
	public SearchAlgo(int numTiles) {
		this.numTiles = numTiles;
		wallTiles = numTiles;
		prob = new double[numTiles]; 
		prev = new int[numTiles];
		visitedTiles = new HashSet<>();
		tilesMap = new HashMap<>();
		tqueue = new PriorityQueue<>(numTiles, new Tile());
		tileGrid = new Tile[(int)Math.sqrt(numTiles)][(int)Math.sqrt(numTiles)];
		tile_AdjList = new ArrayList<>();
	}
	
	public void dijsktra(List<List<Tile>> tile_AdjList, int src_tileID){
		for(int i=0; i < numTiles; i++) {
			prob[i] = Integer.MAX_VALUE;
		}
		
		tqueue.add(new Tile(src_tileID, 0));
		
		prob[src_tileID] = 0;
		prev[src_tileID] = -1;
		
		//Terminate when size of visitedTiles equals number of regular (non-Wall) tiles on the board
		while(visitedTiles.size() != numTiles-wallTiles) {
			
			if(tqueue.isEmpty()) {
				return;
			}
			
			int t = tqueue.remove().getTileID();
			
			if(visitedTiles.contains(t)){
				continue;
			}
			
			visitedTiles.add(t);
			neighborTiles(t);
		}
	}
	
	private void neighborTiles(int t) {
		double edgeProb = -1;
		double newProb = -1;
		
		for(int i = 0; i < tile_AdjList.get(t).size(); i++) {
			Tile v = tile_AdjList.get(t).get(i); 
			
			if(v.getCellType()==0) {	//ignores walls
				continue;
			}
			
			if(!visitedTiles.contains(v.getTileID())) {
				edgeProb = v.getEncounterRate();
				newProb = prob[t] + edgeProb;
				
				if(newProb < prob[v.getTileID()]) {
					prob[v.getTileID()] = newProb;
					prev[v.getTileID()] = t;
				}
				
				tqueue.add(new Tile(v.getTileID(), prob[v.getTileID()]));
			}
		}
	}
	
	public void printDijkstra(Map<Integer, Tile> tilesMap, int[] prev, double[] prob, int source) {
		System.out.println("Dijkstra with Paths");
		for(int i = 0; i < numTiles; i++) {
			if(tilesMap.get(i).getCellType()==0) {		//ignore Wall tiles
				continue;
			}
			System.out.print(source +"("+ tilesMap.get(source) + ") -->" + i + " ("+ tilesMap.get(i) + ") : Encounter Chance = " + probToPercent(prob[i]) + "% Best Path: ");
			printPath(tilesMap, prev, i);
			System.out.println();
		}
	}
	
	public void printPath(Map<Integer, Tile> tilesMap, int[] prev, int dest) {
		if(prev[dest] == -1) {
			System.out.print(dest + "(" + tilesMap.get(dest) + ") " );
			return;
		}
		printPath(tilesMap, prev, prev[dest]);
		System.out.print(dest + "(" + tilesMap.get(dest) + ") " );
	}
	   
	public double probToPercent(double prob) {
		double percent = 0;
		if(prob == 0) {
			return percent;
		}else {
			percent = ((Math.pow(10.00, -prob)-1) * -100);
		}
		return Math.round(percent*100.0)/100.0;
	}
	
	public static String getRandomTile() {
		return TileType.values()[new Random().nextInt(TileType.values().length)].toString();
	}
	
	private Tile getTileByCoordinate(int r, int c){
	    return tileGrid[r][c];
	}
	
	private int getTileIDByCoordinate(int r, int c){
	    return tileGrid[r][c].getTileID();
	}
//	public void reset() {
//		for(int i = 0; i< tileGrid.length; i++) {
//			tileGrid[i] = null;
//		}
//	}
//	
//	public int getSourceTileFromLocation(int r, int c) {
//		return tileGrid[r][c].getTileID();
//	}
	
}
