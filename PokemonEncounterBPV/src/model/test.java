package model;

import java.util.Map;

public class test {
	
	public static void main(String[] args) {			
		
		for(TileType tt : TileType.values()) {
			System.out.println(tt.name());
		}
	}
}
