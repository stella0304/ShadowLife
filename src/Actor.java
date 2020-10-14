import java.util.Objects;
import bagel.*;

public abstract class Actor {

    private int xCoord;
    private int yCoord;
    private Image image;
    private String actorType;

    public Actor(int xCoord, int yCoord, String imgLocation, String actorType) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.image = new Image(imgLocation);
        this.actorType = actorType;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setImage(String location) {
        this.image = new Image(location);
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public String getActorType() {
        return actorType;
    }

    public void displayImage() {
        image.draw(xCoord, yCoord);
    }
}
