import java.util.ArrayList;
import java.util.HashMap;

public class Thief extends Carrier {
    private static final String IMAGE_LOCATION = "res/images/thief.png";
    private static final String TYPE = "Thief";
    private static final int DEFAULT_DIRECTION = 90;
    // directions: 0 = left, 90 = up, 180 = right, 270 = down, etc

    private boolean isConsuming = false;

    public Thief(int xCoord, int yCoord) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, DEFAULT_DIRECTION);
    }

    public Thief(int xCoord, int yCoord, int direction) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE, direction);
    }

    public void atMitosisPool(ArrayList<Actor> carriersToAdd, ArrayList<Actor> carriersToDelete) {

        // make and move new thieves
        int clockwise90 = -90, anticlockwise90 = 90;

        Thief newThief1 = new Thief(getxCoord(), getyCoord(), getDirection() + anticlockwise90);
        newThief1.move();
        Thief newThief2 = new Thief(getxCoord(), getyCoord(), getDirection() + clockwise90);
        newThief2.move();
        //Actor newActor1 = newThief1;
        //Actor newActor2 = newThief2;

        // add to list of actors to add and delete
        carriersToAdd.add(newThief1);
        carriersToAdd.add(newThief2);
        carriersToDelete.add(this);
    }

    public void atHoard(FruitStock oneHoard) {
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

    public void atStockPile(FruitStock oneStockpile) {
        if (!isCarrying()) {
            if (oneStockpile.takeFruit()) {
                setCarrying(true);
                isConsuming = false;
                oneStockpile.takeFruit();
                rotate90Clockwise();
            }
        } else {
            rotate90Clockwise();
        }
    }

    public void atPad() {
        isConsuming = true;
    }

    public void onGatherer() {
        rotate90AntiClockwise();
    }

    public void checkForNonMoving(HashMap<String, Actor> nonMovingActors, ArrayList<Actor> actorsToAdd, ArrayList<Actor> actorsToDelete) {
        String key = getxCoord() + "," + getyCoord();
        Actor onTile = nonMovingActors.get(key);
        if (onTile == null) {
            return;
        } else if (onTile.getActorType().equals("Pad"))  {
            // check if thief is on pad
            this.atPad();
        } else {
            super.atActor(onTile, actorsToAdd, actorsToDelete);
        }

    }

    public void checkForGatherer(HashMap<String, Gatherer> gatherersMap) {
        String key = getxCoord() + "," + getyCoord();
        Gatherer onTile = gatherersMap.get(key);
        if (onTile != null) {
            onGatherer();
        }
    }

}
