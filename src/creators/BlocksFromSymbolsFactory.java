package creators;
import game.Block;
import invaders.Alien;
import invaders.AlienFactory;

import java.util.Map;
import java.util.TreeMap;

/**.
 * The block definition files define a mapping from symbols to spaces and blocks.
 * These symbols are then used in the level specification files to define the blocks that need to be created.
 * You will thus need a mechanism (object) with a method that will get a symbol and create the desired block.
 */
public class BlocksFromSymbolsFactory {
    private Map<String, Integer> spacerWidths;
    private Map<String, BlockFactory> blockCreators;
    private Map<String, AlienFactory> alienCreators;
    /**.
     * Creates new instance of BlocksFromSymbolsFactory.
     * The constructor of our class.
     */
    public BlocksFromSymbolsFactory() {
        //Initializing the maps.
        this.spacerWidths = new TreeMap<String, Integer>();
        this.blockCreators = new TreeMap<String, BlockFactory>();
        this.alienCreators = new TreeMap<String, AlienFactory>();
    }
    /**.
     * addSpacer.
     * Adding spacer to the spacers map.
     *
     * @param spacer a spacer.
     * @param spacerWidth the width of the spacer.
     */
    public void addSpacer(String spacer, int spacerWidth) {
        this.spacerWidths.put(spacer, spacerWidth);
    }
    /**.
     * addBlockCreator.
     * Adding blcok creator to the block creator map by the defined key of the block.
     *
     * @param key the key of the block in that form.
     * @param creator a block creator.
     */
    public void addBlockCreator(String key, BlockFactory creator) {
        this.blockCreators.put(key, creator);
    }
    /**.
     * addAlienCreator.
     * Adding blcok creator to the block creator map by the defined key of the block.
     *
     * @param key the key of the block in that form.
     * @param creator an alien creator.
     */
    public void addAlienCreator(String key, AlienFactory creator) {
        this.alienCreators.put(key, creator);
    }
    /**.
     * isSpaceSymbol.
     *
     * @param s a string.
     * @return true if 's' is a valid space symbol.
     */
    public boolean isSpaceSymbol(String s) {
        //Moving on all the keys in space widths.
        for (String key : spacerWidths.keySet()) {
            if (key.equals(s)) {
                return true;
            }
        }
        return false;
    }
    /**.
     * isSpaceSymbol.
     *
     * @param s a string.
     * @return true if 's' is a valid space symbol.
     */
    public boolean isBlockSymbol(String s) {
        //Moving on all the keys in block creators.
        for (String key : blockCreators.keySet()) {
            if (key.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**.
     * isAlienSymbol.
     *
     * @param s a string.
     * @return true if 's' is a valid alien symbol.
     */
    public boolean isAlienSymbol(String s) {
        //Moving on all the keys in block creators.
        for (String key : alienCreators.keySet()) {
            if (key.equals(s)) {
                return true;
            }
        }
        return false;
    }
    /**.
     * getSpaceWidth.
     *
     * @param s a spacer-symbol.
     * @return the width in pixels associated with the given spacer-symbol.
     */
    public int getSpaceWidth(String s) {
        return this.spacerWidths.get(s);
    }

    /**.
     * getBlock.
     *
     * @param s the key of the block.
     * @param x the x position of the block.
     * @param y the y position of the block.
     * @return block according to the definitions associated with symbol s.
     * The block will be located at position (xpos, ypos).
     */
    public Block getBlock(String s, int x, int y) {
        return this.blockCreators.get(s).create(x, y);
    }

    /**.
     * getAlien.
     *
     * @param s the key of the alien.
     * @param x the x position of the alien.
     * @param y the y position of the alien.
     * @return alien according to the definitions associated with symbol s.
     * The alien will be located at position (xpos, ypos).
     */
    public Alien getAlien(String s, int x, int y) {
        return this.alienCreators.get(s).create(x, y);
    }



}