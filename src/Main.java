/* Main class starts the game
All it does is run the constructor in GameFrame class

This is a common technique among coders to keep things organized (and handy when coding in repl.it since we're forced to call a class Main, which isn't always very descriptive)
*/

import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

class Main {
    public static void main(String[] args) {

//        new GameFrame();
//        Deck d = new Deck();
//        d.shuffle();
////        PlayerHand[] players = new PlayerHand[6];
////        PreflopRanges range = new PreflopRanges();
////        for(int[] arr: range.thirtyfive) {
////            for(int c: arr) {
////                System.out.print(c + " ");
////            }
////            System.out.println();
////        }
//        ArrayList<Card> list = new ArrayList<>();
//        list.add(new Card(0,0));
//        list.add(new Card(0,1));
//        list.add(new Card(0,2));
//        list.add(new Card(0,3));
//        list.add(new Card(5,3));
//        list.add(new Card(4,3));
//        list.add(new Card(4,2));
//        TotalHand h = new TotalHand(list);
//        list = new ArrayList<>();
//        list.add(new Card(0,0));
//        list.add(new Card(0,1));
//        list.add(new Card(0,2));
//        list.add(new Card(0,3));
//        list.add(new Card(4,3));
//        list.add(new Card(4,3));
//        list.add(new Card(3,2));
//        TotalHand h2 = new TotalHand(list);
//        System.out.println(h.getBestHand() +" " + h.compareTo(h2));
//        ArrayList<Card> c = new ArrayList<>();
//        c.add(new Card(0,0));
//        c.add(new Card(0,1));
//        PokerBot bot = new PokerBot(new PreflopRanges().getRanges()[0]);
//        bot.setHand(c);
////        System.out.println(bot.estimateStrength(new ArrayList<Card>()));

        Game g = new Game(5,10);//starts a 5/10 game
        g.startGame();

    }

}