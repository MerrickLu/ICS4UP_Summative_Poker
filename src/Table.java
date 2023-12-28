import java.util.Scanner;

public class Table {

    static Scanner s = new Scanner(System.in);


    static PlayerHand[] players;
    CommunityHand comm;

    static int pos = 0; //current position of the betting;
    static Deck d = new Deck();
    static int you; //your place in the table

    public Table(int numPlayers) {
        d.shuffle();
        players = new PlayerHand[numPlayers];

    }

    public static void startGame() {
        System.out.println("Table Position (1-6): ");
        you = s.nextInt()-1;
        while(true) {

        }
    }

    public static void preflop() {
        System.out.println("\nShuffling Deck...");
        d.shuffle();

        //dealing the hands
        dealHands();

        //betting begins
    }

    public static void betting() {

    }

    public static void dealHands() {
        for(int i = 0; i<6; i++) {
            players[i] = new PlayerHand(i);
            players[i].addToHand(d.deal());
            players[i].addToHand(d.deal());
        }
    }
}
