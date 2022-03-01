import bagel.*;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class for a ShadowLife simulation. Runs the simulation.
 */
public class ShadowLife extends AbstractGame{

    private final static int WIDTH = 960;
    private final static int HEIGHT = 704;

    private ArrayList<Gatherer> gatherers;
    private ArrayList<Thief> thieves;
    private ArrayList<FruitStock> fruitstocks;
    private HashMap<String, Gatherer> gatherersMap;
    private HashMap<String, ArrayList<Actor>> nonMovingActors;
    private ArrayList<ContentDrawable> drawables;
    private final Image background;

    private final int oneTick;
    private int numTicks = 0;
    private final int maxNumTicks;
    private int prevTick = (int) System.currentTimeMillis();

    /**
     * Constructor of one ShadowLife simulation
     * @param oneTick the length of one tick
     * @param maxNumTicks the max number of ticks before the simulation times out
     * @param fileLocation the location of the world file the simulation is built on top of
     */
    public ShadowLife(int oneTick, int maxNumTicks, String fileLocation) {
        super(WIDTH, HEIGHT, "ShadowLife");
        this.oneTick = oneTick;
        this.maxNumTicks = maxNumTicks;
        background = new Image("res/images/background.png");
        readCsv(fileLocation);
    }

    /**
     * main method which constructs a ShadowLife simulation and runs it
     */
    public static void main(String[] args) {
        // check if the inputs are valid - mac user
        String[] macArgs = argsFromFile();

        if (macArgs == null || macArgs.length!=3) {
            System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
            System.exit(-1);
        }

        int tickLen=-1, maxTicks=-1;
        try {
            tickLen = Integer.parseInt(macArgs[0]);
            maxTicks = Integer.parseInt(macArgs[1]);
            if (tickLen < 0 || maxTicks < 0) {
                throw new Exception("tick rate or max ticks are negative");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // run game
        ShadowLife game = new ShadowLife(tickLen, maxTicks, macArgs[2]);
        game.run();
    }

    @Override
    protected void update(Input input) {
        // testing testing

        // check for tick, move accordingly
        int currTime = (int) System.currentTimeMillis();
        if (currTime - prevTick >= oneTick) {

            boolean stillActive = false;
            prevTick = currTime;

            ArrayList<Actor> gatherersToAdd = new ArrayList<>();
            ArrayList<Actor> gatherersToDelete = new ArrayList<>();
            ArrayList<Actor> thievesToAdd = new ArrayList<>();
            ArrayList<Actor> thievesToDelete = new ArrayList<>();

            // move gatherers, check their tiles
            for (Gatherer g : gatherers) {
                if (g.isActive()) {
                    stillActive = true;
                    g.move();
                    g.checkForNonMoving(nonMovingActors, gatherersMap, gatherersToAdd, gatherersToDelete);
                }
            }

            // add & delete gatherers
            for (Actor a : gatherersToAdd) {
                gatherers.add((Gatherer) a);
            }
            for (Actor a : gatherersToDelete) {
                gatherers.remove((Gatherer) a);
            }

            // redo gatherers map according to their new positions
            gatherersMap = new HashMap<>();
            for (Gatherer g : gatherers) {
                String key = g.getxCoord() + "," + g.getyCoord();
                gatherersMap.put(key, g);
            }

            // move thieves, check their tiles, and draw their images
            for (Thief t : thieves) {
                if (t.isActive()) {
                    stillActive = true;
                    t.move();
                    t.checkForNonMoving(nonMovingActors, gatherersMap, thievesToAdd, thievesToDelete);
                    //t.checkForGatherer(gatherersMap);
                }
            }

            // add & delete thieves
            for (Actor a : thievesToAdd) {
                thieves.add((Thief) a);
            }
            for (Actor a : thievesToDelete) {
                thieves.remove((Thief) a);
            }

            // if all thieves and gatherers are inactive, end simulation
            if (!stillActive) {
                endGame();
            }

            numTicks++;
        }

        // draw everything

        // draw background
        background.draw(WIDTH/2, HEIGHT/2);

        // draw all non moving actors' images
        for (String key : nonMovingActors.keySet()) {
            for (Actor a : nonMovingActors.get(key)) {
                a.displayImage();
            }
        }

        // draw the contents of trees, stockpiles and hoards
        for (ContentDrawable c : drawables) {
            c.drawContent();
        }

        // draw gatherers
        for (Gatherer g : gatherers) {
            g.displayImage();
        }

        // draw thieves
        for (Thief t : thieves) {
            t.displayImage();
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
        drawables = new ArrayList<>();
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
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        drawables.add((Tree) oneActor);
                        break;
                    case "GoldenTree":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "GoldenTree");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "Stockpile":
                        oneActor = new FruitStock(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Stockpile");
                        fruitstocks.add((FruitStock) oneActor);
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        drawables.add((FruitStock) oneActor);
                        break;
                    case "Hoard":
                        oneActor = new FruitStock(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Hoard");
                        fruitstocks.add((FruitStock) oneActor);
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        drawables.add((FruitStock) oneActor);
                        break;
                    case "Pad":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Pad");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "Fence":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "Fence");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "SignUp":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignUp");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "SignDown":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignDown");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "SignLeft":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignLeft");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "SignRight":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "SignRight");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "Pool":
                        oneActor = new StationaryActor(Integer.parseInt(splitText[1]),
                                Integer.parseInt(splitText[2]), "MitosisPool");
                        checkArrayExists(hashKey);
                        nonMovingActors.get(hashKey).add(oneActor);
                        break;
                    case "Gatherer":
                        oneActor = new Gatherer(Integer.parseInt(splitText[1]), Integer.parseInt(splitText[2]));
                        gatherers.add((Gatherer) oneActor);
                        //gatherersMap.put(hashKey, (Gatherer) oneActor);
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
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error: file \"" + file + "\" not found");
            System.exit(-1);
        }
    }

    private void checkValidLine(String[] splitText, String file, int lineNum) {
        int x=-1, y=-1;
        try {
            x = Integer.parseInt(splitText[1]);
            y = Integer.parseInt(splitText[2]);
        } catch (NumberFormatException e) {
            System.out.println("error: in file \"" + file + "\" at line" + lineNum);
            System.exit(-1);
        }

        if (splitText.length>3 || x<0 || y<0 || x>WIDTH || y>HEIGHT) {
            System.out.println("error: in file \"" + file + "\" at line" + lineNum);
            System.exit(-1);
        }
    }

    private void endGame() {
        System.out.println(numTicks + " Ticks");
        for (FruitStock f : fruitstocks) {
            System.out.println(f.getNumFruit());
        }
        System.exit(0);
    }

    private static String[] argsFromFile() {
        // the following code was taken from the project spec of Project 2 of SWEN20003 at the University of Melbourne
        try {
            return Files.readString(Path.of("args.txt"), Charset.defaultCharset()) .split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkArrayExists(String key) {
        if (!nonMovingActors.containsKey(key)) {
            ArrayList<Actor> newActorList = new ArrayList<>();
            nonMovingActors.put(key, newActorList);
        }
    }
}
