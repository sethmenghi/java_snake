package snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;


// This class setups the snake
public class Snake {
	
	private Board board;
	private int boardDimensions;
	
	//dimensions of one block (food, barriers, snake)
	private int dimensions = 10;
	private int length; 
	private final int startingLength;
	
	private enum Direction {RIGHT, LEFT, UP, DOWN};
	Queue<Direction> directionQueue = new ArrayDeque<Direction>();
	
	//start moving to the right
	private Direction currentDirection = Direction.RIGHT;
	
	//start position
	private int headX;
	private int headY;

	//arrays that hold body
	private ArrayList<Integer> bodyY = new ArrayList<Integer>();
	private ArrayList<Integer> bodyX = new ArrayList<Integer>();
	//arrays that hold location of each barrier
	private ArrayList<Integer> barriersY = new ArrayList<Integer>();
	private ArrayList<Integer> barriersX = new ArrayList<Integer>();
	
	//starting food spot
	private int foodY;
	private int foodX;
	Random rand = new Random();
	
	/**
	 * Snake Constructor - Sets board, starting length, & inits body array
	 * 
	 * @param	Board	parent board to communicate with
	 * @param 	int		initial length of snake
	 */
	Snake(Board board, int snakeLength){
		if (board == null){
			this.board = new Board(null, 5, 400);
		}
		else {
			this.board = board;
		}
		
		length = startingLength = snakeLength;
		try {
			boardDimensions = board.getBoardDimensions();
		}
		catch(NullPointerException e){
			boardDimensions = 400;
		}
		
		//snake will spawn in the middle of the board
		headX = boardDimensions/2;
		headY = boardDimensions/2;
		
		//adds initialsnake based on snake length
		initSnake();
	}
	public void initSnake(){
		for (int i = startingLength - 1; i >= 0; i--){
			bodyY.add(0, headY);
			bodyX.add(0, headX - (i*dimensions));
		}
		newFood();
	}
	/**
	 * Paints the snake, food, & barriers
	 *
	 * @param 	Graphics2D
	 * @throws	InterruptedException
	 */
	public void paint(Graphics2D snakeGraphic) throws InterruptedException {
		
		//paint each barrier
		for(int i = 0; i < barriersX.size(); i++){
			snakeGraphic.setColor(Color.RED);
			snakeGraphic.fillRect(barriersX.get(i), barriersY.get(i), dimensions, dimensions);
		}
		//paint current food
		snakeGraphic.setColor(Color.GREEN);
		snakeGraphic.fillRect(foodX,  foodY, dimensions, dimensions);

		//paint each block of snake last
		for(int i = 0; i < length; i++){
			snakeGraphic.setColor(Color.WHITE);
			snakeGraphic.fillRect(bodyX.get(i), bodyY.get(i), dimensions, dimensions);
		}
	}

	/**
	 * keyPressed
	 * 
	 * listener called by Board, gives key a direction
	 * 
	 * @param KeyEvent
	 * @see Board.initListeners()
	 */
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (currentDirection != Direction.RIGHT){
				directionQueue.add(Direction.LEFT);
			}
			break;
		case KeyEvent.VK_RIGHT:
			if (currentDirection != Direction.LEFT){
				directionQueue.add(Direction.RIGHT);
			}
			break;
		case KeyEvent.VK_UP:
			if (currentDirection != Direction.DOWN){
				directionQueue.add(Direction.UP);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (currentDirection != Direction.UP){
				directionQueue.add(Direction.DOWN);
			}
			break;
		}
	}
	
	/**
	 * Maps key to headX and headY coordinate
	 * Cannot do a 360, so opposite directions don't change snake movement.
	 */
	public void mapKey() {
		//create new spaces in front of snake
		Direction newDirection = directionQueue.poll();
		//if no keys pressed, so no null pointer exception
		if (newDirection == null){
			newDirection = currentDirection;
		}
		switch(newDirection) {
			case RIGHT:
				if (currentDirection != Direction.LEFT){
					currentDirection = Direction.RIGHT;
					headX = headX + dimensions;
				}
				break;
			case LEFT:
				if (currentDirection != Direction.RIGHT){
					currentDirection = Direction.LEFT;
					headX = headX - dimensions;
				}
				break;
			case UP:
				if (currentDirection != Direction.DOWN) {
					currentDirection = Direction.UP;
					headY = headY - dimensions;
				}
				break;
			case DOWN:
				if (currentDirection != Direction.UP) {
					currentDirection = Direction.DOWN;
					headY = headY + dimensions;
				}
				break;
			
		}
		move();
	}

	/**
	 * Adds a block in front of the snake.
	 * <p>
	 * If no food eaten removes last block from body arrays.
	 * 
	 * @see checkForFood()
	 */
	public void move() {
			//remove last snake trail if no food was eaten
			bodyY.add(0, headY);
			bodyX.add(0, headX);
			//if snake hits food, it doesn't remove last block
			if(!checkForFood()){
				//remove last block to give snake movement
				bodyY.remove(length);
				bodyX.remove(length);
			}
			else {
				//place barrier based on difficulty
				if (board.getDifficulty() == 25){
					newBarrier();
				}
				//normal difficulty
				else if (board.getDifficulty() == 35 && (length % 2) == 0){
					newBarrier();
				}
				//easy difficulty
				else if (board.getDifficulty() == 45 && (length % 3) == 0){
					newBarrier();
				}
				newFood();
			}
			checkGameStatus();
	}
	
	/**
	 * Returns true if snake hits a block of food
	 * 
	 * @param Boolean
	 */
	public Boolean checkForFood(){
		//check if head hit food so needs to grow
		if (foodY == headY && foodX == headX){
			length++;
			return true; 
		}
		return false;
	}
	
	/**
	 * Adds a barrier to list of barrier cords to be painted.
	 * <p>
	 * Barriers shouldn't be created on the snake or directly in front of.
	 * @param	none
	 * @return	none
	 * @see paint()
	 * @see randomBoardCord()
	 */
	public void newBarrier(){
		int newBarrierY;
		int newBarrierX;
		
		do{
			//get random spot for barrier
			newBarrierY = randomBoardCord();
			newBarrierX = randomBoardCord();
			//make sure barrier isn't on, in front of snake or on another barrier
		}while((barriersY.contains(newBarrierY) && barriersX.contains(newBarrierX)) ||
			   (bodyY.contains(newBarrierY) && bodyX.contains(newBarrierX)) ||
			   ((headY + 20) == newBarrierY) || ((headY - 20) == newBarrierY) ||
			   ((headX + 20) == newBarrierX) || ((headX - 20) == newBarrierX));
		//add the barriers to 
		barriersX.add(0, newBarrierX);
		barriersY.add(0, newBarrierY);
	}
	
	/**
	 * Changes food coordinates to new random ones.
	 * <p>
	 * Used for when snake eats one. Cannot be invoked one top of the snake. 
	 * 
	 * @param 	none
	 * @return	none
	 * @see 	paint()
	 * @see 	randomBoardCord()
	 */
	public void newFood(){
		do{
			foodX = randomBoardCord();
			foodY = randomBoardCord();
		}while((bodyY.contains(foodX) && bodyX.contains(foodY)) ||
				(barriersY.contains(foodY) && barriersX.contains(foodX)));
		System.out.println("New Food at: (" + Integer.toString(foodX) + ", " + Integer.toString(foodY) + ")\n");
	}
	
	/**
	 * Gets a random coordinate that is on the board.
	 * <p>
	 * Used in newFood() and newBarrier().
	 * 
	 * @param	none
	 * @return	none
	 * @see newFood()
	 * @see newBarrier()
	 */
	public int randomBoardCord(){
		//Random number between 0-39 so it sticks to grid
		int boardCord = rand.nextInt(boardDimensions/10 - 10)*10;
		return boardCord;
	}
	
	/**
	 * Returns score to be viewed in JFrame.
	 * <p>
	 * Used in game.gameOver() so user can see score.
	 * 
	 * @param	none
	 * @return	none
	 * @see 	Board.gameOver()
	 */
	public int getScore(){
		int score = length - startingLength;
		return score;
	}
	
	/**
	 * Checks if the snake has gone off the board. 
	 * <p>
	 * Invokes board.gameOver(), which then invokes game.gameOver() for a new game.
	 *  
	 *  @param	none
	 *  @return	none
	 *  @see 	board.gameOver()
	 */
	public void checkGameStatus(){
		//check if snake has hit a wall
		if (headY >= boardDimensions - 20 || headY <= -10 || headX >= boardDimensions || headX <= -10){
			System.out.print("Death cords: (" + Integer.toString(headX) + ", " + Integer.toString(headY) + ")\n");
			board.gameOver();
		}
		//check if snake is on top of itself, start at 1 because 1 = head
		for (int i = 1; i < length; i++){
			if (headY == bodyY.get(i) && headX == bodyX.get(i)){
				System.out.print("Death cords: (" + Integer.toString(headX) + ", " + Integer.toString(headY) + ")\n");
				board.gameOver();
			}
		}
		//check if snake has hit a barrier
		for (int i = 0; i < barriersX.size(); i++){
			if (headY == barriersY.get(i) && headX == barriersX.get(i)){
				System.out.print("Death cords: (" + Integer.toString(headX) + ", " + Integer.toString(headY) + ")\n");
				board.gameOver();
			}
		}
	}
	
	public int getDimensions(){
		return dimensions;
	}
	
	public ArrayList<Integer> getBarrierX(){
		return barriersX;
	}
	
	public ArrayList<Integer> getBarrierY(){
		return barriersY;
	}
}
