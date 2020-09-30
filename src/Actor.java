import java.util.Objects;
import bagel.*;

public abstract class Actor {

    private int xCoord;
    private int yCoord;
    private Image image;

    public Actor(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setImage(String location) {
        Image oneImage = new Image(location);
        this.image = oneImage;
    }

    public Image getImage() {
        return image;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
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
}
