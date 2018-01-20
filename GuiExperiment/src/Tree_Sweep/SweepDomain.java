package Tree_Sweep;

import java.awt.Color;
import java.util.Random;
import Space.VertexSegment;

public class SweepDomain {
	
	public Color color;
	public int value;
	
	public VertexSegment leftSegment;
	public VertexSegment rightSegment;
	
	public SweepDomain() {
		Random r = new Random();
		this.value = r.nextInt(999);
		color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 100);
	}
}
