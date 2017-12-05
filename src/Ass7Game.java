import biuoop.GUI;
import animation.AnimationRunner;
import game.GameFlow;
import io.LevelSpecificationReader;
import levels.LevelInformation;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Ass7Game.
 * Create a game object, initializes and runs it.
 * The game should include a paddle (which is controlled by the left and right arrows), two balls, and blocks.
 * The blocks should be arranged in a pattern following the image below. The exact sizes and colors are not important,
 * But choose reasonable sizes and make each row a different color. Each block should start with one hit.
 * except for the blocks at the top row that should start with two hits.
 * Blocks should lose hits when hit (until they reach X, and then they should not change any more).
 You should have tall or wide blocks at the borders of the screen so that the ball never travels outside of the screen.
 */
public class Ass7Game {
    /**
     * .
     * isNumeric.
     *
     * @param str the input string.
     * @return true if the string is double else false.
     */
    public static boolean isNumeric(String str) {
        //match a number with optional '-' and decimal.
        //In other case we check if the string s of the constant.
        return str.matches("-?\\d+(\\.\\d+)?");
    }


    /**
     * The main method.
     * When run without arguments, you should start a game with four levels that run one after the other.
     * When run with additional arguments the arguments should be treated as a list of level numbers to run
     * in the specified order.
     * Discard (ignore) any argument which is not a number, or not in your levels range.
     *
     * @param args are the levels we want to run.
     */
    public static void main(String[] args) {
        //Instalizing the gui and the keyboard sensor
        GUI guiGame = new GUI("Space invaders", 800, 600);
        //Initializing and running the gameLevel.
        String path = "definitions/level_definitions.txt";
        try {
            //Reading the level.
            LevelSpecificationReader li = new LevelSpecificationReader();
            GameFlow gameFlow = new GameFlow(new AnimationRunner(guiGame), guiGame.getKeyboardSensor()
                    , guiGame.getDialogManager());
            //Input stream to get in the resources folder.
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
            InputStreamReader reader = new InputStreamReader(is);
            LevelSpecificationReader levelReader = new LevelSpecificationReader();
            //In this case levels contain only one level.
            List<LevelInformation> levels = levelReader.fromReader(reader);
           gameFlow.createMenu(levels.get(0));


            guiGame.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
