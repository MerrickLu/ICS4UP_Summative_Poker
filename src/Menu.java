/* Home menu */

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Menu {
    public static Color crimson = new Color(187, 33, 14); // custom color
    public static File fontFile; // custom title font
    private final Image BG_IMAGE; // background image
    private final float TITLE_SIZE = GamePanel.GAME_HEIGHT/8; // font size scaled to panel size
    private final float TEXT_SIZE = GamePanel.GAME_HEIGHT/25;
    private String[] options = {"Start Game", "Training Mode", "Settings"};
    private int selectedOption = 3;


    public Menu() {
        fontFile = new File("images/PokerFont.otf"); // font path
        BG_IMAGE = new ImageIcon("images/MenuBG.jpg").getImage(); // bg image path
    }

    // draws everything to screen
    public void draw(Graphics g) throws IOException, FontFormatException {
        String text;
        g.drawImage(BG_IMAGE, 0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT, null); // draw background to fit dimensions of panel
        g.setColor(crimson);
        drawCenteredString(g, "Chip Masters", GamePanel.panelBounds, (int) (GamePanel.GAME_HEIGHT*0.32), Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(TITLE_SIZE));
        g.setColor(Color.white);

        for (double i = 0, yCoord = 0.47; i < options.length; i++, yCoord+=0.13) {
            text = options[(int)i];
            if((int) i == selectedOption-3) {
                text = "*" + text;
            }
            drawCenteredString(g, text, GamePanel.panelBounds, (int) (GamePanel.GAME_HEIGHT*yCoord), Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(TEXT_SIZE));
        }

    }

    // returns true to GamePanel if space is pressed (allowing game to start)
    public String keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (selectedOption>=0) selectedOption--;
            else selectedOption++;
            selectedOption = selectedOption%3+3;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (selectedOption>=0) selectedOption++;
            else selectedOption--;
            selectedOption = selectedOption%3+3;
        }

        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            return options[selectedOption-3];
        }
        System.out.println(selectedOption);
        return "";
    }

    // returns selection to GamePanel if enter is released (allowing game to start)
    public String keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            return options[Math.abs(selectedOption)];
        }
        return "";
    }

    /**
     * Draw a String centered in the horizontal middle of the screen, using a rectangle
     * @param g The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     * @param y The y location to draw the string in.
     * @param font The font of the string
     */
    public void drawCenteredString(Graphics g, String text, Rectangle rect, int y, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Draw the String
        g.setFont(font);
        g.drawString(text, x, y);
    }
}


