package game;
import animation.AnimationRunner;
import animation.KeyPressStoppableAnimation;
import animation.Animation;
import animation.PauseScreen;
import animation.CountdownAnimation;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

import invaders.Alien;
import levels.LevelInformation;
import listeners.Counter;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;
import geometry.Line;
import geometry.Rectangle;
import geometry.Point;
import java.awt.Color;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**.
 * GameLevel
 * GameLevel class that will hold the sprites and the collidables, and will be in charge of the animation.
 */
public class GameLevel implements Animation {
    //Final variables of the screen size.
    static final  int GUIWIDTH = 800;
    static final int GUIHEIGHT = 600;
    //The margin of screen where the game will be. (The blocks of screen width).
    static final int SCREENMARGIN = 25;
    static final int PADDLEHEIGHT = 20;

    //The sprite collection and game environment.
    private SpriteCollection sprites;
    private GameEnvironment environment;
    //Our keyboard sensor
    private KeyboardSensor keyboard;
    //The counter of remaining blocks.
    private Counter remainedAliens;
    //The counter of remaining balls.
    private Counter remainedBalls;
    //The counter of scores.
    private Counter scores;
    //The counter of lives.
    private Counter remainedLives;
    private AnimationRunner runner;
    //Determines if stop or not the game.
    private boolean running;
    //Level information
    private LevelInformation levelInformation;
    //List of aliens.
    private List<Alien> aliens;
    //List of blocks.
    private List<Block> blocks;
    private int count;
    private String levelName;
    private double speed;
    /**.
     * Creates new instance of GameLevel.
     * The constructor of our class GameLevel.
     * Instalize the list of sprite objects and the game environment.
     *
     * @param levelInformation a levelInformation object that contains information about the current level.
     * @param keyboardSensor a keyboard sensor.
     * @param runner an animation runner.
     * @param remainedLives a refference to the remained lives.
     * @param scores a refference to the current scores.
     * @param levelName the name of the level.
     * @param speed the first speed of the aliens in this level.
     */
    public GameLevel(LevelInformation levelInformation, KeyboardSensor keyboardSensor, AnimationRunner runner
            , Counter remainedLives, Counter scores, String levelName, double speed) {
        this.sprites = new SpriteCollection();
        this.environment = new GameEnvironment();
        this.keyboard = keyboardSensor;
        this.runner = runner;
        //Instalizing the counters.
        this.remainedAliens = new Counter(levelInformation.numberOfAliensToRemove());
        //Setting the counters.
        //Only one chance to hit the paddle.
        this.remainedBalls = new Counter(1);
        this.scores = scores;
        this.remainedLives = remainedLives;
        this.running = true;

        this.levelInformation = levelInformation;
        //Initializing the list of aliens.
        this.aliens = new ArrayList<Alien>();
        //Initializing the list of blocks.
        this.blocks = new ArrayList<Block>();
        this.count = 0;
        this.levelName = levelName;
        this.speed = speed;
    }
    /**.
     * addCollidable.
     * Add the given collidable to the game by adding it into the game environment.
     * @param  c the collideable we want to add.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**.
     * removeCollidable.
     * Remove the given collidable from the game by removing it into the game environment.
     * @param  c the collideable we want to add.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }
    /**.
     * addSprite.
     * Add the given Sprite to the game by adding it into a sprite collection.
     * @param  s the Sprite we want to remove.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**.
     * removeSprite.
     * Remove the given Sprite to the game by removing it from the sprite collection.
     * @param  s the Sprite we want to remove.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**.
     * initialize.
     * Initialize a new game: create the Blocks and Ball (and Paddle) and add them to the game.
     */
    public void initialize() {
        levelInformation.getBackground().addToGame(this);
        //Create the screen blocks.
        createScreen();
        initializeBlocks();
        initializeIndicators();
    }
    /**.
     * initializeIndicators.
     * Initialize the game indicators.
     */
    private void initializeIndicators() {
        //Adding the score indicator to the game
        ScoreIndicator si = new ScoreIndicator(this.scores);
        si.addToGame(this);
        //Adding the lives indicator to the game.
        LivesIndicator li = new LivesIndicator(this.remainedLives);
        li.addToGame(this);
        //Adding the level name to the screen.
        LevelIndicator levelIndicator = new LevelIndicator(levelName);
        levelIndicator.addToGame(this);
    }
    /**.
     * initializeBlocks
     * Initialize the game blocks.
     */
    private void initializeBlocks() {
       BlockRemover blockRemover =  new BlockRemover(this, this.remainedAliens);
        for (Alien alien: this.levelInformation.aliens()) {
            //Adding copy block because we dont won't to harm after losing because block is refference.
            Alien copy = alien.copy();
            copy.addToGame(this);
            copy.addHitListener(blockRemover);
            copy.addHitListener(new ScoreTrackingListener(this.scores));
            //Remove the ball after hitting the alien.
            copy.addHitListener(new BallRemover(this, remainedBalls));
            //Setting game level and environment.
            copy.setEnvironment(this.environment);
            copy.setGame(this);
            copy.setSpeed(speed);
            aliens.add(copy);
        }
        //Block remover is a refference so we can add it now.
            blockRemover.setAliens(aliens);
        for (Block block: this.levelInformation.blocks()) {
             blockRemover =  new BlockRemover(this, this.remainedAliens);
            //Adding copy block because we dont won't to harm after losing because block is refference.
            Block copy = block.copy();
            copy.addToGame(this);
            //No matter the counter because it is the shield
            copy.addHitListener(new BlockRemover(this, new Counter(33)));
            //Remove the ball after hitting the block.
            copy.addHitListener(new BallRemover(this, remainedBalls));
            //No score listeners because the blocks are the shileds.
            blocks.add(copy);
        }
        //Block remover is a refference so we can add it now.
        blockRemover.setAliens(aliens);
    }
    /**.
     * createScreen.
     * We notify the screen margins as 4 blocks.
     * In that function we will create each block and add it into the game.
     */
    private void createScreen() {
        //Creating the screen margins.
        Collidable topScreen = new Block(new Rectangle(new Point(0, 0)
                , GUIWIDTH, SCREENMARGIN), Color.gray);
        Collidable leftScreen = new Block(new Rectangle(new Point(0, 0)
                , SCREENMARGIN, GUIHEIGHT), Color.gray);
        Collidable rightScreen = new Block(new Rectangle(new Point(GUIWIDTH - SCREENMARGIN, 0)
                , SCREENMARGIN, GUIHEIGHT), Color.gray);
        //When the ball hits there you are die.
        Collidable bottomScreen = new Block(new Rectangle(new Point(SCREENMARGIN
                , GUIHEIGHT), GUIWIDTH, 0), new Color(255, 255, 255));
        //The screen margins array.
        Collidable[] screen = {topScreen, leftScreen, rightScreen, bottomScreen};
        //Adding each screen margins to the block.
        for (int i = 0; i < screen.length; i++) {
            Block screenPart = (Block) screen[i];
            screenPart.addToGame(this);
            //Adding ball remover to each part of the screen in this game.
            screenPart.addHitListener(new BallRemover(this, this.remainedBalls));


            //Setting to 0 in order to not removing the blocks of screen.
            //We remove blocks with 1 count hit.
            screenPart.setCountHits(0);
        }
    }
    /**.
     * gameRectangle.
     * We notify the screen margins as 4 blocks.
     * @return the rectangle of the inner screen where the game is.
     */
    private Rectangle gameRectangle() {
        return new Rectangle(new Point(SCREENMARGIN, SCREENMARGIN), GUIWIDTH - 2 * SCREENMARGIN
                , GUIHEIGHT - 2 * SCREENMARGIN);
    }
    /**
     * doOneFrame.
     * In charge of the logic.
     * In this case the logic is the game itself.
     *
     * @param d a drawsurfce.
     * @param dt  It specifies the amount of seconds passed since the last call.
     * As we will be dealing with speeds that show many frames per second.
     * Each invocation will result in a small value for dt.
     * For example, in case we set 60 frames per second the dt value will be 1/60
     */
    public void doOneFrame(DrawSurface d, double dt) {
        //Adding the seconds.
        this.count += dt * 60;
        // the logic from the previous playOneTurn method goes here.
        // the `return` or `break` statements should be replaced with
        // this.running = false;
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, new PauseScreen()));
        }

        for (Alien alien: aliens) {
            if (!alien.move(aliens, Block.highestYposition(blocks))) {
                //No more balls so we lose live.
                this.remainedLives.decrease(1);
                //Remove the paddle
                this.running = false;
                //Out from the function because we lose a life.
                return;
            }
        }
        //Shoot every half second.
        if (this.count % 30 == 0) {
            List<Alien> lowerAliens = Alien.downAliens(this.aliens);
            Random ran = new Random();
            int rnd = ran.nextInt(lowerAliens.size());
            //Random alien from bottom lines shoot a bullet.
            lowerAliens.get(rnd).shoot();
        }
            //break;
        //}
        //No more blocks at the screen
        if (this.remainedAliens.getValue() == 0) {
            //Destroying all blocks is worth another 100 points.
            this.scores.increase(100);
            //Clear the GUI resources and close the window.
            this.running = false;
        }




        //The counter is 0 when hitting the paddle so it means losing.
        if (this.remainedBalls.getValue() <= 0) {
            //No more balls so we lose live.
            this.remainedLives.decrease(1);
            //Remove the paddle
            this.running = false;
        }
        //Drawing the sprites objects.
        this.sprites.drawAllOn(d);
        //Moving the sprites we can move.
        this.sprites.notifyAllTimePassed(dt);
    }
    /**
     * shouldStop.
     * The stopping conditions are for example no more balls or blocks.
     *
     * @return true if the animation should stop, false otherwise.
     */
    public boolean shouldStop() {
        return !this.running;
    }
    /**.
     * playOneTurn.
     * Run the game -- start the animation loop.
     * We will use the object Sleeper and draw all the sprites.
     */
    public void playOneTurn() {
        //We create rectangle in order to identify where the ball, paddle and other blocks will be.
        Rectangle screen = gameRectangle();
        //Create the paddle
        Point paddlePosition = new Line(screen.getLowerLeft(), screen.getLowerRight()).middle();
        paddlePosition.setX(paddlePosition.getX() - this.levelInformation.paddleWidth() / 2);
        Paddle paddle = new Paddle(new Rectangle(paddlePosition
                , this.levelInformation.paddleWidth(), PADDLEHEIGHT), this.keyboard, Color.orange);
        //Setting the speed of the paddle.
        paddle.setSpeed(this.levelInformation.paddleSpeed());
        //Setting the game and gameenvironment.
        paddle.setEnvironment(this.environment);
        paddle.setGame(this);
        paddle.addToGame(this);
        paddle.addHitListener(new BallRemover(this, remainedBalls));
        //Return remained balls number to 1.
        if (this.remainedBalls.getValue() != 1) {
            this.remainedBalls.increase(1);
        }
        //Running the countdown
        this.runner.run(new CountdownAnimation(2, 3, this.sprites)); // countdown before turn starts.
        this.running = true;
        // use our runner to run the current animation -- which is one turn of
        // the game.
        this.runner.run(this);
        //When losing remove the bullets from the game.
        paddle.clearBullets();
        for (Alien alien: aliens) {
            alien.reset();
        }
        //Removing the paddle because we want to restart his position.
        paddle.removeFromGame(this);


    }
    /**.
     * noMoreBlocks.
     * @return true if there are more blocks, false otherwise.
     */
    public boolean noMoreBlocks() {
        return this.remainedAliens.getValue() == 0;
    }
}
