package ev3.parser;

public class Line {
	int x0;
    int y0;
    int x1;
    int y1;
    
    String ev3LineFormat;

    public Line(String x0, String y0, String x1, String y1) {
        this.x0 = (int) Math.round(Double.parseDouble(x0));
        this.y0 = (int) Math.round(Double.parseDouble(y0));
        this.x1 = (int) Math.round(Double.parseDouble(x1));
        this.y1 = (int) Math.round(Double.parseDouble(y1));
        
        this.ev3LineFormat = this.x0 + " " + this.y0 + " " + this.x1 + " " + this.y1;
    }
    
    public String getEv3LineFormat(){
    	return ev3LineFormat;
    }

	public int getX0() {
		return x0;
	}

	public int getY0() {
		return y0;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}
    
    
}
