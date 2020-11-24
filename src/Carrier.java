import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/** Parent class for all actors that can carry fruit
 * include all shared properties of
 */
public abstract class Carrier extends Actor {

    // map of the direction and their corresponding angles
    private final static Map<String, Integer> DIRECTIONS = new HashMap<>() {{
        put("LEFT", 180);
        put("UP", 270);
        put("RIGHT", 0);
        put("DOWN", 90);
    }};

    private final static double degToRadRatio = Math.PI / 180.0;
    protected final static int TILE = 64;

    private boolean isActive = true;
    private boolean isCarrying = false;
    private int direction;

    /** Makes one carrier
     * @param xCoord x location of the carrier
     * @param yCoord y location of the carrier
     * @param imgLocation the file location of the image
     * @param actorType the type of the carrier
     * @param direction direction the carrier when it is created
     */
    public Carrier(int xCoord, int yCoord, String imgLocation, String actorType, int direction) {
        super(xCoord, yCoord, imgLocation, actorType);
        this.direction = direction;
    }

    /** Getter for isActive
     * @return boolean whether the actor is still active
     */
    public boolean isActive() {
        return isActive;
    }

    /** Getter for isCarrying
     * @return boolean whether the actor is carrying fruit
     */
    public boolean isCarrying() {
        return isCarrying;
    }

    /** Setter for isCarrying
     * @param carrying the new status of isCarrying
     */
    public void setCarrying(boolean carrying) {
        isCarrying = carrying;
    }

    /** Getter for direction
     * @return int the angle of the carrier's direction with respect to the positive x axis
     */
    public int getDirection() {
        return direction;
    }

    /** moves the carrier one tile in its direction
     */
    public void move() {
        double directionRad = direction * degToRadRatio;
        setxCoord(getxCoord() + TILE * ((int) Math.round(Math.cos(directionRad))));
        setyCoord(getyCoord() + TILE * ((int) Math.round(Math.sin(directionRad))));
    }

    protected void moveBack() {
        double directionRad = direction * degToRadRatio;
        setxCoord(getxCoord() - TILE * ((int) Math.round(Math.cos(directionRad))));
        setyCoord(getyCoord() - TILE * ((int) Math.round(Math.sin(directionRad))));
    }
    
    protected void rotate90Clockwise() {
        // rotate 90 degrees clockwise
        int angleChange = 90;
        direction += angleChange;
    }

    protected void rotate90AntiClockwise() {
        // rotate 90 degrees clockwise
        int angleChange = -90;
        direction += angleChange;
    }

    protected void rotate180() {
        // rotate 180 degrees
        int angleChange  = 180;
        direction += angleChange;
    }

    protected void atFence() {
        isActive = false;
        moveBack();
    }

    protected void atSign(String signDirection) {
        direction = DIRECTIONS.get(signDirection);
    }

    protected boolean carrierAtTree(Tree oneTree) {
        if (!isCarrying) {
            if (oneTree.takeFruit()) {
                isCarrying = true;
                return true;
            }
        }
        return false;
    }

    protected boolean carrierAtGoldenTree() {
        if (!isCarrying) {
            // carry without having to decrease fruit amount
            isCarrying = true;
            return true;
        }
        return false;
    }

    private static Actor findActorWithType(ArrayList<Actor> actors, String type) {
        if (actors == null) {
            return null;
        }
        for (Actor a : actors) {
            if (a.getActorType().equals(type)) {
                return a;
            }
        }
        return null;
    }

    protected static Actor findNonEmptyTree(ArrayList<Actor> actors) {
        if (actors == null) {
            return null;
        }
        for (Actor a : actors) {
            if (a.getActorType().equals("Tree")) {
                Tree thisTree = (Tree) a;
                if (((Tree) a).getNumFruit() > 0) {
                    return a;
                }
            }
        }
        return null;
    }

    /**
     * Checks if the carrier is currently standing on any actors of interest
     * @param nonMovingActors hash map of all the actors that does not move
     * @param gathererMap hash map of all the gatherers
     * @param actorsToAdd list of all the new actors created
     * @param actorsToDelete list of all the actors to be deleted
     */
    public void checkForNonMoving(HashMap<String, ArrayList<Actor>> nonMovingActors,
                                  HashMap<String, Gatherer> gathererMap, ArrayList<Actor> actorsToAdd,
                                  ArrayList<Actor> actorsToDelete) {
        String key = getxCoord() + "," + getyCoord();
        ArrayList<Actor> onTile = nonMovingActors.get(key);
        Actor interestedActor;

        // if no code needs to be run
        if ((this.getActorType().equals("Gatherer") && (onTile == null || onTile.size() < 1)) ||
                (this.getActorType().equals("Thief") && (onTile == null || onTile.size() < 1) &&
                !gathererMap.containsKey(key))) {
            return;
        }

        // carrier at fence or mitosis pool
        if (findActorWithType(onTile, "Fence") != null) {
            atFence();
        }
        if (findActorWithType(onTile, "MitosisPool") != null) {
            atMitosisPool(actorsToAdd, actorsToDelete);
        }

        // carrier at a sign
        if (findActorWithType(onTile, "SignLeft") != null) {
            atSign("LEFT");
        } else if (findActorWithType(onTile, "SignRight") != null) {
            atSign("RIGHT");
        } else if (findActorWithType(onTile, "SignUp") != null) {
            atSign("UP");
        } else if (findActorWithType(onTile, "SignDown") != null) {
            atSign("DOWN");
        }

        // when the carrier is a thief, actions only performed by thieves
        if (this.getActorType().equals("Thief")) {
            if (findActorWithType(onTile, "Pad") != null) {
                atPad();
            }
            if (gathererMap.containsKey(key)) {
                onGatherer();
            }
        }

        // carrier at tree or golden tree
        if (findActorWithType(onTile, "GoldenTree") != null) {
            atGoldenTree();
        }
        if ((interestedActor=findNonEmptyTree(onTile)) != null) {
            atTree((Tree) interestedActor);
        }

        // carrier at hoard or stockpile
        if ((interestedActor=findActorWithType(onTile, "Hoard")) != null) {
            atHoard((FruitStock) interestedActor);
        }
        if ((interestedActor=findActorWithType(onTile, "Stockpile")) != null) {
            atStockPile((FruitStock) interestedActor);
        }

    }

    protected void atMitosisPool(ArrayList<Actor> carriersToAdd, ArrayList<Actor> carriersToDelete) {

        // make and move new gatherers
        int clockwise90 = 90, anticlockwise90 = -90;

        Carrier newGatherer1 = createCarrier(getxCoord(), getyCoord(), getDirection() + anticlockwise90);
        newGatherer1.move();
        Carrier newGatherer2 = createCarrier(getxCoord(), getyCoord(), getDirection() + clockwise90);
        newGatherer2.move();

        // add to list of actors to add and delete
        carriersToAdd.add(newGatherer1);
        carriersToAdd.add(newGatherer2);
        carriersToDelete.add(this);
    }



    // abstract methods
    protected abstract void atTree(Tree oneTree);
    protected abstract void atGoldenTree();
    protected abstract Carrier createCarrier(int xCoord, int yCoord, int direction);
    protected abstract void atHoard(FruitStock oneHoard);
    protected abstract void atStockPile(FruitStock oneStockPile);
    protected abstract void atPad();
    protected abstract void onGatherer();

}
