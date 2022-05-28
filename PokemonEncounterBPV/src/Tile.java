import java.util.*;


public class Tile implements Comparator <Tile> {
	
	final static double encounterConst = 15.0;
	
	private int cellType;
	private int tile_ID;
	private double encounterRate;
	private TileType type; 
	
	public Tile() {}
	
	public Tile(int tile_ID, String type) {
		this.tile_ID = tile_ID;
		this.setTileType(type);
		this.setEncounterRate();
	}
	
	public Tile(int tile_ID, double prob) {
		this.tile_ID = tile_ID;
		this.encounterRate = prob;
	}
	
	public int getTileID() {
		return this.tile_ID;
	}
	
	public double getEncounterRate() {
		return this.encounterRate;
	}
	
	public void setEncounterRate() {
		int baseRate = this.type.getBaseEncounterRate();
		if(baseRate == 0) { // for SAFE tiles because you can't take the log of 0 so rate will simply be 0;
			this.encounterRate = 0;
		}else {
			double prob = (baseRate * encounterConst)/2500.00;
			this.encounterRate = -Math.log10(1-prob); // we are taking 1-prob because we want the shortest path to give us the MINIMUM probability of encounter
		}
	}
	
	public String getTileType() {
		return this.type.toString();
	}
	
	public void setTileType(String type) {
		try{
			this.type = TileType.valueOf(type.toUpperCase());
		}catch(IllegalArgumentException e) {
			System.out.printf("'%s' is not a valid tile type\n", type);
			System.exit(1);
		}
	}
	
	@Override
	public int compare(Tile t1, Tile t2) {

		if(t1.encounterRate < t2.encounterRate) {
			return -1;
		}
		if(t1.encounterRate > t2.encounterRate) {
			return 1;	
		}
		return 0;
	}
	
	public String toString() {
		return this.getTileType();
	}
	public static void main(String[] args) {
	
	}
}
