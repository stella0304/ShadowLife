import java.util.ArrayList;

public class Gatherer extends Carrier{
    private static final String IMAGE_LOCATION = "res/images/gatherer.png";
    private static final String TYPE = "Gatherer";
    private static final int DEFAULT_DIRECTION = 180;
    // directions: 0 = right, 90 = down, 180 = left, 270 = up, etc

    /**
     * Makes a new Gatherer using the default direction
     * @param xCoord x location of the Gatherer
     * @param yCoord y location of the Gatherer
     */
    public Gatherer(int xCoord, int yCoord) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, DEFAULT_DIRECTION);
    }

    /**
     * Make a new Gatherer without using the default direction
     * @param xCoord x location of the Gatherer
     * @param yCoord y location of the Gatherer
     * @param direction starting direction of the Gatherer
     */
    public Gatherer(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, direction);
    }

    protected void atTree(Tree oneTree) {
        if (carrierAtTree(oneTree)) {
            rotate180();
        }
    }

    protected void atGoldenTree() {
        if(carrierAtGoldenTree()) {
            rotate180();
        }
    }

    protected void atMitosisPool(ArrayList<Actor> carriersToAdd, ArrayList<Actor> carriersToDelete) {

        // make and move new gatherers
        int clockwise90 = 90, anticlockwise90 = -90;

        Gatherer newGatherer1 = new Gatherer(getxCoord(), getyCoord(), getDirection() + anticlockwise90);
        newGatherer1.move();
        Gatherer newGatherer2 = new Gatherer(getxCoord(), getyCoord(), getDirection() + clockwise90);
        newGatherer2.move();

        // add to list of actors to add and delete
        carriersToAdd.add(newGatherer1);
        carriersToAdd.add(newGatherer2);
        carriersToDelete.add(this);
    }

    private void atFruitStock(FruitStock oneStock) {
        if (isCarrying()) {
            setCarrying(false);
            oneStock.addFruit();
        }
        rotate180();
    }

    protected void atHoard(FruitStock oneHoard) {
        atFruitStock(oneHoard);
    }

    protected void atStockPile(FruitStock oneStockPile) {
        atFruitStock(oneStockPile);
    }

    protected void atPad() { /* do nothing*/ }

    protected void onGatherer() { /* do nothing */ }

}
