/* Main class starts the game
All it does is run the constructor in GameFrame class

This is a common technique among coders to keep things organized (and handy when coding in repl.it since we're forced to call a class Main, which isn't always very descriptive)
*/

import java.awt.*;
import java.util.ArrayList;

class Main {
    public static void main(String[] args) {

//        new GameFrame();
        Deck d = new Deck();
        d.shuffle();
        ArrayList<Card> hand = new ArrayList<Card>();
//        hand.add(new Card(0, 0));
//        hand.add(new Card(0, 0));
//        hand.add(new Card(4, 1));
//        hand.add(new Card(11, 2));
//        hand.add(new Card(11, 3));
//        hand.add(new Card(11, 0));
//        hand.add(new Card(4, 0));
        for(int i = 0; i<7; i++) {
            hand.add(d.deal());
        }
        TotalHand h = new TotalHand(hand);

        System.out.println(h);
        System.out.println(h.getBestHand());
        System.out.println(h.getStrength());
    }

}