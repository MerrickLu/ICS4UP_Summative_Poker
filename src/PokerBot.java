import java.util.ArrayList;

public class PokerBot {
    int[][] range;
    ArrayList<Card> hand;

    public PokerBot(int[][] r) {
        range = r;
    }

    public void setHand(ArrayList<Card> h) {
        hand = h;
    }

    public String getDecision(ArrayList<Card> comm) {
        if(estimateStrength(comm)>5) {
            return "R3";
        }
        else if(estimateStrength(comm)>3) {
            return "R2"; //Re-Raise
        }
        else if(estimateStrength(comm)>1) {
            return "C"; //Call
        }
        else {
            return "K"; //Check
        }
    }

    public String getDecision() {
        int x, y;
        if(hand.getFirst().getCardSuit()==hand.getLast().getCardSuit()) {//suited hand
            x = Math.max(hand.getFirst().getCardNum(), hand.getLast().getCardNum());
            y = Math.min(hand.getFirst().getCardNum(), hand.getLast().getCardNum());
        }
        else {
            y = Math.max(hand.getFirst().getCardNum(), hand.getLast().getCardNum());
            x = Math.min(hand.getFirst().getCardNum(), hand.getLast().getCardNum());
        }
        switch(range[x][y]) {
            case 3:
                return "R2";
            case 2:
                return "R1";
            case 1:
                return "C";
            default:
                return "K";
        }
    }

    private int estimateStrength (ArrayList<Card> comm) {
        //deal 100 hands and estimate average strength of hand
        comm.addAll(hand);
        int size = comm.size();
        int avg = 0;
        Deck d;
        TotalHand h;
        for(int j = 0; j<100; j++) {
            d = new Deck();
            d.shuffle();
            for (int i = 0; i < (7 - size); i++) {
                comm.add(d.deal());
            }
            h = new TotalHand(comm);
            h.getBestHand();
            avg+=h.getStrength().compareTo(TotalHand.HandStrength.HIGH_CARD);
            for(int i = 0; i<(7-size); i++) {
                comm.removeLast();
            }
        }
        return avg/=100;
    }
}
