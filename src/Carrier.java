import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

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

    public Carrier(int xCoord, int yCoord, String imgLocation, String actorType, int direction) {
        super(xCoord, yCoord, imgLocation, actorType);
        this.direction = direction;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isCarrying() {
        return isCarrying;
    }

    public void setCarrying(boolean carrying) {
        isCarrying = carrying;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

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

    public void atFence() {
        isActive = false;
        moveBack();
    }

    public void atSign(String signDirection) {
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

    public boolean carrierAtGoldenTree() {
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

        if (findActorWithType(onTile, "Fence") != null) {
            atFence();
        }
        if (findActorWithType(onTile, "MitosisPool") != null) {
            atMitosisPool(actorsToAdd, actorsToDelete);
        }

        if (findActorWithType(onTile, "SignLeft") != null) {
            atSign("LEFT");
        } else if (findActorWithType(onTile, "SignRight") != null) {
            atSign("RIGHT");
        } else if (findActorWithType(onTile, "SignUp") != null) {
            atSign("UP");
        } else if (findActorWithType(onTile, "SignDown") != null) {
            atSign("DOWN");
        }

        if (this.getActorType().equals("Thief")) {
            // actions only performed by theives
            if (findActorWithType(onTile, "Pad") != null) {
                atPad();
            }
            if (gathererMap.containsKey(key)) {
                onGatherer();
            }
        }

        if (findActorWithType(onTile, "GoldenTree") != null) {
            atGoldenTree();
        }
        if ((interestedActor=findNonEmptyTree(onTile)) != null) {
            atTree((Tree) interestedActor);
        }

        if ((interestedActor=findActorWithType(onTile, "Hoard")) != null) {
            atHoard((FruitStock) interestedActor);
        }
        if ((interestedActor=findActorWithType(onTile, "Stockpile")) != null) {
            atStockPile((FruitStock) interestedActor);
        }

    }

    /*protected void atActor(Actor oneActor, ArrayList<Actor> actorsToAdd, ArrayList<Actor> actorsToDelete) {
        switch (oneActor.getActorType()) {
            case "Tree":
                this.atTree((Tree) oneActor);
                break;
            case "GoldenTree":
                this.atGoldenTree();
                break;
            case "Stockpile":
                this.atStockPile((FruitStock) oneActor);
                break;
            case "Hoard":
                this.atHoard((FruitStock) oneActor);
                break;
            case "Fence":
                this.atFence();
                break;
            case "SignUp":
                this.atSign("UP");
                break;
            case "SignDown":
                this.atSign("DOWN");
                break;
            case "SignLeft":
                this.atSign("LEFT");
                break;
            case "SignRight":
                this.atSign("RIGHT");
                break;
            case "MitosisPool":
                this.atMitosisPool(actorsToAdd, actorsToDelete);
                break;
        }
    }*/

    // abstract methods
    public abstract void atTree(Tree oneTree);
    public abstract void atGoldenTree();
    public abstract void atMitosisPool(ArrayList<Actor> actorsToAdd, ArrayList<Actor> actorsToDelete);
    public abstract void atHoard(FruitStock oneHoard);
    public abstract void atStockPile(FruitStock oneStockPile);
    public abstract void atPad();
    public abstract void onGatherer();

    /*public abstract void checkForNonMoving(HashMap<String, ArrayList<Actor>> nonMovingActors,
                                           HashMap<String, Gatherer> gathererMap, ArrayList<Actor> actorsToAdd,
                                           ArrayList<Actor> actorsToDelete);*/


}
