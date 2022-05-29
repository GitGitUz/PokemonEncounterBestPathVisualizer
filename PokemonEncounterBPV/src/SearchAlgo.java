import java.util.*;

public class SearchAlgo {
	
	//hash function for tile coordinates (103*row + 97*col)
	private final int hashRow = 103;
	private final int hashCol = 97;
	
	private int pathLength = 0;
	Random rand = new Random();
	
	int numTiles; // number of tiles
	double prob[];
	int prev[];
	Set<Integer> visitedTiles;
	Map<Integer, Tile> tilesMap;
	Map<Integer, Tile> coordinateMap;
	PriorityQueue<Tile> tqueue;
	Tile[][] tileGrid;
	List<List<Tile>> tile_AdjList;
	
	public SearchAlgo(int numTiles) {
		this.numTiles = numTiles;
		prob = new double[numTiles]; 
		prev = new int[numTiles];
		visitedTiles = new HashSet<>();
		tilesMap = new HashMap<>();
		coordinateMap = new HashMap<>();
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
		
		while(visitedTiles.size() != numTiles) {
			
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
	
	public void reset() {
		for(int i = 0; i< tileGrid.length; i++) {
			tileGrid[i] = null;
		}
	}
	
	public int getSourceTileFromLocation(int r, int c) {
		return tileGrid[r][c].getTileID();
	}
	
	public static void main(String[] args) {
		
		//int T = 9;
		//SearchAlgo encounterBoard = new SearchAlgo(T);
		
//		List<List<Tile>> tile_AdjList = new ArrayList<>();
//		
//		for(int i = 0; i < T; i++) {
//			List<Tile> tile = new ArrayList<Tile>();
//			tile_AdjList.add(tile);
//			// generate random terrain out of set of available tile types
//			encounterBoard.tilesMap.put(i, new Tile(i, getRandomTile())); 
//		}
//	
//		//this will change to retrieving from map instead of new Tile()
//		tile_AdjList.get(0).add(new Tile(1,  "DESERTSAND"));
//		tile_AdjList.get(0).add(new Tile(3, "SHORTGRASS"));
//		
//		tile_AdjList.get(1).add(new Tile(0, "SAFE"));
//		tile_AdjList.get(1).add(new Tile(2, "TALLGRASS"));
//		tile_AdjList.get(1).add(new Tile(4, "SAFE"));
//		
//		tile_AdjList.get(2).add(new Tile(1, "DESERTSAND"));
//		tile_AdjList.get(2).add(new Tile(5, "ROUTE"));
//		
//		tile_AdjList.get(3).add(new Tile(0, "SAFE"));
//		tile_AdjList.get(3).add(new Tile(4, "SAFE"));
//		tile_AdjList.get(3).add(new Tile(6, "ROUTE"));
//		
//		tile_AdjList.get(4).add(new Tile(1, "DESERTSAND"));
//		tile_AdjList.get(4).add(new Tile(3, "SHORTGRASS"));
//		tile_AdjList.get(4).add(new Tile(5, "ROUTE"));
//		tile_AdjList.get(4).add(new Tile(7, "DESERTSAND"));
//		
//		tile_AdjList.get(5).add(new Tile(2, "TALLGRASS"));
//		tile_AdjList.get(5).add(new Tile(4, "SAFE"));
//		tile_AdjList.get(5).add(new Tile(8, "SHORTGRASS"));
//		
//		tile_AdjList.get(6).add(new Tile(3, "SHORTGRASS"));
//		tile_AdjList.get(6).add(new Tile(7, "DESERTSAND"));
//		
//		tile_AdjList.get(7).add(new Tile(4, "SAFE"));
//		tile_AdjList.get(7).add(new Tile(6, "ROUTE"));
//		tile_AdjList.get(7).add(new Tile(8, "SHORTGRASS"));
//		
//		tile_AdjList.get(8).add(new Tile(5, "ROUTE"));
//		tile_AdjList.get(8).add(new Tile(7, "DESERTSAND"));
//		
//		Tile source = new Tile(6, "ROUTE");
////		Tile source = encounterBoard.tilesMap.get(new Random().nextInt(T-1);
//		
//		encounterBoard.dijsktra(tile_AdjList, source.getTileID());
//		encounterBoard.printDijkstra(encounterBoard.tilesMap,encounterBoard.prev, encounterBoard.prob, source.getTileID());
		
//		System.out.println(encounterBoard.tilesMap);

	}
}
