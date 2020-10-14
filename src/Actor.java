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

    @Override
    public String toString() {
        return "Actor{" +
                "xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                ", image=" + image +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return xCoord == actor.xCoord &&
                yCoord == actor.yCoord &&
                image.equals(actor.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoord, yCoord, image);
    }

    public void displayImage() {
        image.draw(xCoord, yCoord);
    }
}
