package model;
public enum TileType {
	
	WALL(-1, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\wall.jpg"),
	SAFE(0, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Safe\\s1.png"), 				//0.00
	CAVE(10, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Cave\\c1.png"), 				//0.06	-log(0.06) = 1.22184874962 	-log(0.94) = 0.0268721464
	DESERTSAND(15, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Desert Sand\\d2.png"),  //0.09 	-log(0.09) = 1.04575749056 	-log(0.91) = 0.04095860767
	ROUTE(20, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Route\\r1.png"), 			//0.12 	-log(0.12) = 0.92081875395 	-log(0.88) = 0.05551732784
	SHORTGRASS(25, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Short Grass\\sg2.png"),	//0.15 	-log(0.15) = 0.82390874094 	-log(0.85) = 0.07058107428
	TALLGRASS(30, "C:\\Users\\uzair\\git\\PokemonEncounterBestPathVisualizer\\PokemonEncounterBPV\\Images\\Tall Grass\\tg2.png"); 	//0.18 	-log(0.18) = 0.74472749489 	-log(0.82) = 0.08618614761
	
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
