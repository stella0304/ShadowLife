public class Tree extends Actor implements ContentDrawable{

    private static final String IMAGE_LOCATION = "res/images/tree.png";
    private static final String TYPE = "Tree";
    private int numFruit = 3;

    public Tree(int xCoord, int yCoord) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE);
    }

    public int getNumFruit() {
        return numFruit;
    }

    public boolean takeFruit() {
        if (numFruit <= 0) {
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
