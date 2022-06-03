package model;
import java.util.*;


public class Tile implements Comparator <Tile> {
	
	final static double encounterConst = 15.0;
	
	private int cellType;		//0=wall 1=regular 2= source 3=goal
	private TileType type; 
	private int x;
	private int y;
	private int tile_ID;
	private double encounterRate;
	
	public Tile() {}
	
	//every Tile has knowledge of its x,y coordinate on the GridPane, these never change, only the cellType and TileType changes, which together determines the image shown to the user
	public Tile(int tile_ID, int x, int y, int cellType, String type) {
		this.x = x;
		this.y = y;
		this.tile_ID = tile_ID;
		this.setCellType(cellType);
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
	
	public int getCellType() {
		return this.cellType;
	}
	
	public void setCellType(int cellType) {
		this.cellType = cellType;
	}
	
	public String getTileType() {
		if(this.type != null) {
			return this.type.toString();
		}else {
			return "NULL";
		}
	}
	
	public void setTileType(String type) {
		if (type.equalsIgnoreCase("wall")) {
			this.type = null;
			setCellType(0);
		}else {
			try {
				this.type = TileType.valueOf(type.toUpperCase());
				setCellType(1);
			} catch (IllegalArgumentException e) {
//				System.out.printf("'%s' is not a valid tile type\n", type);
				System.exit(1);
			} 
		}
		setEncounterRate();
	}
	
	public double getEncounterRate() {
		return this.encounterRate;
	}
	
	public void setEncounterRate() {
		if(this.type == null) {
			this.encounterRate = -1;
		}else {
			int baseRate = this.type.getBaseEncounterRate();
			if(baseRate == 0) { 	//for SAFE tiles because you can't take the log of 0 so rate will simply be 0;
				this.encounterRate = 0;
			}else {
				double prob = (baseRate * encounterConst)/1500.00;
				this.encounterRate = -log2(1-prob);
			}
		}
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public static double log2(double N){	//created a log function base 2
        double result = (double)(Math.log(N) / Math.log(2));
        return result;
    }
	
	@Override
	public int compare(Tile t1, Tile t2) {	//Tiles are compared based on their encounter rate, used mainly in priority queue during Dijkstra

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
}
