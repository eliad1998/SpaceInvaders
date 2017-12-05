package listeners;
import game.Ball;
import game.Block;
import game.Paddle;

/**.
 * ScoreTrackingListener.
 *
 * Update this counter when blocks are being hit and removed.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;
    /**.
     * Creates new instance of ScoreTrackingListener.
     * The constructor of our class.
     *
     * @param scoreCounter a reference to the counter to the scores in the game.
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }
    /**
     * hitEvent.
     * This method is called whenever the beingHit object is hit.
     *
     * @param beingHit the block hitted.
     * @param hitter the Ball that's doing the hitting.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        //Destroying alien means add more 100 points.
        this.currentScore.increase(100);
            //Removing the score hit listener from that block that removed.
        beingHit.removeHitListener(this);
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
