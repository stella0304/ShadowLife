import bagel.*;

/** Parent class for all actors in the ShadowLife simulation.
 * include all shared properties of an actor
 */
public abstract class Actor {

    private int xCoord;
    private int yCoord;
    private Image image;
    private String actorType;

    /** Makes a new Actor object
     * @param xCoord x location of the actor
     * @param yCoord y location of the actor
     * @param imgLocation the file location of the image
     * @param actorType the type of the actor
     */
    public Actor(int xCoord, int yCoord, String imgLocation, String actorType) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.image = new Image(imgLocation);
        this.actorType = actorType;
    }

    /** Setter for xCoord
     * @param xCoord new x location of the actor
     */
    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    /** Setter for yCoord
     * @param yCoord new y location of the actor
     */
    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    /** Getter for xCoord
     * @return int the current x location of the actor
     */
    public int getxCoord() {
        return xCoord;
    }

    /** Getter for yCoord
     * @return int the current y location of the actor
     */
    public int getyCoord() {
        return yCoord;
    }

    /** Getter for the actorType
     * @return String the type of the actor
     */
    public String getActorType() {
        return actorType;
    }

    /** Displays the image of the actor on the ShadowLife simulation
     */
    public void displayImage() {
        image.draw(xCoord, yCoord);
    }
}
