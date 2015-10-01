package snake;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;


public class SnakeTest {

    private Snake testSnake;
    private int snakeLength = 5;
    @Before
    public void setUp(){
    	this.testSnake = new Snake(null, snakeLength);
    }
    
    @Test
	public void testCheckForFood() {
		assert(testSnake.checkForFood());
	}
    
    @Test 
    public void testNewBarrier(){
    	ArrayList<Integer> oldBarriersX = testSnake.getBarrierX();
    	testSnake.newBarrier();
    	ArrayList<Integer> newBarriersX = testSnake.getBarrierX();
    	//shouldn't add one yet
    	int sizeOld = oldBarriersX.size();
    	int sizeNew = newBarriersX.size();
    	assertEquals(sizeOld, sizeNew);
    }
    
    @Test
    //make sure random coords are divisible by dimensions to line up to grid
    public void checkRandomBoardCoord(){
    	int randomBoardCoord = testSnake.randomBoardCord();
    	if ((randomBoardCoord % testSnake.getDimensions()) == 0){
    		assert(true);
    	}
    	else {
    		assert(false);
    	}
    }
    
    @Test
    public void checkScore(){
    	int currentScore = testSnake.getScore();
    	assertEquals(currentScore, 0);
    }
}
