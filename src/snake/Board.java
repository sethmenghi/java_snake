package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

// Setup the Snake's home
public class Board extends JPanel {

	//Setup Snake
	Snake mrSnake;
	private int boardDimensions = 400;
	private int gameSpeed = 50;

	//private method game to talk to parent game
	private Game parentGame;
	
	/**
	 * Board Constructor
	 * <p>
	 * Initializes variables and listeners.
	 * 
	 * @param game				Game object
	 * @param snakeLength		length of inital snake
	 * @param boardDimensions	(eg. 400->400x400)
	 * 
	 */
	public Board(Game game, int snakeLength, int boardDimensions){
		if (game == null){
			game = null;
		}
		else {
			this.parentGame = game;
		}
		
		this.boardDimensions = boardDimensions;
		mrSnake = new Snake(this, snakeLength);
		setBackground(Color.BLACK);

		initListeners();
	}
	
	/**
	 * Sets up keyListeners and adds them to JPanel (the board).
	 * 
	 * @return	none
	 */
	public void initListeners() {
		
		KeyListener listener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e){
				mrSnake.keyPressed(e);
				
			}
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {	
			}
		};
		addKeyListener(listener);
		setFocusable(true);
	}
	
	/**
	 * Starts the game.
	 * 
	 * @return none
	 * @see Snake.mapKey()
	 */
	public void play() throws InterruptedException {
		//Use a timer instead of while (true)
		while (true) {
			mrSnake.mapKey();
			repaint();
			
			Thread.sleep(gameSpeed);
		}
	}
	
	/**
	 * Tells the game that it is over.
	 * 
	 * @see Game.gameOver()
	 */
	public void gameOver() {
		parentGame.gameOver(mrSnake.getScore());
	}
	
	/**
	 * Paints the snake, barriers, and food.
	 * 
	 * @see Snake.paint()
	 */
	@Override
	public void paint(Graphics theGraphic){
		super.paint(theGraphic);
		Graphics2D snakeGraphic = (Graphics2D) theGraphic;
		snakeGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			mrSnake.paint(snakeGraphic);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets difficulty of snake, which is just a Thread.sleep in this.paint().
	 * 
	 * @param	newDifficulty
	 * @return	none
	 * @see		paint()
	 */
	public void setDifficulty(int newDifficulty){
		int difficulty = newDifficulty;
		switch(difficulty){
			case 1:
				gameSpeed = 45;
				break;
			case 2:
				gameSpeed = 35;
				break;
			case 3:
				gameSpeed = 25;
				break;
		}
	}
	
	/**
	 * Returns board dimensions for Snake constructor.
	 * @return boardDimensions
	 */
	public int getBoardDimensions(){
		return boardDimensions;
	}
	
	/**
	 * Returns difficulty for how many snake barriers to make.
	 * @return
	 */
	public int getDifficulty(){
		return gameSpeed;
	}

}	
