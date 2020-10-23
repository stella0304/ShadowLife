/**
 * Class for a tree, not including golden trees
 */
public class Tree extends Actor implements ContentDrawable{

    private static final String IMAGE_LOCATION = "res/images/tree.png";
    private static final String TYPE = "Tree";
    private int numFruit = 3;

    /**
     * Makes a new Tree object
     * @param xCoord the x position of the Tree
     * @param yCoord the y position of the Tree
     */
    public Tree(int xCoord, int yCoord) {
        super(xCoord, yCoord, IMAGE_LOCATION, TYPE);
    }

    /**
     * Getter for numFruit
     * @return int the number of fruits on this tree
     */
    public int getNumFruit() {
        return numFruit;
    }

    /**
     * Tries to take a fruit from this tree
     * @return boolean true if successfully taken fruit, false otherwise
     */
    public boolean takeFruit() {
        if (numFruit <= 0) {
            return false;
        } else {
            numFruit--;
            return true;
        }
    }

    /**
     * From ContentDrawable interface, displays the number of fruits on this tree on the ShadowLife simulation
     */
    public void drawContent() {
        FONT.drawString(Integer.toString(numFruit), this.getxCoord(), this.getyCoord());
    }


}
