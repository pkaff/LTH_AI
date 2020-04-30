
public class Move {
	private int x;
	private int y;
	private int color;
	
	public Move(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getColor() {
		return color;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Move)) {
			return false;
		} else {
			return getX() == ((Move)other).getX() && getY() == ((Move)other).getY() && getColor() == ((Move)other).getColor();
		}
	}
	
	@Override
	public String toString() {
		String letters = "abcdefgh";
		if (getColor() == Board.WHITE) {
			return "White: " + letters.charAt(x) + (y+1);
		} else {
			return "Black: " + letters.charAt(x) + (y+1);
		}
	}
}
