import java.util.ArrayList;
import java.util.Random;


public class Bot {
	public static final int UPPER_X_BOUND = Sensor.GRID_WIDTH - 1;
	public static final int UPPER_Y_BOUND = Sensor.GRID_HEIGHT - 1;
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	private Coordinate trueLocation;
	private int direction;
	private ArrayList<Coordinate> directions;
	
	public Bot() {
		Random rng = new Random();
		int x = rng.nextInt(UPPER_X_BOUND + 1);
		int y = rng.nextInt(UPPER_Y_BOUND + 1);
		trueLocation = new Coordinate(x, y);
		directions = new ArrayList<Coordinate>();
		//Create all directions
		directions.add(new Coordinate(0, -1));
		directions.add(new Coordinate(1, 0));
		directions.add(new Coordinate(0, 1));
		directions.add(new Coordinate(-1, 0));
		//Pick random direction
		direction = rng.nextInt(4);
	}
	
	public Coordinate move() {
		Random rng = new Random();
		int oldDir = direction;
		int r = rng.nextInt(3) + 1;
		//if facing wall, change direction
		if (facingWall()) {
			do {
				direction = (direction + r) % 4;
			} while (facingWall());
		} else {
			double rand = rng.nextDouble();
			if (rand < 0.3) {
				do {
					r = rng.nextInt(3) + 1;
					direction = (direction + r) % 4;
				} while (direction == oldDir || facingWall());
			}
		}
		//note: add can not make the robot go out of bounds!
		//System.out.println("Location: " + trueLocation.toString() + ", Direction: " + directions.get(direction).toString());
		trueLocation.add(directions.get(direction));
		return getTrueLocation();
	}
	
	public void printTrueLocation() {
		System.out.print("True location: ");
		System.out.println("X: " + trueLocation.x + " Y: " + trueLocation.y);
	}
	
	public Coordinate getTrueLocation() {
		return new Coordinate(trueLocation.x, trueLocation.y);
	}
	
	private boolean facingWall() {
		return (trueLocation.x == 0 && direction == WEST) || (trueLocation.x == Bot.UPPER_X_BOUND && direction == EAST)
		|| (trueLocation.y == 0 && direction == NORTH) || (trueLocation.y == Bot.UPPER_Y_BOUND && direction == SOUTH);
	}
} 