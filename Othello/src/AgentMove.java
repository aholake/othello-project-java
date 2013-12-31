import java.math.*;
import javax.swing.JOptionPane;

public class AgentMove {

	//================================================================ constants
	private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int EMPTY = 0;
    
  //=================================================================== fields
    private int      _nextPlayer;
    private int 	 dp = 6;
	
	public AgentMove() {	//Empty constructor
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {	//Unused
		// TODO Auto-generated method stub

	}

	////////////////////////////////////////////////////// Alpha-Beta Logic
	public int[] alphaBeta(int[][] _inputBoard) {
		// TODO Auto-generated method stub	
		
		//-- Initialize the move, the current player
		//-- and the DEPTH LIMIT!
		int chosenMove[] = {0,0};
		_nextPlayer = 2; 
		int de = 0;
		
		//Copy the passed in board to a board state for the first level
		int[][] _boardState = new int[ROWS][COLS];
		for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {	
                _boardState[r][c] = _inputBoard[r][c];	
            }
        }
		
		//-- Start the alpha-beta logic
		double resultValue = Double.NEGATIVE_INFINITY;
		double value = 0;
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLS; j++) {
				if (getLegalMoveAB(i, j, de, _inputBoard) == 1 && _inputBoard[i][j] == EMPTY) {
					_boardState = moveAB(i,j,de, _inputBoard);
					value = minValue(de, _boardState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
					if (value > resultValue) {
						chosenMove[0] = i;
						chosenMove[1] = j;
					}
					//JOptionPane.showMessageDialog(null, value);
				}
			}
		}
		String s = "Utility = " + value;
		JOptionPane.showMessageDialog(null, s);	//Show utility
		return chosenMove;
	}
	
	//============================================================ minValue
	//-- This is minValue calculation for alpha-beta
	private double minValue(int de, int[][] _boardState2, double alpha, double beta) {
		// TODO Auto-generated method stub
		
		de++;	//Increment depth counter
		if (de == dp) {
			return countUtility(de, _boardState2);
		} //else if (countUtility(de, _boardState2) > 16) {	//Alternate evaluation
			//return countUtility(de, _boardState2);
		//}
		
		//Copy the passed in board to a board state for the next level
		int[][] _boardState = new int[ROWS][COLS];
		for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {    	
                _boardState[r][c] = _boardState2[r][c];  	
            }
        }
		
		double value = Double.POSITIVE_INFINITY;
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLS; j++) {
				if (getLegalMoveAB(i, j, de, _boardState2) == 1 && _boardState2[i][j] == EMPTY) {
					_boardState = moveAB(i,j,de, _boardState2);
					value = Math.min(value, maxValue(de, _boardState, alpha, beta));
					if (value <= alpha) {
						return value;	
					}
					beta = Math.min(beta, value);
				}
			}
		}
		return value;
	}

	//============================================================ maxValue
	//-- This is maxValue calculation for alpha-beta
	private double maxValue(int de, int[][] _boardState3, double alpha, double beta) {
		// TODO Auto-generated method stub
		
		de++;
		if (de == dp) {
			return countUtility(de, _boardState3);
		} //else if (countUtility(de, _boardState3) > 16) {	//Alternate evaluation function
		//return countUtility(de, _boardState3);
	//} 
		
		//Copy the passed in board to a board state for the next level
		int[][] _boardState = new int[ROWS][COLS];
		for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {    	
                _boardState[r][c] = _boardState3[r][c];     	
            }
        }
		
		double value = Double.NEGATIVE_INFINITY;
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLS; j++) {
				if (getLegalMoveAB(i, j, de, _boardState3) == 1 && _boardState3[i][j] == EMPTY) {
					_boardState = moveAB(i,j,de, _boardState3);
					value = Math.max(value, minValue(de, _boardState, alpha, beta));
					if (value >= beta) {
						return value;	
					}
					alpha = Math.max(alpha, value);
				}
			}
		}
		return value;
	}

	
	//============================================================== Utility Function
	//-- Determines the utility at the leaf nodes
	private double countUtility(int de, int[][] _boardState) {
		// TODO Auto-generated method stub
		double util = 0;
		for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLS; j++) {
				if (_boardState[i][j] == 2) {
					util++;
				}
			}
		}	
		return util;
	}

	
	//============================================================ Move the board
	//-- Performs temporary moves during search
	public int[][] moveAB(int r, int c, int de, int[][] _boardState) {
        assert _boardState[r][c] == EMPTY;
        _boardState[r][c] = _nextPlayer;  // Record this move.
        	
        for (int i=0; i<ROWS; i++) {
			for (int j=0; j<COLS; j++) {
				int d = 0, e = 0;
				d = Math.abs(i-r);
				e = Math.abs(j-c);
				
				/** Possible logic for diagonals
				if (d > 1 && e > 1 && _board[i][j] == _nextPlayer) {
					for (int k=1; k<d; k++) {
						for (int l=1; l<e; l++) {
							if (i-r < 0 && j-c < 0) {
								_board[r-k][j-c] = _nextPlayer;
							}
							if (i-r > 0 && j-c < 0) {
								_board[r+k][j-c] = _nextPlayer;
							}
							if (i-r < 0 && j-c > 0) {
								_board[r-k][j+c] = _nextPlayer;
							}
							if (i-r > 0 && j-c > 0) {
								_board[r+k][j+c] = _nextPlayer;
							}	
						}
					}
				}
				**/
				
				if (d > 1 && _boardState[i][c] == _nextPlayer) {
					for (int k=1; k<d; k++) {
						if (i-r < 0 /*&& _board[r-k][c] == 3-_nextPlayer*/) {
							_boardState[r-k][c] = _nextPlayer;
						}
						else if (i-r > 0 /*&& _board[r+k][c] == 3-_nextPlayer*/) {
							_boardState[r+k][c] = _nextPlayer;
						}
					}
				}
				
				if (e > 1 && _boardState[r][j] == _nextPlayer) {
					for (int l=1; l<e; l++) {
						if (j-c < 0 && _boardState[r][c-l] == 3-_nextPlayer) {
							_boardState[r][c-l] = _nextPlayer;
						}
						else if (j-c > 0 && _boardState[r][c+l] == 3-_nextPlayer) {
							_boardState[r][c+l] = _nextPlayer;
						}
					}
				}
			}
		}
        
        _nextPlayer = 3-_nextPlayer; // Flip players
		return _boardState;
    }
	
	//============================================================ Determine legality
	//-- During search, we still need to determine legality 
	//--	of the specific state
	public int getLegalMoveAB(int r, int c, int de, int[][] _boardState) {
		// TODO Auto-generated method stub
		
		int oppCount = 0;
		boolean anchorDn = false;
			for (int i=r-1; i>-1; i--) {
				if (_boardState[i][c] == EMPTY) {
					break;
				} else if (_boardState[i][c] == 3-_nextPlayer) {	
					oppCount++;
				}
				if (_boardState[i][c] == _nextPlayer && oppCount > 0) {
					anchorDn = true;
					break;
				}
			}
			if (anchorDn && oppCount > 0) {
				return 1;
			}
			
			oppCount = 0;
			boolean anchorUp = false;
				for (int i=r+1; i<ROWS; i++) {
					if (_boardState[i][c] == EMPTY) {
						break;
					} else if (_boardState[i][c] == 3-_nextPlayer) {	
						oppCount++;
					}
					if (_boardState[i][c] == _nextPlayer && oppCount > 0) {
						anchorUp = true;
						break;
					}
				}
				if (anchorUp && oppCount > 0) {
					return 1;
				}
				
				oppCount = 0;
				boolean anchorRt = false;
					for (int j=c-1; j>-1; j--) {
						if (_boardState[r][j] == EMPTY) {
							break;
						} else if (_boardState[r][j] == 3-_nextPlayer) {	
							oppCount++;
						}
						if (_boardState[r][j] == _nextPlayer && oppCount > 0) {
							anchorRt = true;
							break;
						}
					}
					if (anchorRt && oppCount > 0) {
						return 1;
					}
					
					oppCount = 0;
					boolean anchorLt = false;
						for (int j=c+1; j<COLS; j++) {
							if (_boardState[r][j] == EMPTY) {
								break;
							} else if (_boardState[r][j] == 3-_nextPlayer) {	
								oppCount++;
							}
							if (_boardState[r][j] == _nextPlayer && oppCount > 0) {
								anchorLt = true;
								break;
							}
						}
						if (anchorLt && oppCount > 0) {
							return 1;
						}
		return 0;
	}
}
