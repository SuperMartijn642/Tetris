package SuperMartijn642.Tetris.Game;

import java.awt.geom.Point2D;

public class Position2D {
	
	public double x;
	public double y;
	
	public Position2D(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public Position2D(Point2D point){
		this.x = point.getX();
		this.y = point.getY();
	}
	
	public Position2D(String string){
		try {
			Position2D pos = new Position2D(Double.parseDouble(string.substring(2, string.indexOf("y:"))), Double.parseDouble(string.substring(string.indexOf("y:") + 2, string.indexOf(";"))));
			this.x = pos.x;
			this.y = pos.y;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Position2D fromPoint(Point2D point){
		return new Position2D(point);
	}
	
	public static double getDistance(Position2D pos1, Position2D pos2){
		return Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));
	}
	
	public double getDistanceTo(Position2D pos){
		return Math.sqrt(Math.pow(this.x - pos.x, 2) + Math.pow(this.y - pos.y, 2));
	}
	
	public Position2D clone(){
		return new Position2D(this.x, this.y);
	}
	
	public boolean equals(Position2D pos){
		return this.x == pos.x && this.y == pos.y;
	}
	
	public String toString(){
		return "x:" + this.x + "y:" + this.y + ";";
	}
	
	public static Position2D fromString(String string){
		Position2D pos = new Position2D(string);
		return pos == null ? null : pos;
	}
	
}
