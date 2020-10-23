import java.util.HashMap;
import java.util.Map;

/**
 * Class for all Actors with no actions on ticks
 */
public class StationaryActor extends Actor {
    private final static Map<String, String> IMG_LOCATIONS = new HashMap<>() {{
        put("GoldenTree", "res/images/gold-tree.png");
        put("Pad", "res/images/pad.png");
        put("Fence", "res/images/fence.png");
        put("MitosisPool", "res/images/pool.png");
        put("SignUp", "res/images/up.png");
        put("SignDown", "res/images/down.png");
        put("SignLeft", "res/images/left.png");
        put("SignRight", "res/images/right.png");
    }};

    /**
     * Makes a new StationaryActor
     * @param xCoord x position of the StationaryActor
     * @param yCoord y position of the StationaryActor
     * @param actorType the new actor's type
     */
    public StationaryActor(int xCoord, int yCoord, String actorType) {
        super(xCoord, yCoord, IMG_LOCATIONS.get(actorType), actorType);
    }
}