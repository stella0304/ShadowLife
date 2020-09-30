public class Tree extends Actor{

    private static final String IMAGE_LOCATION = "res/images/tree.png";

    public Tree(int xCoord, int yCoord) {
        super(xCoord, yCoord);
        super.setImage(IMAGE_LOCATION);
    }
}
