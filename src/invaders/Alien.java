package invaders;
import game.Ball;
import game.GameEnvironment;
import game.Block;
import game.GameLevel;
import game.Velocity;

import geometry.Point;
import geometry.Rectangle;
import geometry.Line;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Alien.
 * Alien is the enemy in the space invaders game, we have to destroy all the aliens.
 */
public class Alien extends Block {
    //Final variables of the screen size.
    static final  int GUIWIDTH = 800;
    static final int GUIHEIGHT = 600;
    //The margin of screen where the game will be. (The blocks of screen width).
    static final int SCREENMARGIN = 25;
    private GameLevel game;
    private GameEnvironment gameEnvironment;
    private double speed =  1;
    private boolean moveRight;
    private boolean down;
    //The old position of the upper left.
    private Point oldPosition;
    //The old speed of the alien.
    private double oldSpeed = 1;
    //Bullet collection.
    private List<Ball> bullets;
    /**.
     * Creates new instance of Block
     * The constructor of our class Block.
     * @param  block the rectangle that represents block.
     * @param color the color of the block.
     */
    public Alien(Rectangle block, Color color) {
        super(block, color);
        this.moveRight = true;
        this.oldPosition = block.getUpperLeft();
        this.down = false;
        this.bullets = new ArrayList<Ball>();
    }

    /**.
     * Creates new instance of Block
     * The constructor of our class Block.
     * @param  block the rectangle that represents block.
     * @param image the background image of the block.
     */
    public Alien(Rectangle block, Image image) {
        super(block, image);
        this.oldPosition = block.getUpperLeft();
        this.moveRight = true;
        this.down = false;
        this.bullets = new ArrayList<Ball>();
    }
    /**.
     * setSpeed.
     * Setting the speed of the alien.
     *
     * @param speedAlien the speed we want to alien will be.
     */
    public void setSpeed(double speedAlien) {
        this.speed = speedAlien;
        //The old speed on the alien.
        this.oldSpeed = speedAlien;
    }

    /**.
     * setEnvironment.
     * Setting a the alien's game environment.
     *
     * @param environment the gameEnvironment we want the bullet will be in
     */
    public void setEnvironment(GameEnvironment environment) {
        this.gameEnvironment = environment;
    }
    /**.
     * setGame.
     * Setting a the alien's game level.
     *
     * @param gameLevel the game we want the bullet will be in.
     */
    public void setGame(GameLevel gameLevel) {
        this.game = gameLevel;
    }
    /**.
     * timePassed.
     * notify the sprite that time has passed.
     * @param dt do nothing on this class.
     */
    public void timePassed(double dt) {
        return;
    }
    /**.
     * shoot.
     * Makes the alien to shoot a bullet.
     */
    public void shoot() {
        //The position of the bullet.
            Point pos = new Line(this.getCollisionRectangle().getLowerLeft()
                    , this.getCollisionRectangle().getLowerRight()).middle(); //The miidle of bottom of the alien.
            pos.setY(pos.getY() + 5);
            //Creating the bullet in the middle of the bottom of the alien.
            Ball bullet = new Ball(pos, 5, Color.red);
            //Setting the bullet velocity to be align to y axis.
            bullet.setVelocity(Velocity.fromAngleAndSpeed(0, 300));
            bullet.setGameEnvironment(this.gameEnvironment);
            bullet.addToGame(this.game);
            //Adding the bullet to the list of current bullets on the screen.
            this.bullets.add(bullet);
    }
    /**
     * copy.
     * Copy the block.
     * @return block same as our block.
     */
    @Override
    public Alien copy() {
        Alien a = new Alien(super.copy().getCollisionRectangle(), super.getImage());
        return a;
    }

    /**.
     * moveLeft.
     * Moving the alien left.
     * We will change the upper left point's x- cordinate to move it left.
     *
     * @param leftest the leftest alien in the aliens. (The one with smaller x cordinate).
     */
    private void moveLeft(Alien leftest) {
        if (!moveRight) {
            //Where the new rectangle should be
            double newX = this.getCollisionRectangle().getUpperLeft().getX() - this.speed;
            //Check if it is passing the margins of the screen.
            if (leftest.getCollisionRectangle().getUpperLeft().getX() - this.speed <= SCREENMARGIN) {
                newX = this.getCollisionRectangle().getUpperLeft().getX() - this.speed;
                newX -= SCREENMARGIN - leftest.getCollisionRectangle().getUpperLeft().getX();
                //Fixing problems if we have aliens than the distance between them is less than the space definition(15)
                if (newX - leftest.getCollisionRectangle().getUpperLeft().getX() < 15) {
                    newX = leftest.getCollisionRectangle().getUpperLeft().getX();
                }
                this.moveRight = true;
                this.down = true;
            }
            //Updating the position of the block.
            this.setPosition(newX, this.getCollisionRectangle().getUpperLeft().getY());
       }
    }

    /**.
     * moveRight.
     * Moving the alien right.
     * We will change the upper left point's x- cordinate to move it right.
     *
     * @param rightest the leftest alien in the aliens. (The one with biggest x cordinate).
     */
    private void moveRight(Alien rightest) {
        if (moveRight) {
            //Where the new rectangle should be
            double newX = this.getCollisionRectangle().getUpperLeft().getX() + this.speed;

            //Check if it is passing the margins of the screen.
            if (rightest.getCollisionRectangle().getUpperRight().getX() + this.speed >= GUIWIDTH - SCREENMARGIN) {
                newX = this.getCollisionRectangle().getUpperLeft().getX() + this.speed;
                //If alien is on the first line of aliens.
                boolean firstLine = this.getCollisionRectangle().getUpperLeft().getY()
                        == rightest.getCollisionRectangle().getUpperLeft().getY();
                //Prevent problems with distance between aliens.
                if (rightest.getCollisionRectangle().getUpperLeft().getX() - newX < 15 || firstLine) {
                    newX -= GUIWIDTH - SCREENMARGIN - rightest.getCollisionRectangle().getUpperRight().getX();
                }
                //Fixing problems if we have aliens than the distance between them is less than the space definition.
                if (rightest.getCollisionRectangle().getUpperLeft().getX() - newX < 15) {
                    newX = rightest.getCollisionRectangle().getUpperLeft().getX();
                }
                this.moveRight = false;
                this.down = true;
            }
            //Updating the position of the block.
            this.setPosition(newX, this.getCollisionRectangle().getUpperLeft().getY());
        }
    }
    /**.
     * moveDown.
     * Moving the alien down.
     * We will change the upper left point's y- cordinate to move it down.
     *
     * @param highestBlock the y cordinate of the highest block in the shield.
     * @param aliens a list of aliens.
     * @return true if we can continue, false otherwise (the lowest alien got to the highestBlock y cordinate).
     */
    private boolean moveDown(double highestBlock, List<Alien> aliens) {
        double newY = this.getCollisionRectangle().getUpperLeft().getY() + 30;
        this.setPosition(this.getCollisionRectangle().getUpperLeft().getX(), newY);
        //Increasing the speed by 10%;
        this.speed *= 1.1;
        //The lower aliens in the list.
        List<Alien> lowestAliens = Alien.downAliens(aliens);
        //Moving on the lower aliens to check if there is alien got there.
        for (Alien alien: lowestAliens) {
            //If one of the aliens in the bottom gets there.
            if (alien.getCollisionRectangle().getUpperLeft().getY() >= highestBlock
                    - this.getCollisionRectangle().getHeight()) {
                return false;
            }
        }
        return true;

    }
    /**.
     * downAliens.
     *
     * @param alienList the list of aliens.
     * @return list of aliens down of the line.
     */
    public static List downAliens(List<Alien> alienList) {
        //Initializing the returning list.
        List<Alien> downAliens = new ArrayList<Alien>();
        //Moving on the aliens.
        for (Alien alien: alienList) {
            //Getting the lowest alien in the row.
            Alien lower = Alien.lowestAlienRow(Alien.aliensRow(alien, alienList));
            //The list does not contain this alien.
            if (!downAliens.contains(lower)) {
                downAliens.add(lower);
            }

        }
        return downAliens;
    }
    /**.
     * aliensRow.
     *
     * @param alien an alien.
     * @param alienList a list contains the alien.
     * @return list of the row of the aliens that contains the alien.
     */
    private static List<Alien> aliensRow(Alien alien, List<Alien> alienList) {
        List<Alien> rowAliens = new ArrayList<Alien>();
        for (Alien a: alienList) {
            if (a.getCollisionRectangle().getUpperLeft().getX() == alien.getCollisionRectangle().getUpperLeft().getX()
                    || Math.abs(a.getCollisionRectangle().getUpperLeft().getX() //Case on delay between aliens.
                    - alien.getCollisionRectangle().getUpperLeft().getX()) < 15) {
                //Adding the alien in the same line.
                rowAliens.add(a);
            }
        }
        return rowAliens;
    }
    /**.
     * lowestAlienRow.
     *
     * @param rowAliens list of aliens in a row.
     * @return the lowest alien in the row.
     */
    public static Alien lowestAlienRow(List<Alien> rowAliens) {
        double y = 0;
        Alien lowestAlien = null;
        //Moving on the aliens.
        for (Alien a: rowAliens) {
            //If there is lowest alien than the current lowest alien.
            if (a.getCollisionRectangle().getUpperLeft().getY() > y) {
                lowestAlien = a;
                //Updating the y cordinate of the alien.
                y = a.getCollisionRectangle().getUpperLeft().getY();
            }
        }
        return lowestAlien;
    }
    /**.
     * getLeftestAlien.
     *
     * @param aliens a list of aliens organized in the game structure.
     * @return the leftest alien in the list (the one with smallest x-cordinate).
     */
    public static Alien getLeftestAlien(List<Alien> aliens) {
        //Each alien's x cordianate is smaller than the width of the gui.
        double x = GUIWIDTH;
        Alien leftest = aliens.get(0);
        for (Alien alien: aliens) {
            double currentX = alien.getCollisionRectangle().getUpperLeft().getX();
            //The current is leftest than the forward.
            if (currentX < x) {
                x = currentX;
                leftest = alien;
            }

        }
        return leftest;
    }
    /**.
     * getRightest.
     *
     * @param aliens a list of aliens organized in the game structure.
     * @return the rightest alien in the list (the one with biggest x-cordinate).
     */
    public static Alien getRightest(List<Alien> aliens) {
        //The leftest alien is the alien with the smallest x cordinate.
        double x = 0;
        Alien rightest = aliens.get(0);
        for (Alien alien: aliens) {
            double currentX = alien.getCollisionRectangle().getUpperLeft().getX();
            //The current is righter than the forward.
            if (currentX > x) {
                x = currentX;
                rightest = alien;
            }
        }
        return rightest;
    }
    /**.
     * move.
     * Moving the aliens first right if hits the edge go down and than left then the same with right until getting to
     * height of the shield.
     *
     * @param aliens a list of aliens.
     * @param highestBlock the y value of the highest block.
     * @return true if the player didn't got to the height of the shield, false otherwise.
     */
    public boolean move(List<Alien> aliens, double highestBlock) {
        //Can moove down.
        if (down) {
            this.down = false;
            if (!moveDown(highestBlock, aliens)) {
                return false;
            }
        } else {
            //Can move right
            if (moveRight) {
                this.moveRight(Alien.getRightest(aliens));
            } else { //Move left.
                this.moveLeft(Alien.getLeftestAlien(aliens));

            }
        }

        return true;
    }
    /**.
     * reset.
     * When losing a life the alien will back to his place and we will remove the bullets shooted from the alien.
     */
    public void reset() {
        this.setPosition(this.oldPosition.getX(), this.oldPosition.getY());
        this.speed = oldSpeed;
        //Reset the booleans.
        this.moveRight = true;
        this.down = false;
        //Clearing the boolets that the alien shooted.
        for (Ball bullet: bullets) {
            //remove the current bullets from the game.
            bullet.removeFromGame(this.game);
        }
        this.bullets.clear();
    }
    /**.
     * isAlien.
     * @return true because it is an alien.
     */
    @Override
    public boolean isAlien() {
        return true;
    }
}



