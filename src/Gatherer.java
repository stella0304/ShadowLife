import java.util.ArrayList;

public class Gatherer extends Carrier{
    private static final String IMAGE_LOCATION = "res/images/gatherer.png";
    private static final String TYPE = "Gatherer";
    private static final int DEFAULT_DIRECTION = 0;
    // directions: 0 = left, 90 = up, 180 = right, 270 = down, etc

    public Gatherer(int xCoord, int yCoord) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, DEFAULT_DIRECTION);
    }

    public Gatherer(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, direction);
    }

    public void atMitosisPool(ArrayList<Actor> carriersToAdd, ArrayList<Actor> carriersToDelete) {

        // make and move new gatherers
        int clockwise90 = -90, anticlockwise90 = 90;

        Gatherer newGatherer1 = new Gatherer(getxCoord(), getyCoord(), getDirection() + anticlockwise90);
        newGatherer1.move();
        Gatherer newGatherer2 = new Gatherer(getxCoord(), getyCoord(), getDirection() + clockwise90);
        newGatherer2.move();
        //Actor newActor1 = newGatherer1;
        //Actor newActor2 = newGatherer2;

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
    }

    public void atHoard(FruitStock oneHoard) {
        atFruitStock(oneHoard);
    }

    public void atStockPile(FruitStock oneStockPile) {
        atFruitStock(oneStockPile);
    }

}
