import java.util.ArrayList;
import java.util.HashMap;

public class Gatherer extends Carrier{
    private static final String IMAGE_LOCATION = "res/images/gatherer.png";
    private static final String TYPE = "Gatherer";
    private static final int DEFAULT_DIRECTION = 180;
    // directions: 0 = right, 90 = down, 180 = left, 270 = up, etc

    public Gatherer(int xCoord, int yCoord) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, DEFAULT_DIRECTION);
    }

    public Gatherer(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, direction);
    }

    public void atTree(Tree oneTree) {
        if (carrierAtTree(oneTree)) {
            rotate180();
        }
    }

    public void atGoldenTree() {
        if(carrierAtGoldenTree()) {
            rotate180();
        }
    }

    public void atMitosisPool(ArrayList<Actor> carriersToAdd, ArrayList<Actor> carriersToDelete) {

        // make and move new gatherers
        int clockwise90 = 90, anticlockwise90 = -90;

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
        rotate180();
    }

    public void atHoard(FruitStock oneHoard) {
        atFruitStock(oneHoard);
    }

    public void atStockPile(FruitStock oneStockPile) {
        atFruitStock(oneStockPile);
    }

    public void atPad() { /* do nothing*/ }

    public void onGatherer() { /* do nothing */ }

    /*public void checkForNonMoving(HashMap<String, ArrayList<Actor>> nonMovingActors,
                                  HashMap<String, Gatherer> gathererMap, ArrayList<Actor> actorsToAdd,
                                  ArrayList<Actor> actorsToDelete) {
        String key = getxCoord() + "," + getyCoord();
        ArrayList<Actor> onTile = nonMovingActors.get(key);
        Actor interestedActor;

        if (onTile == null || onTile.size() < 1) {
            return;
        }

        if ((interestedActor=findActorWithType(onTile, "Fence")) != null) {
            atFence();
        }
        if ((interestedActor=findActorWithType(onTile, "MitosisPool")) != null) {
            atMitosisPool();
        }
        if ((interestedActor=findActorWithType(onTile, "SignLeft")) != null) {
            atSign("LEFT");
        }

    }*/

}
