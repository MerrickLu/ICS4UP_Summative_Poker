import java.util.ArrayList;

public class PlayerHand {

    private int playerNum;
    public ArrayList<Card> hand = new ArrayList<>();


    public PlayerHand(int n) {
        playerNum = n;
    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

}
