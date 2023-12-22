/* Main class starts the game
All it does is run the constructor in GameFrame class

This is a common technique among coders to keep things organized (and handy when coding in repl.it since we're forced to call a class Main, which isn't always very descriptive)
*/

class Main {
    public static void main(String[] args) {

//        new GameFrame();
        Deck d = new Deck();
        d.shuffle();
        Card[] hand = new Card[7];
        for(int i = 0; i<hand.length; i++) {
            hand[i] = d.deal();
        }
        Hand h = new Hand(hand);
        System.out.println(h.checkHand());
        System.out.println(h.checkHand().equals(Hand.handStrength.ROYAL_FLUSH));
    }
}