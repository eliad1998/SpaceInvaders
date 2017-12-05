package levels;

import game.Block;
import game.Sprite;
import invaders.Alien;

import java.util.List;
/**.
 * LevelInformation.
 *
 * The LevelInformation interface specifies the information required to fully describe a level.
 */
public interface LevelInformation {
    /**.
     * paddleSpeed.
     * @return the speed of the paddle.
     */
    int paddleSpeed();
    /**.
     * paddleWidth.
     * @return the width of the paddle.
     */
    int paddleWidth();
    /**.
     * levelName.
     * The level name will be displayed at the top of the screen.
     * @return level name.
     */
    String levelName();
    /**.
     * getBackground.
     * @return a sprite with the background of the level.
     */
    Sprite getBackground();
    /**.
     * blocks.
     * The Blocks that make up this level, each block contains its size, color and location.
     * @return a list of blocks of this level.
     */
    List<Block> blocks();
    /**.
     * aliens.
     * The aliens that make up this level, each block contains its size, color and location.
     * @return a list of aliens of this level.
     */
    List<Alien> aliens();
    /**.
     * numberOfAliensToRemove.
     * This number should be <= aliens.size();
     * @return the number of aliens that should be removed
     */
    int numberOfAliensToRemove();
}
