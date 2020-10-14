import java.util.HashMap;
import java.util.Map;

public class FruitStock extends Actor implements ContentDrawable{

    private final static Map<String, String> IMG_LOCATIONS = new HashMap<>() {{
        put("Stockpile", "res/images/cherries.png");
        put("Hoard", "res/images/hoard.png");
    }};

    int numFruit = 0;

    public FruitStock(int xCoord, int yCoord, String actorType) {
        super(xCoord, yCoord, IMG_LOCATIONS.get(actorType), actorType);
    }

    public int getNumFruit() {
        return numFruit;
    }

    public void addFruit() {
        numFruit++;
    }

    public boolean takeFruit() {
        if (numFruit <= 1) {
            return false;
        } else {
            numFruit--;
            return true;
        }
    }

    public void drawContent() {
        FONT.drawString(Integer.toString(numFruit), this.getxCoord(), this.getyCoord());
    }

}
