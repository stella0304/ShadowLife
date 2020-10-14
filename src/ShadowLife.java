import bagel.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class ShadowLife extends AbstractGame{

    private final static int WIDTH = 960;
    private final static int HEIGHT = 704

    private ArrayList<Gatherer> gatherers;
    private ArrayList<Thief> thieves;
    private ArrayList<FruitStock> fruitstocks;
    private HashMap<String, Gatherer> gatherersMap;
    private HashMap<String, Actor> nonMovingActors;
    private Image background;

    private int oneTick;
    private int numTicks = 0;
    private int maxNumTicks;
    private int prevTick = (int) System.currentTimeMillis();

    public ShadowLife(int oneTick, int maxNumTicks, String fileLocation) {
        super(WIDTH, HEIGHT, "ShadowLife");
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
            System.exit(-1);
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

            boolean stillActive = false;
            prevTick = currTime;
            numTicks++;

            // move gatherers and draw their images

            // move gatherers and draw their images

            // draw all non moving actors' images

            if (!stillActive) {
                endGame();
            }
        }

        // check for naxNumTick
        if (numTicks >= maxNumTicks) {
            System.out.println("Time Out");
            System.exit(-1);
        }
    }

    private void readCsv(String file) {
        // reads in CSV file and store them in corresponding data structures
        // csv: type, x-coord, y-coord

        // initialise all data structures
        gatherers = new ArrayList<>();
        thieves = new ArrayList<>();
        fruitstocks = new ArrayList<>();
        gatherersMap = new HashMap<>();
        nonMovingActors = new HashMap<>();

        // read csv and store info
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            //read cvs
            String textRead;
            int lineNum = 0;
            while ((textRead = br.readLine()) != null) {
                lineNum++;
                String[] splitText = textRead.split(",");

                Actor oneActor;
                String hashKey = splitText[1] + "," + splitText[2];

                // check if one line is valid
                checkValidLine(splitText, file, lineNum);

                switch (splitText[0]) {
                    case "Tree":
                        oneActor = new Tree(Integer.parseInt(splitText[1]), Integer.parseInt(splitText[2]));
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "GoldenTree":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "GoldenTree");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "Stockpile":
                        oneActor = new FruitStock(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Stockpile");
                        fruitstocks.add((FruitStock) oneActor);
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "Hoard":
                        oneActor = new FruitStock(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Hoard");
                        fruitstocks.add((FruitStock) oneActor);
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "Pad":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Pad");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "Fence":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Fence");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "SignUp":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignUp");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "SignDown":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignDown");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "SignLeft":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignLeft");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "SignRight":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignRight");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "MitosisPool":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "MitosisPool");
                        nonMovingActors.put(hashKey, oneActor);
                        break;
                    case "Gatherer":
                        oneActor = new Gatherer(Integer.parseInt(splitText[1]), Integer.parseInt(splitText[2]));
                        gatherers.add((Gatherer) oneActor);
                        gatherersMap.put(hashKey, (Gatherer) oneActor);
                        break;
                    case "Thief":
                        oneActor = new Thief(Integer.parseInt(splitText[1]), Integer.parseInt(splitText[2]));
                        thieves.add((Thief) oneActor);
                        break;
                    default:
                        System.out.println("error: in file \"" + file + "\" at line" + lineNum);
                        System.exit(-1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error: file \"" + file + "\" not found");
            System.exit(-1);
        }
    }

    private void checkValidLine(String[] splitText, String file, int lineNum) {
        int x, y;
        try {
            x = Integer.parseInt(splitText[1]);
            y = Integer.parseInt(splitText[2]);
        } catch (Exception e) {
            System.out.println("error: in file \"" + file + "\" at line" + lineNum);
            System.exit(-1);
        }

        if (splitText.length>3 || x<0 || y<0 || x>WIDTH || y>HEIGHT) {
            System.out.println("error: in file \"" + file + "\" at line" + lineNum);
            System.exit(-1);
        }
    }

    private void endGame() {
        System.out.println(numTicks + "Ticks");
        for (FruitStock f : fruitstocks) {
            System.out.println(f.getNumFruit());
        }
        System.exit(0);
    }
}
