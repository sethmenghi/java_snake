//
// Project 2
// Name: Seth Menghi
// E-mail: swm36@georgetown.edu
// Instructor: Singh
// COSC 150
//
// In accordance with the class policies and Georgetown's Honor Code,
// I certify that, with the exceptions of the lecture and Blackboard 
// notes and thoseitems noted below, I have neither given nor received 
// any assistanceon this project.
//
// Description: Runs the game snake
//
package snake;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

//Keeps track of game structures
//Setup instance of board, keep track of scores
//init GUI and game
public class Game extends JFrame{

	private Board board;
	//game initial settings
	private int snakeLength = 5;
	private int windowDimensions = 400;
	
	/**
	 * Constructor that creates board and invokes initGUI().
	 * 
	 * @see initGUI()
	 * @see Board(Game, int, int)
	 */
	public Game() throws InterruptedException{
		board = new Board(this, snakeLength, windowDimensions);
		add(board);
		
		//setup GUI
		initGUI();
	}
	
	/**
	 * Initialize the GUI and start the game.
	 * 
	 * @return	none
	 */
	public void initGUI() throws InterruptedException {
		setSize(windowDimensions,windowDimensions);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDifficulty();
		board.play();
	}
	
	/**
	 * Returns array of options for JOptionPane.
	 * 
	 * @return	String[]
	 */
	public String[] getOptions(){
		String[] options = new String[4];
		options[0] = new String("Exit");
		options[1] = new String("Easy");
		options[2] = new String("Normal");
		options[3] = new String("Hard");
		return options;
	}
	
	/**
	 * Sets gameSpeed based on options from JOptionPane.
	 * 
	 * @return	none
	 * 
	 * @see board.setDifficulty()
	 */
	public void setDifficulty(){
		String message = "Control the Snake with Arrow Keys\nEat the green blocks\nAvoid the red blocks\n\nGame begins after 3 seconds.";
		String[] options = getOptions();
		int difficulty = JOptionPane.showOptionDialog(this, message, "New Game", 0, JOptionPane.INFORMATION_MESSAGE, null,options,null);
		if (difficulty == 0){
			System.exit(ABORT);
		}
		else{
			board.setDifficulty(difficulty);
		}
		try {
			//give two seconds before game starts
			Thread.sleep(500);
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Causes the game to end. Asks if want to play new game.
	 * Lets user know game is over and shows total snakeLength
	 * 
	 * @return	none
	 * @see		board.gameOver()
	 */
	public void gameOver(int score) {
		String message = "Game Over\nFood Eaten: " + score + "\n";
		JOptionPane.showMessageDialog(this, message, "Game Over",JOptionPane.INFORMATION_MESSAGE);
		try {
			dispose();
			new Game();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	public void paint(Graphics g){
//		String three = "3...";
//		String two = "2...";
//		String one = "1...";
//		final Font medium = new Font("Helvetica", Font.BOLD, 14);
//		FontMetrics metric = this.getFontMetrics(medium);
//		
//		g.setColor(Color.white);
//		g.setFont(medium);
//		try {
//			g.drawString(three, (windowDimensions - metric.stringWidth(three))/2, windowDimensions/2);
//			Thread.sleep(100);
//			g.drawString(two, (windowDimensions - metric.stringWidth(three))/2, windowDimensions/2);
//			Thread.sleep(100);
//			g.drawString(one, (windowDimensions - metric.stringWidth(three))/2, windowDimensions/2);
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//	}
	public static void main(String[] args) throws InterruptedException {
		new Game();
	}

}
