package model;
import java.util.*;

public class SearchAlgo {
	
	private int pathLength = 0;
	Random rand = new Random();
	
	//following needed for Dijkstra
	int numTiles;
	int wallTiles;
	double prob[];
	int prev[];
	private Set<Integer> visitedTiles;
	PriorityQueue<Tile> tqueue;
	public Map<Integer, Tile> tilesMap;
	public List<List<Tile>> tile_AdjList;
	
	public Tile[][] tileGrid;
	
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
	
	
	//need to check what happens if user defines a GOAL on an unreachable tile
	public void dijsktra(List<List<Tile>> tile_AdjList, int src_tileID){
		
		for(int i=0; i < numTiles; i++) {
			prob[i] = Integer.MAX_VALUE;
		}
		
		tqueue.add(new Tile(src_tileID, 0));
		
		prob[src_tileID] = 0;
		prev[src_tileID] = -1;
		
		//Terminate when size of visitedTiles equals number of regular (non-Wall) tiles on the board FIX THISSSSSS
		while(visitedTiles.size() != numTiles-wallTiles) {
			System.out.println();
			System.out.println("visited Size: " + visitedTiles.size());
			System.out.println("Visited: " + visitedTiles);
			System.out.println("PQueue: " + tqueue.peek().getTileID());
			System.out.println();
			
			if(tqueue.isEmpty()) {
				System.out.println();
				System.out.println();
				System.out.println("PRIORITY QUEUE IS EMPTY, ");
				System.out.println("GOAL UNREACHABLE");
				System.out.println();
				return;
			}
			
			int t = tqueue.remove().getTileID();
			
			if(visitedTiles.contains(t) || tilesMap.get(t).getCellType() == 0){		//ignore walls
				System.out.println(".............continuing inside while loop.........");
				continue;
			}
			
			visitedTiles.add(t);
			neighborTiles(t);
		}
		this.printDijkstra(tilesMap, prev, prob, src_tileID);
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
				
				//ADD SOME LOGIC HERE TO MORE RANDOMLY GENERATE ENCOUNTERS (40% SKIP CHANCE THEN RANDOM NUM B/W 0-1500)
				edgeProb = v.getEncounterRate();
				newProb = prob[t] + edgeProb;
				
				if(newProb < prob[v.getTileID()]) {
					System.out.println();
					System.out.printf("PREVIOUS PROB for %d: %f\n",v.getTileID(), prob[v.getTileID()]);
					prob[v.getTileID()] = newProb;
					System.out.printf("AFTER PROB for %d: %f\n",v.getTileID(), prob[v.getTileID()]);
					System.out.println();
					prev[v.getTileID()] = t;
				}
				if(!(v.getTileID() == 0)) {
					tqueue.add(new Tile(v.getTileID(), prob[v.getTileID()]));
				}
			}
		}
	}
	
	//prints shortest path from source node to every non-wall node on grid
	public void printDijkstra(Map<Integer, Tile> tilesMap, int[] prev, double[] prob, int source) {
		System.out.println("Dijkstra with Paths");
		for(int i = 0; i < numTiles; i++) {
//			System.out.printf("RECUR: %d CellType: %d\n", tilesMap.get(i).getTileID(), tilesMap.get(i).getCellType());
			if(tilesMap.get(i).getCellType()==0) {		//ignore walls
				continue;
			} 
			System.out.print("["+tilesMap.get(source).getX() + "]["+ tilesMap.get(source).getY() + "] " +"("+ tilesMap.get(source) + ") -->" + 
							 "["+tilesMap.get(i).getX() + "]["+ tilesMap.get(i).getY() + "] " + " ("+ tilesMap.get(i) + ") : "
							 		+ "Encounter Chance: Prob ["+ prob[i]+"] Percent = " + probToPercent(prob[i]) + "% Best Path: ");
			printPath(tilesMap, prev, i);
			System.out.println();
		}
	}
	//helper method for main printing method
	public void printPath(Map<Integer, Tile> tilesMap, int[] prev, int dest) {
		if(prev[dest] == -1) {
			System.out.print(dest + "(" + tilesMap.get(dest) + ") ");
			return;
		}
		if(!(tilesMap.get(dest).getCellType()==0)) {
			printPath(tilesMap, prev, prev[dest]);
			System.out.print(dest + "(" + tilesMap.get(dest) + ") ");
		}else {
			return;
		}
	}
	
	//reverses the log of encounter chance with exponential to give readable decimal percent
	public double probToPercent(double prob) {
		double percent = 0;
		if(prob == 0) {
			return percent;
		}else {
			percent = ((Math.pow(2.00, -prob)-1) * -100);
//			percent = ((Math.pow(2.00, prob)) * 100);

		}
		return Math.round(percent*100.0)/100.0;
	}
	
	//get a random tile type from the enum
	public static String getRandomTile() {
		return TileType.values()[new Random().nextInt(TileType.values().length)].toString();
	}
	
	//populates adjacency list for all tiles
	public void populateAdjacencyList() {
		for(int i = 0; i < numTiles; i++) {
			tile_AdjList.get(i).addAll(getNeighbors(tilesMap.get(i).getX(), tilesMap.get(i).getY()));
		}
	}
	
	//gets list of neighbors of a tile in the tile array
	public List<Tile> getNeighbors(int r, int c) {
		List<Tile> neighborsList = new ArrayList<>();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(!(i==0 && j==0)) {	//ignore when i==j==0 because that's just the same Tile
					int x = r+i;
					int y = c+j;
					if((x > -1 && x < (int)Math.sqrt(numTiles)) && (y > -1 && y < (int)Math.sqrt(numTiles))) {
						try {
							neighborsList.add(tileGrid[x][y]);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}	
				}
			}
		}
		return neighborsList;
	}
	
	//prints entire adjacency list
	public void printAdjacencyList() {
		int id = 0;
		for (List<Tile> tl : tile_AdjList) {
			System.out.println("Neighbors of Tile "+id+" -- CellType:  " + tilesMap.get(id).getCellType()+" -- TileType:  " + tilesMap.get(id).getTileType() );
			for(Tile tile : tl) {
				System.out.println("TileID: "+tile.getTileID()+" CellType: "+tile.getCellType()+" TileType: "+tile.getTileType());
			}
			System.out.println();
			id++;
		}
	}
	
	public int getWallTiles() {
		return this.wallTiles;
	}
	
	public void inceaseWallTiles() {
		this.wallTiles++;
		tqueue = new PriorityQueue<>(numTiles-wallTiles, new Tile());
	}
	
	public void decreaseWallTiles() {
		this.wallTiles--;
		tqueue = new PriorityQueue<>(numTiles-wallTiles, new Tile());
	}
	
	public void resetDijkstra() {
		this.visitedTiles.clear();
		tqueue.clear();
	}
		
}
