public enum TileType {
	
	SAFE(0), 		//0.00
	CAVE(10), 		//0.06	-log(0.06) = 1.22184874962 	-log(0.94) = 0.0268721464
	DESERTSAND(15), //0.09 	-log(0.09) = 1.04575749056 	-log(0.91) = 0.04095860767
	ROUTE(20), 		//0.12 	-log(0.12) = 0.92081875395 	-log(0.88) = 0.05551732784
	SHORTGRASS(25),	//0.15 	-log(0.15) = 0.82390874094 	-log(0.85) = 0.07058107428
	TALLGRASS(30); 	//0.18 	-log(0.18) = 0.74472749489 	-log(0.82) = 0.08618614761

	
	final int baseEncounterRate;
	
	TileType(int baseEncounterRate){
		this.baseEncounterRate = baseEncounterRate;
	}
	
	int getBaseEncounterRate() {
		return this.baseEncounterRate;
	}
}
