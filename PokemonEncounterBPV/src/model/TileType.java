package model;
public enum TileType {
	
	WALL(-1, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\wall.jpg"),
	SAFE(0, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Safe\\s1.png"), 				//0.00
	CAVE(10, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Cave\\c1.png"), 				//0.133333	log(0.133333) = -2.907
	DEEPSAND(15, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Deep Sand\\d2.png"),  	//0.20 		log(0.20) 	  = -2.322
	ROUTE(20, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Route\\r1.png"), 			//0.266667 	log(0.266667) = -1.907
	SHORTGRASS(25, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Short Grass\\sg2.png"),	//0.333333 	log(0.333333) = -1.585            
	TALLGRASS(30, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Tall Grass\\tg2.png"); 	//0.40 		log(0.40) 	  = -1.322
	
	final int baseEncounterRate;
	final String filePath;
	
	TileType(int baseEncounterRate, String filepath){
		this.baseEncounterRate = baseEncounterRate;
		this.filePath = filepath;
	}
	
	int getBaseEncounterRate() {
		return this.baseEncounterRate;
	}
	
	public String getFilePath() {
		return this.filePath;
	}
}
