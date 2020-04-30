import java.util.Arrays;
import java.util.Random;

public class Sensor {
	public static final int GRID_WIDTH = 8;
	public static final int GRID_HEIGHT = 8;
	private double[][] beliefState;
	private double[][] transitionModel;
	private double[][] evidence;
	public Coordinate belief;
	//Probability to move to a certain state from the edge is 71/180 in the directions along the edge and 
	//32/180 in the direction away from the edge
	//From corner 1/2 in each adjacent space
	//Anywhere else is 1/4 for each adjacent space

	public Sensor() {
		beliefState = new double[GRID_HEIGHT][GRID_WIDTH];
		transitionModel = new double[GRID_HEIGHT*GRID_WIDTH][GRID_HEIGHT*GRID_WIDTH];
		evidence = new double[GRID_HEIGHT][GRID_WIDTH];
		
		//Initialize beliefstate matrix to uniform distribution
		for (int y = 0; y < GRID_HEIGHT; ++y) {
			Arrays.fill(beliefState[y], 1.0/64);
		}
		
		//Initialize transitions
		for (int y = 0; y < GRID_HEIGHT; ++y) {
			for (int x = 0; x < GRID_WIDTH; ++x) {
				setTransitions(x, y);
			}
		}
		belief = new Coordinate(-1, -1);
	}

	public Coordinate sense(Coordinate trueLocation) {
		Random rng = new Random();
		double r = rng.nextDouble();
		Coordinate c = trueLocation;
		int dx;
		int dy;
		if (r < 0.1) {
			// "nothing"
			c = new Coordinate(-1, -1);
		} else if (r < 0.5) {
			// inner ring
			// dx and dy take the values -1, 0 and 1
			do {
				dx = -1 * rng.nextInt(3) + 1;
				dy = -1 * rng.nextInt(3) + 1;
			} while (dx == 0 && dy == 0);
			c.move(dx, dy);
		} else if (r < 0.9) {
			// outer ring
			do {
				dx = -1 * rng.nextInt(5) + 2;
				dy = -1 * rng.nextInt(5) + 2;
				// Create new randoms if new location is within the inner circle
			} while (Math.abs(dx) <= 1 && Math.abs(dy) <= 1);
			c.move(dx, dy);
		} // .1 probability of returning true location
		updateEvidence(c);
		updateBeliefState();
		return c;
	}
	
	public void printBeliefState() {
		System.out.println("BeliefState:");
		for (int y = 0; y < GRID_HEIGHT; ++y) {
			for (int x = 0; x < GRID_WIDTH; ++x) {
				if (beliefState[y][x] > 0.1) {
					System.out.println("X: " + x + " Y: " + y + " probability: " + beliefState[y][x]);
				}
			}
		}
	}
	
	public double maxProb() {
		double max = 0.0;
		for (int y = 0; y < GRID_HEIGHT; ++y) {
			for (int x = 0; x < GRID_WIDTH; ++x) {
				if (beliefState[y][x] > max) {
					max = beliefState[y][x];
					belief = new Coordinate(x, y);
				}
			}
		}
		return max;
	}
	
	private void updateBeliefState() {
		//f_t+1 = O_t * T^T * f_t
		double[][] temp  = new double[8][8];
		double OT = 0.0;
		
		double alpha = 0.0;
		for (int y = 0; y < GRID_HEIGHT*GRID_WIDTH; ++y) {
			double sum = 0.0;
			for (int x = 0; x < GRID_WIDTH*GRID_HEIGHT; ++x) {
				OT = transitionModel[x][y] * evidence[y / GRID_HEIGHT][y % GRID_WIDTH];
				sum += OT * beliefState[x / GRID_HEIGHT][x % GRID_WIDTH];
			}
			temp[y / GRID_HEIGHT][y % GRID_WIDTH] = sum;
			alpha += sum;
		}
		alpha = 1.0/alpha;
		
		//Normalize
		for (int y = 0; y < GRID_HEIGHT; ++y) {
			for (int x = 0; x < GRID_WIDTH; ++x) {
				beliefState[y][x] = alpha*temp[y][x];
			}
		}
	}
	
	private void updateEvidence(Coordinate c) { 
		for (int y = 0; y < GRID_WIDTH; ++y) {
			for (int x = 0; x < GRID_HEIGHT; ++x) {
				//probability also handles c == Coordinate.NOTHING
				evidence[y][x] = c.probability(new Coordinate(x, y));
			}
		}
	}
	
	private void setTransitions(int x, int y) {
		//x and y going from 0 to GRID_HEIGHT*GRID_WIDTH
		if (x == 0) { 
			if (y == 0) { //Top left corner
				transitionModel[0][1] = 0.5;
				transitionModel[0][8] = 0.5;
			} else if (y == Bot.UPPER_Y_BOUND) { //Bottom left corner
				transitionModel[56][48] = 0.5;
				transitionModel[56][57] = 0.5;
			} else { //Left edge
				int state = 8 * y;
				transitionModel[state][state - 8] = 71.0/180;
				transitionModel[state][state + 8] = 71.0/180;
				transitionModel[state][state + 1] = 32.0/180;
			}
		} else if (x == Bot.UPPER_X_BOUND) {
			if (y == 0) { //Top right corner
				transitionModel[7][6] = 0.5;
				transitionModel[7][15] = 0.5;
			} else if (y == Bot.UPPER_Y_BOUND) { //Bottom right corner
				transitionModel[63][62] = 0.5;
				transitionModel[63][55] = 0.5;
			} else { //Right edge
				int state = 8 * y + 7;
				transitionModel[state][state - 8] = 71.0/180;
				transitionModel[state][state + 8] = 71.0/180;
				transitionModel[state][state - 1] = 32.0/180;
			}
		} else if (y == 0) { //Top edge
			transitionModel[x][x - 1] = 71.0/180;
			transitionModel[x][x + 1] = 71.0/180;
			transitionModel[x][x + 8] = 32.0/180;
		} else if (y == Bot.UPPER_Y_BOUND) { //Bottom ledge
			int state = 56 + x;
			transitionModel[state][state - 1] = 71.0/180;
			transitionModel[state][state + 1] = 71.0/180;
			transitionModel[state][state - 8] = 32.0/180;
		} else { //Middle
			int state = y * 8 + x;
			transitionModel[state][state - 8] = 0.25;
			transitionModel[state][state - 1] = 0.25;
			transitionModel[state][state + 1] = 0.25;
			transitionModel[state][state + 8] = 0.25;
		}
	}
}
