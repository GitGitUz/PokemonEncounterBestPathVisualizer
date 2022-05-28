import java.util.*;
import java.text.*;

public class Board {
	
	private static final DecimalFormat df = new DecimalFormat("#.#");

	double prob[];
	int prev[];
	Set<Integer> visitedTiles;
	Map<Integer, Tile> tilesList;
	List<List<Tile>> tile_AdjList;
	PriorityQueue<Tile> tqueue;
	int T; // number of tiles
	
	public Board(int T) {
		this.T = T;
		prob = new double[T];
		prev = new int[T];
		visitedTiles = new HashSet<>();
		tilesList = new HashMap<>();
		tqueue = new PriorityQueue<>(T, new Tile());
	}
	
	public void dijsktra(List<List<Tile>> tile_AdjList, int src_tileID){
		this.tile_AdjList = tile_AdjList;
		for(int i=0; i < T; i++) {
			prob[i] = Integer.MAX_VALUE;
		}
		
		tqueue.add(new Tile(src_tileID, 0));
		
		prob[src_tileID] = 0;
		prev[src_tileID] = -1;
		
		while(visitedTiles.size() != T) {
			
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
	
	public void printDijkstra(Map<Integer, Tile> tilesList, int[] prev, double[] prob, int source) {
		System.out.println("Dijkstra with Paths");
		for(int i = 0; i < T; i++) {
			System.out.print(source +"("+ tilesList.get(source) + ") -->" + i + " ("+ tilesList.get(i) + ") : Encounter Chance = " + probToPercent(prob[i]) + "% Best Path: ");
			printPath(tilesList, prev, i);
			System.out.println();
		}
	}
	
	public void printPath(Map<Integer, Tile> tilesList, int[] prev, int dest) {
		if(prev[dest] == -1) {
			System.out.print(dest + "(" + tilesList.get(dest) + ") " );
			return;
		}
		printPath(tilesList, prev, prev[dest]);
		System.out.print(dest + "(" + tilesList.get(dest) + ") " );
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
	
	public static void main(String[] args) {
		
		int T = 9;
		Board encounterBoard = new Board(T);
		
		List<List<Tile>> tile_AdjList = new ArrayList<>();
		
		for(int i = 0; i < T; i++) {
			List<Tile> tile = new ArrayList<Tile>();
			tile_AdjList.add(tile);
			// generate random terrain out of set of available tile types
			encounterBoard.tilesList.put(i, new Tile(i, getRandomTile())); 
		}
	
		//this will change to retrieving from map instead of new Tile()
		tile_AdjList.get(0).add(new Tile(1, "DESERTSAND"));
		tile_AdjList.get(0).add(new Tile(3, "SHORTGRASS"));
		
		tile_AdjList.get(1).add(new Tile(0, "SAFE"));
		tile_AdjList.get(1).add(new Tile(2, "TALLGRASS"));
		tile_AdjList.get(1).add(new Tile(4, "SAFE"));
		
		tile_AdjList.get(2).add(new Tile(1, "DESERTSAND"));
		tile_AdjList.get(2).add(new Tile(5, "ROUTE"));
		
		tile_AdjList.get(3).add(new Tile(0, "SAFE"));
		tile_AdjList.get(3).add(new Tile(4, "SAFE"));
		tile_AdjList.get(3).add(new Tile(6, "ROUTE"));
		
		tile_AdjList.get(4).add(new Tile(1, "DESERTSAND"));
		tile_AdjList.get(4).add(new Tile(3, "SHORTGRASS"));
		tile_AdjList.get(4).add(new Tile(5, "ROUTE"));
		tile_AdjList.get(4).add(new Tile(7, "DESERTSAND"));
		
		tile_AdjList.get(5).add(new Tile(2, "TALLGRASS"));
		tile_AdjList.get(5).add(new Tile(4, "SAFE"));
		tile_AdjList.get(5).add(new Tile(8, "SHORTGRASS"));
		
		tile_AdjList.get(6).add(new Tile(3, "SHORTGRASS"));
		tile_AdjList.get(6).add(new Tile(7, "DESERTSAND"));
		
		tile_AdjList.get(7).add(new Tile(4, "SAFE"));
		tile_AdjList.get(7).add(new Tile(6, "ROUTE"));
		tile_AdjList.get(7).add(new Tile(8, "SHORTGRASS"));
		
		tile_AdjList.get(8).add(new Tile(5, "ROUTE"));
		tile_AdjList.get(8).add(new Tile(7, "DESERTSAND"));
		
		Tile source = new Tile(6, "ROUTE");
//		Tile source = encounterBoard.tilesList.get(new Random().nextInt(T-1);
		
		encounterBoard.dijsktra(tile_AdjList, source.getTileID());
		encounterBoard.printDijkstra(encounterBoard.tilesList,encounterBoard.prev, encounterBoard.prob, source.getTileID());
		
//		System.out.println(encounterBoard.tilesList);

	}
}
