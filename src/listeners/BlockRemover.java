package listeners;

import creators.ColorImageParser;
import game.Ball;
import game.Block;
import game.GameLevel;
import game.Paddle;
import invaders.Alien;

import java.util.List;

/**.
 * BlockRemover.
 *
 * A BlockRemover is in charge of removing blocks from the gameLevel.
 * As well as keeping count of the number of blocks that remain.
 */
public class BlockRemover implements HitListener {
    private GameLevel gameLevel;
    private Counter remainingBlocks;
    private List<Alien> aliens;
    private List<Block> blocks;
    /**.
     * Creates new instance of BlockRemover.
     *
     * The constructor of our class.
     * @param gameLevel the gameLevel where the blocks are.
     * @param removedBlocks counter of the number of blocks that remain.
     */
    public BlockRemover(GameLevel gameLevel, Counter removedBlocks) {
        this.gameLevel = gameLevel;
        //The blocks we want to remove.
        this.remainingBlocks = removedBlocks;

    }
    /**.
     * setAliens.
     *
     * Giving a parameter of alien list to remove the alien from the list after being removed.
     * @param alienList the alien list.
     */
    public void setAliens(List<Alien> alienList) {
        //We assume that the list contain aliens.
        this.aliens = alienList;
    }

    /**.
     * setBlocks.
     *
     * Giving a parameter of alien list to remove the block from the list after being removed.
     * @param blockList the alien list.
     */
    public void setBlocks(List<Block> blockList) {
        //We assume that the list contain blocks.
        this.blocks = blockList;
    }
    /**.
     * hitEvent
     *  Blocks that are hit and reach 0 hit-points should be removed from the gameLevel.
     *  Remember to remove this listener from the block that is being removed from the gameLevel.
     *
     * @param beingHit the block we hitted.
     * @param hitter the ball hitted the block.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        //The bullet of aliens is red.
        //We make that alien can't hit other aliens.
        if (hitter.getColor().getRGB() != ColorImageParser.colorFromString("red").getRGB() || !beingHit.isAlien()) {
            //We have one hit points until remove or 0 (in case of aliens hitting aliens)
            if (beingHit.getHitPoints() <= 1) {
                //Remove the block from the gameLevel.
                beingHit.removeFromGame(this.gameLevel);
                if (this.aliens != null && beingHit.isAlien()) {
                    //Remove the block from the list.
                    this.aliens.remove(beingHit);
                }

                if (this.blocks != null && !beingHit.isAlien()) {
                    //Remove the block from the list.
                    this.blocks.remove(beingHit);
                }
                //Remove this listener from the block that is being removed from the gameLevel.
                beingHit.removeHitListener(this);
                //Updating the remaining blocks.
                this.remainingBlocks.decrease(1);
            }

        }
    }

    /**
     * hitEvent.
     * Do nothing on this class because we don't have paddle.
     *
     * @param beingHit the paddle hitted.
     * @param hitter the Ball that's doing the hitting.
     */
    public void hitEvent(Paddle beingHit, Ball hitter) {
        return;
    }
}
