package game;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import listeners.HitListener;
import listeners.HitNotifier;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * Paddle.
 * Is the player in the game.
 * It is a rectangle that is controlled by the arrow keys, and moves according to the player key presses.
 * It should implement the Sprite and the Collidable interfaces.
 * It should also know how to move to the left and to the right.
 */
public class Paddle implements Sprite, Collidable, HitNotifier {
    //The key pressed
    private biuoop.KeyboardSensor keyboard;
    //The rectangle represents paddle.
    private Rectangle rectangle;
    //The color of the paddle.
    private java.awt.Color color;
    //The speed of the paddle - the steps he move left and right.
    private int speed;
    //Determines if first frame.
    private boolean firstFrame;
    private GameLevel game;
    private GameEnvironment gameEnvironment;
    private int counterSeconds;
    //List of hit listeners.
    private List<HitListener> hitListeners;
    //Bullet collection.
    private List<Ball> bullets;
    private boolean canShoot;
    /**.
     * Creates new instance of Paddle.
     * The constructor of our class Paddle.
     * @param  rectangle the rectangle that represents paddle.
     * @param keyboard the key pressed (or not) in the keyboard.
     * @param color the color of the paddle.
     */
    public Paddle(Rectangle rectangle, biuoop.KeyboardSensor keyboard, java.awt.Color color) {
        this.rectangle = rectangle;
        this.keyboard = keyboard;
        this.color = color;
        //Setting a value to speed.
        this.speed = 5;
        //Setting first frame to be true
        this.firstFrame = true;
        //Setting can shoot to be true.
        this.canShoot = true;
        //Setting the counter of seconds to be 0 first.
        this.counterSeconds = 0;
        //Initializing the list.
        this.hitListeners = new ArrayList<HitListener>();
        this.bullets = new ArrayList<Ball>();
    }
    /**.
     * setSpeed.
     * Setting a the paddle's speed.
     *
     * @param speedPaddle the speed we want the paddle will be.
     */
    public void setSpeed(int speedPaddle) {
        this.speed = speedPaddle;
    }

    /**.
     * setEnvironment.
     * Setting a the paddle's game environment.
     *
     * @param environment the gameEnvironment we want the paddle will be in
     */
    public void setEnvironment(GameEnvironment environment) {
        this.gameEnvironment = environment;
    }
    /**.
     * setGame.
     * Setting a the paddle's game level.
     *
     * @param gameLevel the game we want the paddle will be in.
     */
    public void setGame(GameLevel gameLevel) {
        this.game = gameLevel;
    }
    /**.
     * moveLeft.
     * Moving the paddle left.
     * We will change the upper left point's x- cordinate to move it left.
     */
    public void moveLeft() {
        //Where the new rectangle should be
        double newX = this.rectangle.getUpperLeft().getX() - this.speed;
        //Check if it is passing the margins of the screen.
        if (newX <= GameLevel.SCREENMARGIN) {
            newX = GameLevel.SCREENMARGIN;
        }
        this.rectangle = new Rectangle(new Point(newX, this.rectangle.getUpperLeft().getY())
                , this.rectangle.getWidth(), this.rectangle.getHeight());

    }
    /**.
     * moveRight.
     * Moving the paddle right.
     * We will change the upper left point's x- cordinate to move it right.
     */
    public void moveRight() {
        //Where the new rectangle should be
        double newX = this.rectangle.getUpperLeft().getX() + this.speed;
        double newRightX = this.rectangle.getUpperRight().getX() + this.speed;

        if (newRightX >= GameLevel.GUIWIDTH - GameLevel.SCREENMARGIN) {
            newX = GameLevel.GUIWIDTH - GameLevel.SCREENMARGIN - this.rectangle.getWidth();
        }
        //Check if it is passing the margins of the screen.
        this.rectangle = new Rectangle(new Point(newX, this.rectangle.getUpperLeft().getY())
                , this.rectangle.getWidth(), this.rectangle.getHeight());
    }
    /**.
     * shoot.
     * Shoots a bullet from this paddle.
     */
    private void shoot() {
        Point pos = new Line(this.getCollisionRectangle().getUpperLeft()
                , this.getCollisionRectangle().getUpperRight()).middle(); //The miidle of bottom of the alien.
        //Put the ball above the paddle to prevent it disappear.
        pos.setY(pos.getY() - 5);
        //Creating the bullet in the middle of the bottom of the alien.
        Ball bullet = new Ball(pos, 3, Color.white);
        //Setting the bullet velocity to be align to y axis.
        bullet.setVelocity(Velocity.fromAngleAndSpeed(180, 600));
        bullet.setGameEnvironment(this.gameEnvironment);
        bullet.addToGame(this.game);
        //Adding the bullet to list of bullets.
        this.bullets.add(bullet);
    }
    // Sprite
    /**.
     * timePassed.
     * Check if the "left" or "right" keys are pressed, and if so move it accordingly.
     *
     * @param dt  It specifies the amount of seconds passed since the last call.
     * As we will be dealing with speeds that show many frames per second.
     * Each invocation will result in a small value for dt.
     * For example, in case we set 60 frames per second the dt value will be 1/60
     */
    public void timePassed(double dt) {
        //dt = 1/60
        this.counterSeconds += 60 * dt;
        //updating speed by dt
        if (this.firstFrame) {
            double updated = (double) dt * this.speed;
            this.speed = (int) updated;
            this.firstFrame = false;
        }
        //Left key pressed
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        }
        //Right key pressed
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
        //25 is closer to 0.35 of second.
        if (this.counterSeconds % 21 == 0) {
            canShoot = true;
        }
        if (this.counterSeconds % 21 != 0 && !canShoot) {
            canShoot = false;
        }
        //Space to shoot a bullet.
        if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            if (canShoot) {
                shoot();
                canShoot = false;
            }
        }
    }
    /**.
     * drawOn.
     * Draw the paddle to the screen.
     * We will use drawrectangle function that we already created.
     * @param d the drawsurfce.
     */
    public void drawOn(DrawSurface d) {
        this.getCollisionRectangle().drawRectangle(this.color, d);
    }

    // Collidable
    /**.
     * getCollisionRectangle.
     * @return the rectangle represents the paddle.
     */
    public Rectangle getCollisionRectangle() {
        return this.rectangle;

    }
    /**.
     * hit.
     * In this game when ball hits a paddle it disappears and we lose life.
     *
     * @param hitter the ball hitted the paddle.
     * @param  collisionPoint the point that the ball collided with the paddle.
     * @param currentVelocity the velocity that the ball collided with the paddle.
     * @return the velocity after the collide.
     */
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.notifyHit(hitter);
        return currentVelocity;
    }
    /**.
     * addToGame.
     * Add this paddle to the game.
     * We will do it by adding it as a colidable and as a sprite to the game.
     * @param g the game we want to add the paddle to.
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);

    }

    /**
     * removeFromGame.
     * This method will be in charge of removing the paddle from the gameLevel.
     *
     * @param gameLevel the gameLevel we want to remove the paddle from.
     */
    public void removeFromGame(GameLevel gameLevel) {
        //Removing it from the collidables collection.
        gameLevel.removeCollidable(this);
        //Removing it from the sprites collection.
        gameLevel.removeSprite(this);
    }

    //Hit notifier.
    /**
     * addHitListener
     * Add hl as a listener to hit events.
     *
     * @param hl an HitListener.
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }
    /**
     * removeHitListener
     * Remove hl from the list of listeners to hit events.
     *
     * @param hl an HitListener.
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    /**
     * notifyHit.
     * Will be called whenever a hit() occurs.
     * And notifiers all of the registered HitListener objects by calling their hitEvent method.
     *
     * @param hitter the ball hitted the block.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }
    /**
     * clearBullets.
     * Clearing from the screen the bullet shooted by this paddle.
     */
    public void clearBullets() {
        for (Ball bullet: this.bullets) {
            //remove the current bullets from the game.
            bullet.removeFromGame(this.game);
        }
        //Clearing the list too.
        this.bullets.clear();
    }
}