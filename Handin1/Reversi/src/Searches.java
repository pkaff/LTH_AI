import java.util.ArrayList;


public class Searches {
	//--------------------------MINIMAX---------------------------------------
	public static Move miniMaxValue(Board b, int color, int depth, long time) {
		long maxTime = System.currentTimeMillis() + time;
		ArrayList<Move> legalMoves = b.getLegalMoves(color);

		//initialize bestValue
		Move bestMove = null;
		int bestValue = Integer.MIN_VALUE;
	
		//look for best move that minimizes opponents utility
		int newValue;
		for (Move m : legalMoves) {
			Board newBoard = new Board(b.getBoardState(), m);
			newValue = minValue(newBoard, -m.getColor(), depth, maxTime);
			if (newValue >= bestValue) {
				bestValue = newValue;
				bestMove = m;
			}
		}
		
		return bestMove;
	}
	
	
	private static int maxValue(Board b, int color, int depth, long maxTime) {
		int minValue;
		int newValue;
		if (depth <= 0 || b.gameOver() || System.currentTimeMillis() > maxTime) {
			return utility(b.getBoardState(), color);
		} else {
			minValue = Integer.MIN_VALUE;
			ArrayList<Move> legalMoves = b.getLegalMoves(color);
			for (Move m : legalMoves) {
				Board newBoard = new Board(b.getBoardState(), m);
				newValue = minValue(newBoard, -m.getColor(), depth - 1, maxTime);
				if (minValue < newValue) {
					minValue = newValue;
				}
			}
			
		}
		return minValue;
	}
	
	private static int minValue(Board b, int color, int depth, long maxTime) {
		int maxValue;
		int newValue;
		if (depth <= 0 || b.gameOver() || System.currentTimeMillis() > maxTime) {
			return utility(b.getBoardState(), color);
		} else {
			maxValue = Integer.MAX_VALUE;
			ArrayList<Move> legalMoves = b.getLegalMoves(color);
			for (Move m : legalMoves) {
				Board newBoard = new Board(b.getBoardState(), m);
				newValue = maxValue(newBoard, -m.getColor(), depth - 1, maxTime);
				if (maxValue > newValue) {
					maxValue = newValue;
				}
			}
			
		}
		return maxValue;
	}
	
	//-----------------------ALPHABETAPRUNING-------------------------------
	public static Move abSearch(Board b, int color, int depth, long time) {
		long maxTime = System.currentTimeMillis() + time;
		ArrayList<Move> legalMoves = b.getLegalMoves(color);

		//initialize bestValue
		Move bestMove = null;
		int bestValue = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
	
		//look for best move that minimizes opponents utility
		int newValue;
		for (Move m : legalMoves) {
			Board newBoard = new Board(b.getBoardState(), m);
			newValue = abMaxValue(newBoard, -m.getColor(), depth, maxTime, alpha, beta);
			if (newValue >= bestValue) {
				bestValue = newValue;
				bestMove = m;
			}
		}
		
		return bestMove;
	}
	
	
	private static int abMaxValue(Board b, int color, int depth, long maxTime, int alpha, int beta) {
		int minValue;
		int newValue;
		if (depth <= 0 || b.gameOver() || System.currentTimeMillis() > maxTime) {
			return utility(b.getBoardState(), color);
		} else {
			minValue = Integer.MIN_VALUE;
			ArrayList<Move> legalMoves = b.getLegalMoves(color);
			for (Move m : legalMoves) {
				Board newBoard = new Board(b.getBoardState(), m);
				newValue = abMinValue(newBoard, -m.getColor(), depth - 1, maxTime, alpha, beta);
				if (minValue < newValue) {
					minValue = newValue;
				}
				if (minValue >= beta) {
					return minValue;
				}
				if (minValue > alpha) {
					alpha = minValue;
				}
			}
		}
		return minValue;
	}
	
	private static int abMinValue(Board b, int color, int depth, long maxTime, int alpha, int beta) {
		int maxValue;
		int newValue;
		if (depth <= 0 || b.gameOver() || System.currentTimeMillis() > maxTime) {
			return utility(b.getBoardState(), color);
		} else {
			maxValue = Integer.MAX_VALUE;
			ArrayList<Move> legalMoves = b.getLegalMoves(color);
			for (Move m : legalMoves) {
				Board newBoard = new Board(b.getBoardState(), m);
				newValue = abMaxValue(newBoard, -m.getColor(), depth - 1, maxTime, alpha, beta);
				if (maxValue > newValue) {
					maxValue = newValue;
				}
				if (maxValue <= alpha) {
					return maxValue;
				}
				if (maxValue < beta) {
					beta = maxValue;
				}
			}
			
		}
		return maxValue;
	}
	
	//-----------------UTILITY--------------------------
	private static int utility(int[][] board, int color) {
		int utility = 0;
		for (int y = 0; y < Board.BOARD_SIZE; ++y) {
			for (int x = 0; x < Board.BOARD_SIZE; ++x) {
				utility += board[y][x];
			}
		}
		
		return color*(utility);
	}
}
