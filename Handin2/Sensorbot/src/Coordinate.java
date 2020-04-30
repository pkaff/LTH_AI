
public class Coordinate {
	public int x;
	public int y;
	public static final Coordinate NOTHING = new Coordinate(-1, -1);
	
	public Coordinate(int x, int y) { 
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Coordinate)) {
			return false;
		} else {
			return x == ((Coordinate)o).x && y == ((Coordinate)o).y;
		}	
	}
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
		if (outOfBounds()) {
			x = -1;
			y = -1;
		}
	}
	
	private boolean outOfBounds() {
		return x < 0 || y < 0 || x > Bot.UPPER_X_BOUND || y > Bot.UPPER_Y_BOUND;
	}
	
	public void add(Coordinate c) {
		int newX = x + c.x;
		int newY = y + c.y;
		//If inside bounds
		if (newX >= 0 && newX <= Bot.UPPER_X_BOUND) {
			x = newX;
		}
		if (newY >= 0 && newY <= Bot.UPPER_Y_BOUND) {
			y = newY;
		}
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public double probability(Coordinate c) {
		if (equals(NOTHING)) {
			int x = c.x;
			int y = c.y;
			if (((x == 0 || x == Bot.UPPER_X_BOUND) && (y == 0 || y == Bot.UPPER_Y_BOUND))) { //Corner
				return 0.625;
			} else if (((x == 1 || x == Bot.UPPER_X_BOUND - 1) && (y == 0 || y == Bot.UPPER_Y_BOUND)) ||
						((x == 0 || x == Bot.UPPER_X_BOUND) && (y == 1 || y == Bot.UPPER_Y_BOUND - 1))) { //Corner +- 1
				return 0.5;
			} else if (x == 0 || x == Bot.UPPER_X_BOUND || y == 0 || y == Bot.UPPER_Y_BOUND) { //Edge, not corner
				return 0.425;
			} else if ((x == 1 || x == Bot.UPPER_X_BOUND - 1) && (y == 1 || y == Bot.UPPER_Y_BOUND - 1)) { //Corner 1 in
				return 0.325;
			} else if (x == 1 || y == 1 || x == Bot.UPPER_X_BOUND - 1 || y == Bot.UPPER_Y_BOUND - 1) { //Corner 1 in
				return 0.225;
			} else {
				return 0.1;
			}
		}
		if (equals(c)) {
			return 0.1;
		} else if (Math.abs(x - c.x) <= 1 && Math.abs(x - c.x) <= 1) {
			return 0.05;
		} else if (Math.abs(x - c.x) <= 2 && Math.abs(x - c.x) <= 2) {
			return 0.025;
		}
		return 0;
	}
}
