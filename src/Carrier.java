public abstract class Carrier extends Actor {

    private boolean isActive = true;
    private boolean isCarrying = false;
    private double direction;

    public Carrier(int xCoord, int yCoord, String imgLocation, String actorType, double direction) {
        super(xCoord, yCoord, imgLocation, actorType);
        this.direction = direction;
    }

    private void changeAngle(int angle) {
        direction += angle;
    }

    private void changeDirection(String direction) {

    }


}
