import java.util.ArrayList;

public class PlayerHand {

    private int playerNum;
    public int stack;
    public ArrayList<Card> hand = new ArrayList<>();

    public boolean isFold;


    public PlayerHand() {
        isFold = false;
        stack = 1000;
    }

    public void assignNum(int n) {
        playerNum = n;
    }

    public int getStack() {
        return stack;
    }

    public void bet(int n) {
        stack-=n;
    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void fold() {
        isFold = true;
    }
}
