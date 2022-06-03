package model;
import java.util.*;

public class SearchAlgo {
	
	private int pathLength = 0;
	Random rand = new Random();
	
	//following needed for Dijkstra
	int numTiles;
	double prob[];
	int prev[];
	private Set<Integer> visitedTiles;
	PriorityQueue<Tile> tqueue;
	public Map<Integer, Tile> tilesMap;
	public List<List<Tile>> tile_AdjList;
	public boolean goalFound;
	public List<Tile> bestPath;
	
	public Tile[][] tileGrid;
	
	public SearchAlgo(int numTiles) {
		this.numTiles = numTiles;
		prob = new double[numTiles]; 
		prev = new int[numTiles];
		visitedTiles = new HashSet<>();
		tilesMap = new HashMap<>();
		tqueue = new PriorityQueue<>(numTiles, new Tile());
		tileGrid = new Tile[(int)Math.sqrt(numTiles)][(int)Math.sqrt(numTiles)];
		tile_AdjList = new ArrayList<>();
		goalFound = false;
		bestPath = new ArrayList<>();
	}
	
	
	//need to check what happens if user defines a GOAL on an unreachable tile
	public List<Tile> dijsktra(List<List<Tile>> tile_AdjList, int src_tileID, int goal_tileID){
		
		for(int i=0; i < numTiles; i++) {
			prob[i] = Integer.MAX_VALUE;
		}
		
		tqueue.add(new Tile(src_tileID, 0));
		
		prob[src_tileID] = 0;
		prev[src_tileID] = -1;
		
		//Terminate when size of visitedTiles equals number of regular (non-Wall) tiles on the board FIX THISSSSSS
		while(visitedTiles.size() != numTiles) {
			
			System.out.println();
			System.out.println("visited Size: " + visitedTiles.size());
			System.out.println("Visited: " + visitedTiles);

			if(tqueue.peek() == null) {
				System.out.println("PQueue: [NULL]");
			}else {
				System.out.println("PQueue: [" + tqueue.peek().getTileID()+"]");
			}
			
			System.out.println();
			
			if(tqueue.isEmpty()) {
				
				if(visitedTiles.contains(goal_tileID)) {
					goalFound = true;
				}
				
				for (Map.Entry<Integer, Tile> tile : tilesMap.entrySet()) {		//terminate search if all reachable nodes found with unreachable nodes remaining on board
					if(prob[tile.getKey()] == Integer.MAX_VALUE && visitedTiles.contains(goal_tileID)) {
						goalFound = true;
						break;
					}
				}
				
				if(goalFound) {
					break;
				}else {
					System.out.println();
					System.out.println("GOAL UNREACHABLE");
					System.out.println();
					return null;
				}
			}
			int t = tqueue.remove().getTileID();
			
			if(visitedTiles.contains(t) || tilesMap.get(t).getCellType() == 0){		//ignore walls
				continue;
			}
			
			visitedTiles.add(t);
			neighborTiles(t);
		}
//		this.printDijkstra(tilesMap, prev, prob, src_tileID);
		printBestPath(tilesMap, prev, prob, src_tileID, goal_tileID);
		createBestPath(tilesMap, prev, goal_tileID);
		return bestPath;
	}
	
	private void neighborTiles(int t) {
		double edgeProb = -1;
		double newProb = -1;
		
		for(int i = 0; i < tile_AdjList.get(t).size(); i++) {
//			System.out.printf("Num Neighbors for %d: %d\n", t,  tile_AdjList.get(t).size());
			Tile v = tile_AdjList.get(t).get(i);
//			System.out.printf("Neighbor of %d: %d\n",t,v.getTileID());
			
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
				tqueue.add(new Tile(v.getTileID(), prob[v.getTileID()]));
			}
		}
	}
	
	//prints shortest path from source node to every non-wall node on grid
	public void printBestPath(Map<Integer, Tile> tilesMap, int[] prev, double[] prob, int source, int goal) {
		System.out.print("["+tilesMap.get(source).getX() + "]["+ tilesMap.get(source).getY() + "] " +"("+ tilesMap.get(source) + ") -->" + 
						 "["+tilesMap.get(goal).getX() + "]["+ tilesMap.get(goal).getY() + "] " + " ("+ tilesMap.get(goal) + ") : "
							+ "Encounter Chance: Prob ["+ prob[goal]+"] Percent = " + probToPercent(prob[goal]) + "% Best Path: ");
		printPath(tilesMap, prev, goal);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	public void createBestPath(Map<Integer, Tile> tilesMap, int[] prev, int dest) {
		if(prev[dest] == -1) {
//			System.out.println(dest);
			bestPath.add(tilesMap.get(dest));
			return;
		}
		createBestPath(tilesMap, prev, prev[dest]);
		bestPath.add(tilesMap.get(prev[dest]));
		return;
	}
	
	//helper method for main printing method
	public void printPath(Map<Integer, Tile> tilesMap, int[] prev, int dest) {
		if(prev[dest] == -1) {
			System.out.print(dest + "(" + tilesMap.get(dest) + ") ");
			return;
		}
		printPath(tilesMap, prev, prev[dest]);
		System.out.print(dest + "(" + tilesMap.get(dest) + ") ");
		return;
	}
//	
	//prints shortest path from source node to every non-wall node on grid
//	public void printDijkstra(Map<Integer, Tile> tilesMap, int[] prev, double[] prob, int source) {
//		System.out.println("Dijkstra with Paths");
//		for(int i = 0; i < numTiles; i++) {
//			if(!visitedTiles.contains(tilesMap.get(i).getTileID())) {		//ignore walls
//				continue;
//			} 
//			System.out.print("["+tilesMap.get(source).getX() + "]["+ tilesMap.get(source).getY() + "] " +"("+ tilesMap.get(source) + ") -->" + 
//							 "["+tilesMap.get(i).getX() + "]["+ tilesMap.get(i).getY() + "] " + " ("+ tilesMap.get(i) + ") : "
//							 		+ "Encounter Chance: Prob ["+ prob[i]+"] Percent = " + probToPercent(prob[i]) + "% Best Path: ");
//			printPath(tilesMap, prev, i);
//			System.out.println();
//		}
//	}
	
	//reverses the log of encounter chance with exponential to give readable decimal percent
	public double probToPercent(double prob) {
		double percent = 0;
		if(prob == 0) {
			return percent;
		}else {
			percent = ((Math.pow(2.00, -prob)-1) * -100);
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
	
	public double getProbPercentFromTile(int tileID) {
		return probToPercent(prob[tileID]);
	}
	
	public void resetDijkstra() {
		this.visitedTiles.clear();
		this.tqueue.clear();
		this.goalFound = false;
		this.bestPath.clear();
	}
		
}
