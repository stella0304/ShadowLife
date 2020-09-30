import java.util.Objects;
import java.util.Random;

public class Gatherer extends Actor {

    private static final String IMAGE_LOCATION = "res/images/gatherer.png";
    private static final String[] DIRECTIONS = {"up", "down", "left", "right"};
    // 0=up, 1=down, 2=left, 3=right

    private String direction = "";

    public Gatherer(int xCoord, int yCoord, int index) {
        // direction: takes a number between 0 and 3 and assign the
        // corresponding str in DIRECTIONS array
        super(xCoord, yCoord);
        super.setImage(IMAGE_LOCATION);
        this.direction = DIRECTIONS[index];
    }

    public Gatherer(int xCoord, int yCoord) {
        super(xCoord, yCoord);
        super.setImage(IMAGE_LOCATION);
        changeDirection();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(int index) {
        this.direction = DIRECTIONS[index];
    }

    @Override
    public String toString() {
        return "Gatherer{" +
                "direction='" + direction + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Gatherer gatherer = (Gatherer) o;
        return direction.equals(gatherer.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), direction);
    }

    public void changeDirection() {
        Random random = new Random();
        int index;
        do {
            index = random.nextInt(4);
        } while (this.direction == DIRECTIONS[index]);

        this.direction = DIRECTIONS[index];

    }
}
