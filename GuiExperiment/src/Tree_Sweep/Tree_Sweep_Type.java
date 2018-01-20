package Tree_Sweep;

public enum Tree_Sweep_Type {
	MAIN,
	JOIN,
	ALL,
	NONE;
	
	public static Tree_Sweep_Type oppositeType(Tree_Sweep_Type type) {
		switch(type) {
		case JOIN: return MAIN;
		case MAIN: return JOIN;
		case ALL: return NONE;
		case NONE: return ALL;
		}
		
		return NONE;
		
	}
}
