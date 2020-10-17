import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public abstract class Carrier extends Actor {

    // map of the direction and their corresponding angles
    private final static Map<String, Integer> DIRECTIONS = new HashMap<>() {{
        put("LEFT", 0);
        put("UP", 90);
        put("RIGHT", 180);
        put("DOWN", 270);
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

    protected void rotate90Clockwise() {
        // rotate 90 degrees clockwise
        direction -= 90;
    }

    protected void rotate90AntiClockwise() {
        // rotate 90 degrees clockwise
        direction += 90;
    }

    protected void rotate180() {
        // rotate 180 degrees
        direction += 180;
    }

    public void atFence() {
        isActive = false;
        moveBack();
    }

    public void atSign(String signDirection) {
        direction = DIRECTIONS.get(signDirection);
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
    public void atTree(Tree oneTree) {
        if (!isCarrying) {
            if (oneTree.takeFruit()) {
                isCarrying = true;

                rotate180();
            }
        }
    }

    public void atGoldenTree() {
        if (!isCarrying) {
            // carry without having to decrease fruit amount
            isCarrying = true;

            rotate180();
        }
    }

    protected void atActor(Actor oneActor, ArrayList<Actor> actorsToAdd, ArrayList<Actor> actorsToDelete) {
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
    }

    // abstract methods
    public abstract void atMitosisPool(ArrayList<Actor> actorsToAdd, ArrayList<Actor> actorsToDelete);
    public abstract void atHoard(FruitStock oneHoard);
    public abstract void atStockPile(FruitStock oneStockPile);
    public abstract void checkForNonMoving(HashMap<String, Actor> nonMovingActors, ArrayList<Actor> actorsToAdd, ArrayList<Actor> actorsToDelete);

}
