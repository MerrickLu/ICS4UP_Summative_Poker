import java.util.ArrayList;

public class CommunityHand {

    public ArrayList<Card> hand = new ArrayList<>();

    public CommunityHand() {

    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }
}
