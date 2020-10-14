import bagel.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class ShadowLife extends AbstractGame{

    private ArrayList<Actor> movingActors;
    private HashMap<String, Actor> actors;
    private Image background;

    private int oneTick;
    private int numTicks = 0;
    private int maxNumTicks;
    private int prevTick = (int) System.currentTimeMillis();

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
            if (tickLen < 0 || maxTicks < 0) {
                throw new Exception("tick rate or max ticks are negative");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // System.exit(-1);
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

        // check for tick
        int currTime = (int) System.currentTimeMillis();
        if (currTime - prevTick >= 500) {

            prevTick = currTime;
            numTicks++;

            // move movingActors

        }

        // check for naxNumTick
        if (numTicks >= maxNumTicks) {
            System.out.println("Time Out");
            System.exit(-1);
        }
    }

    private void readCsv(String file) {
        // reads in CSV file and store them in actors
        // csv: type, x-coord, y-coord

        movingActors = new ArrayList<>();
        actors = new HashMap<>();

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
                    actors.add(oneActor);
                } else {
                    System.out.println("invalid actor type");
                    return;
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
