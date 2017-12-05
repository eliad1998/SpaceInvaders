package invaders;

import creators.BlockCreator;
import geometry.Point;
import geometry.Rectangle;

import java.awt.Image;


/**
 * AlienFactory.
 * Creating aliens - because all the aliens are the same.
 */
public class AlienFactory implements BlockCreator {
    private int height;
    private int width;
    private Image image;
    /**.
     * Creates new instance of AlienFactory
     * The constructor of our class.
     * @param height height of the alien.
     * @param width the width of the alien.
     * @param image the background image of the alien.
     */
    public AlienFactory(int height, int width, Image image) {
        this.height = height;
        this.width = width;
        this.image = image;
    }
    /**.
     * create.
     * Create an Alien at the specified location.
     *
     * @param x the x position of the top left point.
     * @param y the y position of the top left point.
     * @return the block we just created.
     */
    public Alien create(int x, int y) {
        return new Alien(new Rectangle(new Point(x, y), this.width, this.height), this.image);
    }


}
