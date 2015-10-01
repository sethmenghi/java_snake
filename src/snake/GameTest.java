package snake;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class GameTest {

	@Parameters(name = "{index}: test({0},{1}) expected={2}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { 1,5, 45},
            { 2,5, 35},
            { 3,5,25},
            { 1,6, 45},
            { 2,6, 35},
            { 3,6,25},
            { 1,2, 45},
            { 2,2, 35},
            { 3,2,25},

        });
    }
	private int gameSpeed;
	private int assertedGameSpeed;
	private int snakeLength;
	private int difficulty;
    public GameTest(int targetGameSpeed, int snakeLength, int actualGameSpeed){
    	this.gameSpeed = targetGameSpeed;
    	this.assertedGameSpeed = actualGameSpeed;
    	this.snakeLength = snakeLength;
    }
    @Test
	public void test() {
		Board testBoard = new Board(null, gameSpeed, snakeLength);
		testBoard.setDifficulty(gameSpeed);
		this.difficulty = new Integer(testBoard.getDifficulty());
		assertEquals(this.difficulty, this.assertedGameSpeed);
	}

}
