package levels;

import game.Block;
import geometry.Point;
import geometry.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Level.
 * Contains functions we want to use them in each level class.
 */
public abstract class Level {
    //Final variables of the screen size.
    public static final  int GUIWIDTH = 800;
    public static final int GUIHEIGHT = 600;
    //The margin of screen where the game will be. (The blocks of screen width).
    static final int SCREENMARGIN = 25;

    /**.
     * gameRectangle.
     * We notify the screen margins as 4 blocks.
     * @return the rectangle of the inner screen where the game is.
     */

     public static Rectangle gameRectangle() {
        return new Rectangle(new Point(SCREENMARGIN, SCREENMARGIN), GUIWIDTH - 2 * SCREENMARGIN
                , GUIHEIGHT - SCREENMARGIN);
    }

    /**.
     * numberOfBalls.
     * @return the number of balls in this level.
     */
    public abstract int numberOfBalls();
    /**.
     * getSpeed.
     * @return the speed of the balls in this class.
     */
    protected abstract double getSpeed();

    /**.
     * createLineOfBlocks.
     * Creating lines of number of blocks.
     * @param  number the number of blocks in the line.
     * @param  firstBlock the point where the first block will be than the others will be left to it.
     * @param colors an array of the colors of the block in the line.
     * @param blockWidth the width of the blocks.
     * @param blockHeight the height of the blocks.
     * @param countHits the number of hits to destroy the block.
     *
     * @return list with the block we just created.
     */
    protected  List<Block> createLineOfBlocks(int number, Point firstBlock, Color [] colors, double blockWidth
            , double blockHeight, int countHits) {
        List<Block> blocks = new ArrayList<Block>();
        //Create blocks from left to right.
        for (int i = 0; i < number; i++) {
            //Creating each block next to the other in the line by adding to x cordinate each time the block width.
            Point upperLeft = new Point(firstBlock.getX() + i * blockWidth, firstBlock.getY());
            //Creating the block and adding it into the game.
            Block block = new Block(new Rectangle(upperLeft, blockWidth, blockHeight), colors[i]);
            block.setCountHits(countHits);
            blocks.add(block);
        }
        return blocks;
    }

    /**.
     * createLineOfBlocks.
     * Creating lines of number of blocks.
     * @param  number the number of blocks in the line.
     * @param  firstBlock the point where the first block will be than the others will be left to it.
     * @param color the color of the blocks in the line.
     * @param blockWidth the width of the blocks.
     * @param blockHeight the height of the blocks.
     * @param countHits the number of hits to destroy the block.
     *
     * @return list with the block we just created.
     */
    protected  List<Block> createLineOfBlocks(int number, Point firstBlock, Color color
            , double blockWidth, double blockHeight
            , int countHits) {
        List<Block> blocks = new ArrayList<Block>();
        //Create blocks from left to right.
        for (int i = 0; i < number; i++) {
            //Creating each block next to the other in the line by adding to x cordinate each time the block width.
            Point upperLeft = new Point(firstBlock.getX() + i * blockWidth, firstBlock.getY());
            //Creating the block and adding it into the game.
            Block block = new Block(new Rectangle(upperLeft, blockWidth, blockHeight), color);
            block.setCountHits(countHits);
            blocks.add(block);
        }
        return blocks;
    }


}
