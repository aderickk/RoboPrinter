import java.util.ArrayList;

public class PrintData {
	
	private static final int NUMBER_OF_ARM_STEPS = 70;
	private static final int NUMBER_OF_WHEEL_STEPS = 100;
	
	private int sourceCanvasWidth;
	private int sourceCanvasHeight;
	
	private ArrayList<Line> lines = new ArrayList<>();

	public void addLine(String lineCoords) {
		lines.add(new Line(lineCoords));
	}
	
	public ArrayList<Line> getLines() {
		return lines;
	}
	
	public void setSourceDimensions(String line) {
		String[] dimensions = line.split(" ");
		sourceCanvasWidth = Integer.parseInt(dimensions[0]);
		sourceCanvasHeight = Integer.parseInt(dimensions[1]);
	}

	public class Line{
		private float startX;
		private float startY;
		private float targetX;
		private float targetY;

	    public Line(String lineCoords) {
	    	String[] coords = lineCoords.split(" ");
	    	
	    	int startXPixels = Integer.parseInt(coords[0]);
	    	int startYPixels = Integer.parseInt(coords[1]);
	    	int targetXPixels = Integer.parseInt(coords[2]);
	    	int targetYPixels = Integer.parseInt(coords[3]);
	    	
	    	startX = adjustToPaperWidth(startXPixels);
	    	startY = adjustToPaperHeight(startYPixels);
			targetX = adjustToPaperWidth(targetXPixels);
			targetY = adjustToPaperHeight(targetYPixels);
	    	
	    }
	    
	    private float adjustToPaperWidth(int xCoordPixels) {
			int adjustedCoordinate = xCoordPixels * NUMBER_OF_ARM_STEPS / sourceCanvasWidth;
			return adjustedCoordinate;
		}
	    
		private float adjustToPaperHeight(int yCoordPixels) {
			int adjustedCoordinate = yCoordPixels * NUMBER_OF_WHEEL_STEPS / sourceCanvasHeight;
			return adjustedCoordinate;
		}

		public float getStartX() {
			return startX;
		}

		public float getStartY() {
			return startY;
		}

		public float getTargetX() {
			return targetX;
		}

		public float getTargetY() {
			return targetY;
		}
	    
	    
	}
	
}
