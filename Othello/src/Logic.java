
public class Logic {
	
	//================================================================ constants
    public  static final int EMPTY   = 0;  // The cell is empty.
    public  static final int LEGAL   = 1;  // The move is legal.
    public  static final int PLAYER1 = 1;  // The initial value for Player.
    public  static final int TIE     = -1; // Game is a tie (draw).
    
    //=================================================================== fields
    private int     _maxRows;    // Number of rows. Set in constructor.
    private int     _maxCols;    // Number of columns.  Set in constructor.
    
    private int[][] _board;      // The board values.
    private int     _nextPlayer; // The player who moves next.
    private int     _moves = 0;  // Number of moves in the game.
    
    private AgentMove _agentMove = new AgentMove();	// Initializes an AgentMove object.
    
    //============================================================== constructor
    public Logic(int rows, int cols) {
        _maxRows = rows;
        _maxCols = cols;
        _board = new int[_maxRows][_maxCols];
        reset();
    }
    
    //============================================================ getNextPlayer
    /** Returns the next player. */
    public int getNextPlayer() {
        return _nextPlayer;
    }
    
    //============================================================ changePlayer
    /** Changes the player if there are no legal moves.*/
    public int changePlayer() {
    	_nextPlayer = 3-_nextPlayer; // Flip players
    	return _nextPlayer;
    }
    
    //============================================================== getPlayerAt
    /** Returns player who has played at particular row and column. */
    public int getPlayerAt(int r, int c) {
        return _board[r][c];
    }
    
    //==================================================================== reset
    /** Clears board to initial state. Makes first move in center. */
    public void reset() {
        for (int r = 0; r < _maxRows; r++) {
            for (int c = 0; c < _maxCols; c++) {
                _board[r][c] = EMPTY;
            }
        }
        
        _moves = 0;  // Reset moves to zero.
        _nextPlayer = PLAYER1;  
        
        //-- Fill the center four cells.
        _board[_maxCols/2-1][_maxRows/2] = _nextPlayer;
        _board[_maxCols/2-1][_maxRows/2-1] = 3-_nextPlayer;
        _board[_maxCols/2][_maxRows/2-1] = _nextPlayer;
        _board[_maxCols/2][_maxRows/2] = 3-_nextPlayer;
    }
    
    //===================================================================== move/capture
    /** Play a marker on the board, record it, flip discs, switch players. */
    public void move(int r, int c) {
        assert _board[r][c] == EMPTY;
        _board[r][c] = _nextPlayer;  // Record this move.
        	
        //Loop through all spaces to determine the distances
        //	between a player's selected move and other pieces
        //	of the same color
        for (int i=0; i<_maxRows; i++) {
			for (int j=0; j<_maxCols; j++) {
				int d = 0, e = 0;
				d = Math.abs(i-r);
				e = Math.abs(j-c);
				
				/** Possible code for diagonals
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
				
				//--- Flip and capture discs in the same column
				if (d > 1 && _board[i][c] == _nextPlayer) {
					for (int k=1; k<d; k++) {
						if (i-r < 0 /*&& _board[r-k][c] == 3-_nextPlayer*/) {
							_board[r-k][c] = _nextPlayer;
						}
						else if (i-r > 0 /*&& _board[r+k][c] == 3-_nextPlayer*/) {
							_board[r+k][c] = _nextPlayer;
						}
					}
				}
				
				//--- Flip and capture discs in the same row
				if (e > 1 && _board[r][j] == _nextPlayer) {
					for (int l=1; l<e; l++) {
						if (j-c < 0 && _board[r][c-l] == 3-_nextPlayer) {
							_board[r][c-l] = _nextPlayer;
						}
						else if (j-c > 0 && _board[r][c+l] == 3-_nextPlayer) {
							_board[r][c+l] = _nextPlayer;
						}
					}
				}
			}
		}

        _nextPlayer = 3-_nextPlayer; // Flip players
        _moves++;  // Increment number of moves.
    }
    
    //============================================================ getGameStatus
    /** -1 = game is tie, 
         0 = more to play, 
         1 = player1 wins,
         2 = player2 wins  **/
    public int getGameStatus() {
         
    	int numberOfLegalMoves = 0;
        int numWhite = 0, numBlack = 0;
        
        //-- Count the number of black pieces, white pieces
        //--	and legal moves.
        for (int i=0; i<_maxRows; i++) {
			for (int j=0; j<_maxCols; j++) {
				if (_board[i][j] == 1) {
					numBlack++;
				}
				if (_board[i][j] == 2) {
					numWhite++;
				}
				if (_board[i][j] == EMPTY && getLegalMove(i, j) == LEGAL) {
					numberOfLegalMoves++;
				}
			}
        }
        
        //-- Determine who is the winner or if there is a tie when there
        //--	are no more legal moves.
        if (numberOfLegalMoves == 0) {
        	if (numBlack > numWhite) {
        		return 1;
        	}
        	if (numWhite > numBlack) {
        		return 2;
        	}
        	if (numWhite == numBlack) {
        		return TIE;
        	}
        }
        
        return 0;
    }

  //============================================================ determine legality
    public int getLegalMove(int r, int c) {
		// TODO Auto-generated method stub
		
		int oppCount = 0;
		boolean anchorDn = false;
			for (int i=r-1; i>-1; i--) {
				if (_board[i][c] == EMPTY) {
					break;
				} else if (_board[i][c] == 3-_nextPlayer) {	
					oppCount++;
				}
				if (_board[i][c] == _nextPlayer && oppCount > 0) {
					anchorDn = true;
					break;
				}
			}
			if (anchorDn && oppCount > 0) {
				return 1;
			}
			
			oppCount = 0;
			boolean anchorUp = false;
				for (int i=r+1; i<_maxRows; i++) {
					if (_board[i][c] == EMPTY) {
						break;
					} else if (_board[i][c] == 3-_nextPlayer) {	
						oppCount++;
					}
					if (_board[i][c] == _nextPlayer && oppCount > 0) {
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
						if (_board[r][j] == EMPTY) {
							break;
						} else if (_board[r][j] == 3-_nextPlayer) {	
							oppCount++;
						}
						if (_board[r][j] == _nextPlayer && oppCount > 0) {
							anchorRt = true;
							break;
						}
					}
					if (anchorRt && oppCount > 0) {
						return 1;
					}
					
					oppCount = 0;
					boolean anchorLt = false;
						for (int j=c+1; j<_maxCols; j++) {
							if (_board[r][j] == EMPTY) {
								break;
							} else if (_board[r][j] == 3-_nextPlayer) {	
								oppCount++;
							}
							if (_board[r][j] == _nextPlayer && oppCount > 0) {
								anchorLt = true;
								break;
							}
						}
						if (anchorLt && oppCount > 0) {
							return 1;
						}
		
		
				/** Possible code for diagonals
				int d = 0, e = 0;
				d = Math.abs(i-r);
				e = Math.abs(j-c);
				if (d > 1 && e > 1 && _board[i][j] == _nextPlayer) {
					for (int k=1; k<d; k++) {
						for (int l=1; l<e; l++) {
							if (i-r < 0 && j-c < 0 && _board[r-k][j-c] == 3-_nextPlayer) {
								return 1;
							}
							if (i-r > 0 && j-c < 0 && _board[r+k][j-c] == 3-_nextPlayer) {
								return 1;
							}
							if (i-r < 0 && j-c > 0 && _board[r-k][j+c] == 3-_nextPlayer) {
								return 1;
							}
							if (i-r > 0 && j-c > 0 && _board[r+k][j+c] == 3-_nextPlayer) {
								return 1;
							}	
						}
					}
				}
				**/
						
		return 0;
	}
	
  //============================================================ Find first legal move
    //--- Method for simple Agent logic
	//--- Returns the first legal move found
	public int[] firstMatch() {
		int move[] = {0, 0};
		for (int i=0; i<_maxRows; i++) {
			for (int j=0; j<_maxCols; j++) {
				if (getLegalMove(i, j) == LEGAL && getPlayerAt(i, j) == EMPTY) {
					move[0] = i;
					move[1] = j;
				}
			}
		}
		return move;
	}

	//============================================================ Call Alpha Beta search
	//--- Creates a copy of the board and passes it into the
	//--- AlphaBeta logic to find the best move
	public int[] alphaBeta() {
		int[][] _currentBoard = new int[_maxRows][_maxCols];
		// TODO Auto-generated method stub
		for (int r = 0; r < _maxRows; r++) {
            for (int c = 0; c < _maxCols; c++) {
                _currentBoard[r][c] = _board[r][c];
            }
        }	
		return _agentMove.alphaBeta(_currentBoard);
	}
}
