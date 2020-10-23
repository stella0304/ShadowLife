import java.util.HashMap;
import java.util.Map;

/**
 * Class for both Stockpile and Hoard
 */
public class FruitStock extends Actor implements ContentDrawable{

    private final static Map<String, String> IMG_LOCATIONS = new HashMap<>() {{
        put("Stockpile", "res/images/cherries.png");
        put("Hoard", "res/images/hoard.png");
    }};

    private int numFruit = 0;

    /**
     * Makes a new FruitStock object
     * @param xCoord x position of the FruitStock
     * @param yCoord y position of the FruitStock
     * @param actorType whether the new FruitStock is a Stockpile or a Hoard
     */
    public FruitStock(int xCoord, int yCoord, String actorType) {
        super(xCoord, yCoord, IMG_LOCATIONS.get(actorType), actorType);
    }

    /**
     * Getter for numFruit
     * @return int the current amount of fruit on the FruitStock
     */
    public int getNumFruit() {
        return numFruit;
    }

    /**
     * adds a fruit to the FruitStock
     */
    public void addFruit() {
        numFruit++;
    }

    /**
     * Tries to remove a fruit from the FruitStock
     * @return boolean, true if successfully taken fruit, false otherwise
     */
    public boolean takeFruit() {
        if (numFruit <= 0) {
            return false;
        } else {
            numFruit--;
            return true;
        }
    }

    /**
     * from the ContentDrawable interface, draws the amount of fruit on the StockPile on the ShadowLife simulation
     */
    public void drawContent() {
        FONT.drawString(Integer.toString(numFruit), this.getxCoord(), this.getyCoord());
    }

}
