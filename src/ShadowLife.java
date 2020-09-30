import bagel.*;
import java.util.*;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class ShadowLife extends AbstractGame{

    private List<Actor> actors;
    private Image background;

    private final int TILE = 64;
    private final int TICK = 500;
    private final String FILE_LOCATION = "res/worlds/test.csv";
    private int numTicks = 0;
    private int time = (int) System.currentTimeMillis();

    public ShadowLife() {
        super(960, 704, "ShadowLife");
        background = new Image("res/images/background.png");
        readCsv(FILE_LOCATION);
        //time = (int) System.currentTimeMillis();
    }

    public static void main(String[] args) {
        ShadowLife game = new ShadowLife();
        game.run();
    }

    @Override
    protected void update(Input input) {
        // display everything
        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        for (Actor a : actors) {
            a.getImage().draw(a.getxCoord(), a.getyCoord());
        }

        // check for tick & 5 tick
        boolean changeDirection = false;
        int currTime = (int) System.currentTimeMillis();
        if (currTime - time >= 500) {

            time = currTime;
            numTicks++;

            // 5th tick
            if (numTicks == 5) {
                changeDirection = true;
                numTicks = 0;
            }

            // move gatherer
            for (Actor a : actors) {
                if (a.getClass().getSimpleName().equals("Gatherer")) {
                    int newxCoord, newyCoord;
                    switch (((Gatherer) a).getDirection()) {
                        case "up":
                            newxCoord = a.getxCoord() - TILE;
                            a.setxCoord(newxCoord);
                            break;
                        case "down":
                            newxCoord = a.getxCoord() + TILE;
                            a.setxCoord(newxCoord);
                            break;
                        case "left":
                            newyCoord = a.getyCoord() - TILE;
                            a.setyCoord(newyCoord);
                            break;
                        case "right":
                            newyCoord = a.getyCoord() + TILE;
                            a.setyCoord(newyCoord);
                            break;
                    }
                    // change direction at 5th tick
                    if (changeDirection) {
                        ((Gatherer) a).changeDirection();
                    }
                }
            }
        }
    }

    private void readCsv(String file) {
        // reads in CSV file and store them in actors
        // csv: type, x-coord, y-coord

        actors = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            //read cvs
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");
                Actor oneActor;
                if (splitText[0].equals("Tree")) {
                    oneActor = new Tree(Integer.parseInt(splitText[1]), Integer.parseInt(splitText[2]));
                } else if (splitText[0].equals("Gatherer")) {
                    oneActor = new Gatherer(Integer.parseInt(splitText[1]), Integer.parseInt(splitText[2]));
                } else {
                    System.out.println("invalid actor type");
                    return;
                }
                actors.add(oneActor);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
