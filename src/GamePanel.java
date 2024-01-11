import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePanel extends JPanel implements Runnable, KeyListener{

    //dimensions of window
    public static final int GAME_WIDTH = 867;
    public static final int GAME_HEIGHT = 500;
    public static final Rectangle panelBounds = new Rectangle(0, 0, GAME_WIDTH, GAME_HEIGHT);


    public final static String IMAGE_FOLDER_LOCATION = "images" + File.separator;
    public final static String POKER_TABLE_IMAGE = IMAGE_FOLDER_LOCATION + "poker_table.png";

    private static JPanel tablePanel;
    private static JPanel cardPanel;
    private static JPanel buttonPanel;

    private static JLabel messageLabel;
    private static JLabel balanceLabel = new JLabel("Balance: $0.00");
    private static JLabel timeLeftLabel = new JLabel("Time Left: 0:00");
    private static HashMap<Integer, String> cardMap;
    private static Deck theDeck;

    public Menu menu = new Menu();

    public Thread gameThread;
    Image image;
    Graphics graphics;



    public GamePanel(){

        cardMap = new HashMap<Integer, String>();
        for (int i = 1; i <= Deck.SIZE; i++) {
            cardMap.put(i, IMAGE_FOLDER_LOCATION + i + ".png");
        }

        this.setFocusable(true); //make everything in this class appear on the screen
        this.addKeyListener(this); //start listening for keyboard input

        //add the MousePressed method from the MouseAdapter - by doing this we can listen for mouse input. We do this differently from the KeyListener because MouseAdapter has SEVEN mandatory methods - we only need one of them, and we don't want to make 6 empty methods
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

            }
        });
        this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        // make this class run at the same time as other classes
        gameThread = new Thread(this);
        gameThread.start();
    }

    //paint is a method in java.awt library that we are overriding. It is a special method - it is called automatically in the background in order to update what appears in the window. You NEVER call paint() yourself
    public void paint(Graphics g){
        //we are using "double buffering here" - if we draw images directly onto the screen, it takes time and the human eye can actually notice flashes of lag as each pixel on the screen is drawn one at a time. Instead, we are going to draw images OFF the screen, then simply move the image on screen as needed.
        image = createImage(GAME_WIDTH, GAME_HEIGHT); //draw off screen
        graphics = image.getGraphics();
        try {
            draw(graphics);//update the positions of everything on the screen
        } catch (Exception e){
            e.printStackTrace();
        }
        g.drawImage(image, 0, 0, this); //move the image on the screen

    }

    //call the draw methods in each class to update positions as things move
    public void draw(Graphics g) throws IOException, FontFormatException {
        menu.draw(g);
    }

    //call the move methods in other classes to update positions
    //this method is constantly called from run(). By doing this, movements appear fluid and natural. If we take this out the movements appear sluggish and laggy
    public void move(){

    }

    //handles all collision detection and responds accordingly
    public void checkAction(){
    }

    //run() method is what makes the game continue running without end. It calls other methods to move objects,  check for collision, and update the screen
    public void run(){
        //the CPU runs our game code too quickly - we need to slow it down! The following lines of code "force" the computer to get stuck in a loop for short intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        long now;

        while(true){ //this is the infinite game loop
            now = System.nanoTime();
            delta = delta + (now-lastTime)/ns;
            lastTime = now;

            //only move objects around and update screen if enough time has passed
            if(delta >= 1){
                move();
                repaint();
                delta--;
            }
        }
    }

    //if a key is pressed, we'll send it over to the PlayerBall class for processing
    public void keyPressed(KeyEvent e){
        menu.keyPressed(e);
    }

    //if a key is released, we'll send it over to the PlayerBall class for processing
    public void keyReleased(KeyEvent e){
        menu.keyReleased(e);
    }

    //left empty because we don't need it; must be here because it is required to be overridded by the KeyListener interface
    public void keyTyped(KeyEvent e){

    }
}