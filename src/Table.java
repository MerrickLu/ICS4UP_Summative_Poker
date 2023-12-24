public class Table {

    PlayerHand[] players;
    CommunityHand comm;
    Deck d = new Deck();
    int you; //your place in the table

    public Table(int numPlayers) {
        d.shuffle();
        players = new PlayerHand[numPlayers];
        for(int i = 0; i<6; i++) {
            players[i] = new PlayerHand(i);
            players[i].addToHand(d.deal());
            players[i].addToHand(d.deal());
        }
    }

    public static void startGame() {
        System.out.println("Welcome to Poker");

    }
}
