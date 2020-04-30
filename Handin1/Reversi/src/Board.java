import java.util.ArrayList;


public class Board {
	//board attributes
	private int[][] board;
	public static final int BOARD_SIZE = 8;
	
	//color attributes
	public static final int WHITE = 1;
	public static final int BLACK = -1;
	public static final int EMPTY = 0;
	
	public Board() {
		//initialize board
		board = new int[BOARD_SIZE][BOARD_SIZE];
		board[3][3] = WHITE;
		board[4][4] = WHITE;
		board[3][4] = BLACK;
		board[4][3] = BLACK;
	}
	
	public Board(int[][] boardState, Move m) {
		
		//Copy current boardstate
		board = new int[BOARD_SIZE][BOARD_SIZE];
		for (int k = 0; k < BOARD_SIZE; ++k) {
			System.arraycopy(boardState[k], 0, board[k], 0, BOARD_SIZE);
		}
		//Add new move
		updateBoard(m);
	}
	
	//----------------PUBLIC----------------------
	
	public void printBoard() {
		System.out.println("Current board: ");
		System.out.println("  | a b c d e f g h");
		System.out.println("- - - - - - - - - -");
		for (int i = 0; i < BOARD_SIZE; ++i) {
			System.out.print(i + 1);
			System.out.print(" |");
			for (int j = 0; j < BOARD_SIZE; ++j) {
				if (board[i][j] == 0) {
					System.out.print(" " + ".");
				} else if (board[i][j] == 1) {
					System.out.print(" " + "W");
				} else {
					System.out.print(" " + "B");
				}
			}
			System.out.println();
		}
		System.out.println("Current score: " + calcScore(board));
		//if no legal moves, end game
		if (gameOver()) {
			System.out.println("No more legal moves, GG");
			printScore();
			System.exit(0);
		}
	}
	
	public int[][] getBoardState() {
		//Copy current boardstate
		int[][] tempBoard = new int[BOARD_SIZE][BOARD_SIZE];
		for (int k = 0; k < BOARD_SIZE; ++k) {
			System.arraycopy(board[k], 0, tempBoard[k], 0, BOARD_SIZE);
		}
		return tempBoard;
	}
	
	public boolean gameOver() {
		return calcLegalMoves(WHITE).isEmpty() && calcLegalMoves(BLACK).isEmpty();

	}
	
	public int play(Move m, int color) {
		if (!legalMove(m, color)) {
			System.out.println(m.toString() + " is an illegal move!");
			//Illegal move, return same color
			return color;
		}
		updateBoard(m);
		
		//Pass if no more legal moves
		if (color*-1 == WHITE) {
			if (calcLegalMoves(WHITE).isEmpty()) {
				return color;
			}
		} else {
			if (calcLegalMoves(BLACK).isEmpty()) {
				return color;
			}
		}
		//Correct move, return other color
		return color*-1;
	}
	
	public int play(String str, int color) {
		if (str.equals("quit")) {
			System.out.println("Thank you for playing!");
			System.exit(0);
		}
		if (str.isEmpty() || str.length() > 2 || str == null) {
			System.out.println("Illegal input, please specify move a-h and 1-8 such as a1!");
			//Illegal move return same color
			return color;
		}
		String letters = "abcdefgh";
		Move m = new Move (letters.indexOf(str.charAt(0)), Character.getNumericValue(str.charAt(1)) - 1, color);
		if (!legalMove(m, color)) {
			System.out.println(m.toString() + " is an illegal move!");
			//Illegal move, return same color
			return color;
		}
		updateBoard(m);
		
		//Opponent pass if no more legal moves
		if (color*-1 == WHITE) {
			if (calcLegalMoves(WHITE).isEmpty()) {
				return color;
			}
		} else {
			if (calcLegalMoves(BLACK).isEmpty()) {
				return color;
			}
		}
		//Correct move, return other color
		return color*-1;
	}
	
	public ArrayList<Move> getLegalMoves(int c) {
		ArrayList<Move> temp;
		
		if (c == WHITE) {
			temp = calcLegalMoves(WHITE);
		} else {
			temp = calcLegalMoves(BLACK);
		}
		
		return temp;
	}
	
	//----------------PRIVATE----------------------
	
	private void printScore() {
		int score = calcScore(board);
		if (score < 0) {
			System.out.println("Black wins by " + score*-1);
		} else if (score > 0) {
			System.out.println("White wins by " + score);
		} else {
			System.out.println("The game ends in a tie");
		}
	}
	
	private boolean legalMove(Move m, int player) {
		if (player == WHITE) {
			return calcLegalMoves(WHITE).contains(m);
		} else {
			return calcLegalMoves(BLACK).contains(m);
		}
	}
	
	private int calcScore(int[][] board) {
		int scoreSum = 0;
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				scoreSum += board[i][j];
			}
		}
		return scoreSum;
	}
	
	private ArrayList<Move> calcLegalMoves(int c) {
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		//Copy board
		int[][] tempBoard = new int[BOARD_SIZE][BOARD_SIZE];
		for (int k = 0; k < BOARD_SIZE; ++k) {
			System.arraycopy(board[k], 0, tempBoard[k], 0, BOARD_SIZE);
		}
		for (int y = 0; y < BOARD_SIZE; ++y) {
			for (int x = 0; x < BOARD_SIZE; ++x) {
				if (tempBoard[y][x] == 0) {
					tempBoard[y][x] = c;
					flip(x, y, c, tempBoard);
					if (Math.abs(calcScore(tempBoard) - calcScore(board)) > 1) {
						possibleMoves.add(new Move(x, y, c));
						//Copy board only if flips were made
						tempBoard = new int[BOARD_SIZE][BOARD_SIZE];
						for (int k = 0; k < BOARD_SIZE; ++k) {
							System.arraycopy(board[k], 0, tempBoard[k], 0, BOARD_SIZE);
						}
					} else {
						tempBoard[y][x] = 0;
					}
				}
			}
		}
		return possibleMoves; 
	}
	
	private void updateBoard(Move m) {
		int x = m.getX();
		int y = m.getY();
		int c = m.getColor();
		
		//add the move itself
		board[y][x] = c;
		flip(x, y, c, board);
	}
	
	private void flip(int x, int y, int c, int[][] board) {

		ArrayList<Move> directions = new ArrayList<Move>();
		ArrayList<Move> coords = new ArrayList<Move>();
		
		//Check all directions around current move
		for (int xd = -1; xd <= 1; ++xd) {
			for (int yd = -1; yd <= 1; ++yd) {
				if (x + xd < 0 || x + xd > 7 || y + yd < 0 || y + yd > 7 || (board[y + yd][x + xd] == 0) || (board[y + yd][x + xd] == c)) {
					continue;
				} else {
					directions.add(new Move(xd, yd, c));
				}
			}
		}
		
		//For every direction, see if we can flip that row
		for (Move m : directions) {
			int xtemp = x + m.getX();
			int ytemp = y + m.getY();
			while(board[ytemp][xtemp] != c && !(x < 0 || x > 7 || y < 0 || y > 7)) {
				
				coords.add(new Move(xtemp, ytemp, c));
				
				xtemp += m.getX();
				ytemp += m.getY();	
				if(xtemp < 0 || xtemp > 7 || ytemp < 0 || ytemp > 7) {
					coords.clear();
					break;
				}
			}
			for (Move n : coords) {
				board[n.getY()][n.getX()] = c;
			}
		}
	}	
	

}
