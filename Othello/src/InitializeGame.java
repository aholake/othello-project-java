import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class InitializeGame extends JApplet {

	//================================================================ constants
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final Color[]  PLAYER_COLOR = {null, Color.BLACK, Color.WHITE};
    private static final String[] PLAYER_NAME  = {null, "PLAYER", "AGENT"};
	
   //======================================================== instance variables
    private Board  _boardDisplay;
    private JTextField _statusField = new JTextField();
    private Logic  _gameLogic   = new Logic(ROWS, COLS);
	
    //============================================================== main method
    // If used as an applet, main will not be called.
    // If used as an application, main will be called.
    public static void main(String[] args) {
        JFrame window = new JFrame("OTHELLO - CS 5804 - NO DIAGONALS");
        window.setContentPane(new InitializeGame());  // Make applet content pane
        window.pack();                      // Do layout
        //System.out.println(window.getContentPane().getSize());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null); // Center window.
        window.setResizable(false);
        window.setVisible(true);            // Make window visible
    }
	
    //============================================================== constructor
    public InitializeGame() {
		
	//--- Create game buttons
    JButton newGameButton = new JButton("New Game");
    JButton firstMatchButton = new JButton("Play Agent - First Match");
    JButton alphaBetaButton = new JButton("Play Agent - Alpha Beta");
    
    //--- Create control panel
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());
    controlPanel.add(newGameButton);
    controlPanel.add(firstMatchButton);
    controlPanel.add(alphaBetaButton);
    
    //--- Create component to display board
    _boardDisplay = new Board();
    
    //--- Set the layout and add the components
    this.setLayout(new BorderLayout());
    this.add(controlPanel , BorderLayout.NORTH);
    this.add(_boardDisplay, BorderLayout.CENTER);
    this.add(_statusField , BorderLayout.SOUTH);
    _statusField.setText(PLAYER_NAME[_gameLogic.getNextPlayer()]
            + " to play");
    
    //-- Add action listeners to buttons
    newGameButton.addActionListener(new NewButtonHandler());
    firstMatchButton.addActionListener(new FirstMatchHandler());
    alphaBetaButton.addActionListener(new AlphaBetaHandler());
}
	
    //////////////////////////////////////////////////inner class NewButtonHandler
    private class NewButtonHandler implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		_gameLogic.reset();
    		_boardDisplay.repaint();
    		 _statusField.setText(PLAYER_NAME[_gameLogic.getNextPlayer()]
                     + " to play");
    	}
    }
    
    //////////////////////////////////////////////////inner class FinishButtonHandler
    private class FirstMatchHandler implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		if (PLAYER_NAME[_gameLogic.getNextPlayer()] == "PLAYER") {
    			_gameLogic.changePlayer();
    		}
    		agentMove("First Match");
    		//_gameLogic.firstMatch();
    	}
    }
    
    //////////////////////////////////////////////////inner class FinishButtonHandler
    private class AlphaBetaHandler implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		if (PLAYER_NAME[_gameLogic.getNextPlayer()] == "PLAYER") {
    			_gameLogic.changePlayer();
    		}
    		agentMove("Alpha Beta");
    		//_gameLogic.alphaBeta();
    	}
    }
	
    //////////////////////////////////////////////////////inner class GameBoard
    // This is defined inside the outer class to use things from it:
    //    * The game logic and alphaBeta logic.
    //    * The number of rows and cols.
    private class Board extends JComponent implements MouseListener {
		
    	//============================================================ constants
        private static final int CELL_SIZE = 60; // Pixels
        private static final int WIDTH  = COLS * CELL_SIZE;
        private static final int HEIGHT = ROWS * CELL_SIZE;
		
        //========================================================== constructor
        public Board() {
            this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
            this.addMouseListener(this);  // Listen to own mouse events
        }
		
      //========================================================= paintComponent
        @Override public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            //... Paint background
            g2.setColor(Color.GREEN);
            g2.fillRect(0, 0, WIDTH, HEIGHT);
            
            //... Paint grid
            g2.setColor(Color.BLACK);
            for (int r=1; r<ROWS; r++) {
                g2.drawLine(0, r*CELL_SIZE, WIDTH, r*CELL_SIZE);
            }
            for (int c=1; c<COLS; c++) {
                g2.drawLine(c*CELL_SIZE, 0, c*CELL_SIZE, HEIGHT);
            }
            
            //... Draw player pieces
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    int x = c * CELL_SIZE;
                    int y = r * CELL_SIZE;
                    int who = _gameLogic.getPlayerAt(r, c);
                    if (who != _gameLogic.EMPTY) {
                        g2.setColor(PLAYER_COLOR[who]);
                        g2.fillOval(x+2, y+2, CELL_SIZE-4, CELL_SIZE-4);
                    }
                }
            }
        }

        //=================================================== ignore these events
        @Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub		
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub	
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub	
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub	
		}

		//================================================ listener mouseReleased
        // When the mouse is released, the coordinates are 
		//		 translated into a row and column.
		@Override
		public void mouseReleased(MouseEvent e) {
			int col = e.getX() / CELL_SIZE;
            int row = e.getY() / CELL_SIZE;
            
            boolean gameOver = _gameLogic.getGameStatus() != 0;
            int currentOccupant = _gameLogic.getPlayerAt(row, col); // Check for an occupant
            int testLegalMove = _gameLogic.getLegalMove(row, col);	// Test for a legal move
            if (!gameOver && PLAYER_NAME[_gameLogic.getNextPlayer()] == "PLAYER" &&
            		currentOccupant == _gameLogic.EMPTY && testLegalMove == _gameLogic.LEGAL) {
                //... Make the move.
                _gameLogic.move(row, col);
                
                //... Report what happened in status field.
                switch (_gameLogic.getGameStatus()) {
                    case 1:
                        //... Player one wins.  Game over.
                        _statusField.setText("PLAYER WINS");
                        break;
                    case 2:
                        //... Player two wins.  Game over.
                        _statusField.setText("AGENT WINS");
                        break;
                        
                    case Logic.TIE:  // Tie game.  Game over.
                        _statusField.setText("TIE GAME");
                        break;
                        
                    default:
                        _statusField.setText(PLAYER_NAME[_gameLogic.getNextPlayer()]
                                + " to play");
                }
                
            } else {  // Move is not legal
                Toolkit.getDefaultToolkit().beep();
            }
            
            this.repaint();  // Show updated board  
		}
	}

	public void agentMove(String choice) {
		// TODO Auto-generated method stub
		int i=0, j=0;
		
		//Depending on the Agent Button Pressed:
		//	* The appropriate agent logic is called.
		//	* The resulting move is returned.
		if (choice == "First Match") {
			int result[] = _gameLogic.firstMatch();
			i = result[0];
			j = result[1];
		}
		if (choice == "Alpha Beta") {
			int result[] = _gameLogic.alphaBeta();
			i = result[0];
			j = result[1];
		}		
				
		boolean gameOver = _gameLogic.getGameStatus() != 0;
        int currentOccupant = _gameLogic.getPlayerAt(i, j);
        int testLegalMove = _gameLogic.getLegalMove(i, j);
        if (!gameOver && PLAYER_NAME[_gameLogic.getNextPlayer()] == "AGENT" && 
        		currentOccupant == _gameLogic.EMPTY && testLegalMove == _gameLogic.LEGAL) {
            //... Make the move.
            _gameLogic.move(i, j);
            
            //... Report what happened in status field.
            switch (_gameLogic.getGameStatus()) {
                case 1:
                    //... Player one wins.  Game over.
                    _statusField.setText("PLAYER WINS");
                    break;
                case 2:
                    //... Player two wins.  Game over.
                    _statusField.setText("AGENT WINS");
                    break;
                    
                case Logic.TIE:  // Tie game.  Game over.
                    _statusField.setText("TIE GAME");
                    break;
                    
                default:
                    _statusField.setText(PLAYER_NAME[_gameLogic.getNextPlayer()]
                            + " to play");
            }        
            
        } else {  // Move is not legal
            Toolkit.getDefaultToolkit().beep();
        }
        
        this.repaint();  // Show updated board		
	}
}
