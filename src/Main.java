/* Main class starts the game
All it does is run the constructor in GameFrame class

This is a common technique among coders to keep things organized (and handy when coding in repl.it since we're forced to call a class Main, which isn't always very descriptive)
*/

import java.awt.*;

class Main {
    public static void main(String[] args) {

//        new GameFrame();
        Deck d = new Deck();
        d.shuffle();
        Card[] hand = {new Card(0,0),
                new Card(12,0),
                new Card(0,1),
                new Card(0,2),
                new Card(11,0),
                new Card(0,3),
                new Card(2,2)};
        Hand h = new Hand(hand);
        System.out.println(h);
//        for(int i = 0; i<hand.length; i++) {
//            hand[i] = d.deal();
//        }

        for(Card c:h.getElse()) {
            System.out.println(c);
        }
        System.out.println(h.strength);
    }
}