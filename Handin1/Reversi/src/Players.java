import java.util.Scanner;


public class Players {
	
	private static final int BLACK_INDEX = 0;
	private static final int WHITE_INDEX = 1;
	private static final char HUMAN = 'p';
	private static final char COMPUTER = 'c';
	private static int BLACK_DEPTH;
	private static int WHITE_DEPTH;
	private static int BLACK_ALGORITHM;
	private static int WHITE_ALGORITHM;

	private long blackTime;
	private long whiteTime;
	private Scanner scan;
	private int color;
	private Board b;
	
	private char[] player;
	
	public Players(String config, Scanner scan) {
		if (config == null || config.length() != 3 || (config.charAt(0) != 'c' && config.charAt(0) != 'p') || config.charAt(1) != 'v' || (config.charAt(2) != 'c' && config.charAt(2) != 'p')) {
			//Standard setup
			System.out.println("Incorrect setup. Terminating.");
			System.exit(1);
		}
		
		//Create board
		b = new Board();
		
		//Create player configs
		player = new char[2];
		player[BLACK_INDEX] = config.charAt(0);
		player[WHITE_INDEX] = config.charAt(2);
		
		//Initialize starting color
		color = Board.BLACK;
		
		//Scanner
		this.scan = scan;
		
		//Computer depths
		if (player[BLACK_INDEX] == COMPUTER) {
			System.out.println("Settings for black computer:");
			System.out.println("Thinktime (ms):");
			try {
				blackTime = scan.nextLong();
			} catch (Exception e) {
				System.out.println("Incorrect input. Terminating.");
				System.exit(1);
			}
			
			System.out.println("Difficulty:");
			try {
				BLACK_DEPTH = scan.nextInt();
			} catch (Exception e) {
				System.out.println("Incorrect input. Terminating.");
				System.exit(1);
			}
			
			System.out.println("Algorithm (1 = minimax, 2 = alphabeta):");
			try {
				BLACK_ALGORITHM = scan.nextInt();
				if (BLACK_ALGORITHM != 1 && BLACK_ALGORITHM != 2) {
					throw new Exception("Incorrect input");
				}
			} catch (Exception e) {
				System.out.println("Incorrect input. Terminating.");
				System.exit(1);
			}
		}
		
		if (player[WHITE_INDEX] == COMPUTER) {
			System.out.println("Settings for white computer:");
			System.out.println("Thinktime (ms):");
			try {
				whiteTime = scan.nextLong();
			} catch (Exception e) {
				System.out.println("Incorrect input. Terminating.");
				System.exit(1);
			}
			
			System.out.println("Difficulty:");
			try {
				WHITE_DEPTH = scan.nextInt();
			} catch (Exception e) {
				System.out.println("Incorrect input. Terminating.");
				System.exit(1);
			}
			
			System.out.println("Algorithm (1 = minimax, 2 = alphabeta):");
			try {
				WHITE_ALGORITHM = scan.nextInt();
				if (WHITE_ALGORITHM != 1 && WHITE_ALGORITHM != 2) {
					throw new Exception("Incorrect input");
				}
			} catch (Exception e) {
				System.out.println("Incorrect input. Terminating.");
				System.exit(1);
			}
		}

		//Print startingboard
		b.printBoard();
	}
	
	public void play() {
		Move m;
		if (color == Board.BLACK) {
			if (player[BLACK_INDEX] == HUMAN) {
				System.out.println("Black to play (HMN)");
				System.out.print("Input move to play (e.g. a1): ");
				String str = scan.nextLine();
				System.out.println();
				color = b.play(str, color);
			} else {
				System.out.println("Black to play (CPU)");
				if (BLACK_ALGORITHM == 1) {
					m = Searches.miniMaxValue(b, color, BLACK_DEPTH, blackTime);
				} else {
					m = Searches.abSearch(b, color, BLACK_DEPTH, blackTime);
				}
				color = b.play(m, color);
				System.out.println(m.toString());
			}
		} else {
			if (player[WHITE_INDEX] == HUMAN) {
				System.out.println("White to play (HMN)");
				System.out.print("Input move to play (e.g. a1): ");
				String str = scan.nextLine();
				System.out.println();
				color = b.play(str, color);
			} else {
				System.out.println("White to play (CPU)");
				if (WHITE_ALGORITHM == 1) {
					m = Searches.miniMaxValue(b, color, WHITE_DEPTH, whiteTime);
				} else {
					m = Searches.abSearch(b, color, WHITE_DEPTH, whiteTime);
				}
				color = b.play(m, color);
				System.out.println(m.toString());
			}
		}
		b.printBoard();
	}
}
