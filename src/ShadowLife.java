import bagel.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class ShadowLife extends AbstractGame{

    private ArrayList<Actor> movingActors;
    private HashMap<String, Actor> stationaryActors;
    private Image background;

    private final int TILE = 64;
    private int oneTick;
    private int numTicks = 0;
    private int maxNumTicks;
    private int time = (int) System.currentTimeMillis();

    public ShadowLife(int oneTick, int maxNumTicks, String fileLocation) {
        super(960, 704, "ShadowLife");
        this.oneTick = oneTick;
        this.maxNumTicks = maxNumTicks;
        background = new Image("res/images/background.png");
        readCsv(fileLocation);
    }

    public static void main(String[] args) {
        // check if the inputs are valid
        if (args.length!=3) {
            System.exit(-1);
        }

        int tickLen, maxTicks;
        try {
            tickLen = Integer.parseInt(args[0]);
            maxTicks = Integer.parseInt(args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // run game
        ShadowLife game = new ShadowLife(tickLen, maxTicks, args[2]);
        game.run();
    }

    @Override
    protected void update(Input input) {
        // display everything
        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        for (Actor a : actors) {
            a.displayImage();
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
