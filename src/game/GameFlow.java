package game;
import animation.AnimationRunner;
import animation.KeyPressStoppableAnimation;
import animation.LoseScreen;
import animation.HighScoresAnimation;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import io.HighScoresTable;
import io.ScoreInfo;
import levels.LevelInformation;
import listeners.Counter;
import menu.Menu;
import menu.MenuAnimation;
import menu.Task;

import java.io.File;
import java.io.IOException;

/**.
 * GameFlow.
 * This class will be in charge of creating the differnet levels, and moving from one level to the next.
 */
public class GameFlow {
    static final int STARTLIVES = 3;
    private KeyboardSensor keyboardSensor;
    private AnimationRunner animationRunner;
    private DialogManager dialogManager;
    //The counter of lives.
    private Counter remainedLives;
    //The counter of scores.
    private Counter scores;
    //An highscore table.
    private HighScoresTable highScoresTable;
    /**.
     * Creates new instance of GameFlow.
     * The constructor of our class.
     *
     * @param ar an animation runner.
     * @param ks a keyboard sensor.
     * @param dialogManager a dialog manager in order to add player to the score board.
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, DialogManager dialogManager) {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.dialogManager = dialogManager;
        //Instalizing the counters.
        this.remainedLives = new Counter(STARTLIVES);
        this.scores = new Counter(0);
        //Instalizing the high score table to be with default size.
        this.highScoresTable = new HighScoresTable(HighScoresTable.DEFAULTSIZE);
    }
    /**.
     * loadHighScores.
     *  Loading the high scores.
     *  If it is the first time we run the game, a high-scores file does not exist.
     *  And we create a new table and immediately save it to a file.
     *  In the next time we run the game on the same computer, a file will exist.
     *  And so the table will be read from that file.
     */
    private void loadHighScores() {
        //The file of scores.
        File f = new File("highscores");
        //   HighScoresTable s = HighScoresTable.loadFromFile(f);
        try {
            //Loading the table of scores.
            this.highScoresTable.load(f);
        } catch (Exception e) { //Problems with reading the file.
            //The table is already instalized so we have only to save it.
            try { //Saving the file.
                this.highScoresTable.save(f);
            } catch (IOException eSave) {
                throw new RuntimeException("Problem with saving the file");
            }
        }
    }
    /**.
     * showHighScores.
     * Showing the high scores.
     */
    private void showHighScores() {
        //Showing the high scores table if there is no high scores showing empty table.
        this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor, KeyboardSensor.SPACE_KEY
                , new HighScoresAnimation(this.highScoresTable)));
    }
    /**.
     * insertHighScore.
     * When the game ends, check if the player's score entitles him to be listed on the high-scores table.
     * If it does, ask the player for his name, add his name to the table, and save the table
     */
    private void insertHighScore() {
        //We can add the score only if we have place.
        if (this.highScoresTable.getRank(this.scores.getValue()) <= this.highScoresTable.size()) {
            DialogManager dialog = this.dialogManager;
            //Opening a dialog manager.
            String name = dialog.showQuestionDialog("Name", "What is your name?", "");
            //Adding the current score.
            this.highScoresTable.add(new ScoreInfo(name, this.scores.getValue()));
            File f = new File("highscores");
            try { //Saving the scores after adding the score.
                this.highScoresTable.save(f);
            } catch (IOException e) {
                throw new RuntimeException("Problems with saving the file");
            }
        }
    }
    /**.
     * runLevels.
     * Running the levels of the game.
     * Each levels are the same except for the speed of the aliens.
     *
     * @param firstLevel the first level of the game.
     */
    public void runLevels(LevelInformation firstLevel) {
        int levelNumber = 1;
        double speed = 1;
        while (this.remainedLives.getValue() > 0) {
            String levelName = "level_name:Battle no ." + levelNumber;
            GameLevel level = new GameLevel(firstLevel, this.keyboardSensor, this.animationRunner, this.remainedLives
                    , scores, levelName, speed);
            level.initialize();
            //The level has more blocks and lives.
            while (!level.noMoreBlocks() && this.remainedLives.getValue() > 0) {
                level.playOneTurn();
            }
            levelNumber++;
            //Increasing the speed by 10% every level.
            speed *= 1.1;
        }
        //No more lives
                //Adding the high score before the losing animation.
                insertHighScore();
                this.animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor
                        , KeyboardSensor.SPACE_KEY, new LoseScreen(scores.getValue())));

    }

    /**.
     * createMenu.
     * Creating and running the game by menu selections.
     * Instead of doing infinitie while loop like in the example in the instructions i did it recursive.
     *
     * @param firstLevel the first level of the game.
     */
    public void createMenu(LevelInformation firstLevel) {
        //Our animation runner.
        AnimationRunner runner = this.animationRunner;
        //Implementing anonymmous classes of each class becasue they are short classes.
        /**.
         * highScores.
         * This task will show the high scores.
         */
        Task<Void> highScores = new Task<Void>() {
            @Override
            public Void run() {
                showHighScores();
                //Back to menu.
                createMenu(firstLevel);
                return null;
            }
        };
        /**.
         * exit.
         * This task will exit the game.
         */
        Task<Void> exit = new Task<Void>() {
            @Override
            public Void run() {
                return null;
            }
        };
        /**.
         * game.
         * This task will run the game by his levels.
         */
        Task<Void> game = new Task<Void>() {
            @Override
            public Void run() {
                //Reset the lives
                remainedLives.increase(STARTLIVES - remainedLives.getValue());
                //Reset the scores.
                scores.decrease(scores.getValue());
                //Running the levels.
                runLevels(firstLevel);
                //At the end of the game showing high scores.
                showHighScores();
                //Back to menu.
                createMenu(firstLevel);
                return null;
            }
        };
        //Adding the selections to our menu.
        Menu<Task<Void>> menu = new MenuAnimation<Task<Void>>("Menu", this.keyboardSensor);
        menu.addSelection("h", "High scores", highScores);
        menu.addSelection("s", "Game", game);
        menu.addSelection("e", "Exit", exit);

        //Loading the high scores to prevent problems in adding sores.
        loadHighScores();
        runner.run(menu);
        // wait for user selection
        Task<Void> task = menu.getStatus();
        //Running the selection.
        task.run();
    }
}
