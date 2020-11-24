import java.util.ArrayList;

public class Thief extends Carrier {
    private static final String IMAGE_LOCATION = "res/images/thief.png";
    private static final String TYPE = "Thief";
    private static final int DEFAULT_DIRECTION = 270;
    // directions: 0 = left, 90 = up, 180 = right, 270 = down, etc

    private boolean isConsuming = false;

    /**
     * Makes a new Thief using the default direction
     * @param xCoord x location of the Thief
     * @param yCoord y location of the Thief
     */
    public Thief(int xCoord, int yCoord) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, DEFAULT_DIRECTION);
    }

    /**
     * Make a new Thief without using the default direction
     * @param xCoord x location of the Thief
     * @param yCoord y location of the Thief
     * @param direction starting direction of the Thief
     */

    public Thief(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, direction);
    }

    protected void atTree(Tree oneTree) {
        carrierAtTree(oneTree);
    }

    protected void atGoldenTree() {
        carrierAtGoldenTree();
    }

    protected void atHoard(FruitStock oneHoard) {
        if (isConsuming) {
            isConsuming = false;
            if (!isCarrying()) {
                if (oneHoard.takeFruit()) {
                    setCarrying(true);
                } else {
                    rotate90Clockwise();
                }
            }
        } else if (isCarrying()) {
            setCarrying(false);
            oneHoard.addFruit();
            rotate90Clockwise();
        }
    }

    protected void atStockPile(FruitStock oneStockpile) {
        if (!isCarrying()) {
            if (oneStockpile.takeFruit()) {
                setCarrying(true);
                isConsuming = false;
                rotate90Clockwise();
            }
        } else {
            rotate90Clockwise();
        }
    }

    protected void atPad() {
        isConsuming = true;
    }

    protected void onGatherer() {
        rotate90AntiClockwise();
    }

    protected Carrier createCarrier(int xCoord, int yCoord, int direction) {
        return new Thief(xCoord, yCoord, direction);
    }

}
