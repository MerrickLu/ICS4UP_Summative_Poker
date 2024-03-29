import java.util.ArrayList;

public class PokerBot {
    int[][] range; //a range table
    PlayerHand hand;

    public PokerBot(int[][] r) {
        range = r;
    }

    public void setHand(PlayerHand h) {
        hand = h;
    }

    //gets decision based on your average hand strength in 100 hands
    public void getDecision(Game g) { //gets decision after flop
        g.players[g.getCurrentPos()].resetActions();
        this.setHand(g.players[g.getCurrentPos()]);
        if(estimateStrength(g.getComm())>3) {//very strong hand
            hand.addAction("MR", 0);
            hand.addAction("MR", 0);
            hand.addAction("A", 0);
        }
        else if(estimateStrength(g.getComm())>2) {
            hand.addAction("MR", 0);//1 bet, then call up to a third of it's stack
            hand.addAction("C", Math.max(g.getBB()*20, hand.getStack()/4));
        }
        else if(estimateStrength(g.getComm())>1) {
            hand.addAction("C", hand.getStack()/10);
            hand.addAction("F", 0);
        }
        else {
            hand.addAction("F", 0);
        }
    }

    //For decisions preflop. It is based on the range tables
    public void getDecisionPreFlop(Game g) {
        g.players[g.getCurrentPos()].resetActions();
        this.setHand(g.players[g.getCurrentPos()]);
        int x, y;
        if(hand.getHand().getFirst().getCardSuit()==hand.getHand().getLast().getCardSuit()) {//suited hand
            x = Math.max(hand.getHand().getFirst().getCardNum(), hand.getHand().getLast().getCardNum());
            y = Math.min(hand.getHand().getFirst().getCardNum(), hand.getHand().getLast().getCardNum());
        }
        else {
            y = Math.max(hand.getHand().getFirst().getCardNum(), hand.getHand().getLast().getCardNum());
            x = Math.min(hand.getHand().getFirst().getCardNum(), hand.getHand().getLast().getCardNum());
        }
        int temp;
        if(x==0) {//if x is an ace
            x = y;
            y = 13;
        }
        else if(y==0) {//if y is an ace
            y = x;
            x = 13;
        }
        x-=1;//index conversion
        y-=1;

        switch(range[x][y]) {
            case 3:
                hand.addAction("R", (int)((Math.random()+2.2)*g.getBB()));
                hand.addAction("MR", 0);//2 bets then call any
                hand.addAction("CA", 0);
            case 2:
                hand.addAction("R", (int)((Math.random()*0.5+2)*g.getBB()));//1 bet then call any
                hand.addAction("C", Math.max(g.getBB()*10, hand.getStack()/10));
            case 1:
                hand.addAction("C", g.getBB()*2);
                hand.addAction("F", 0);//fold
            default:
                hand.addAction("F", 0);
        }
    }

    //deals 100 hands and returns the average strength
    private int estimateStrength (ArrayList<Card> com) {
        //deal 100 hands and estimate average strength of hand
        ArrayList<Card> comm = (ArrayList<Card>)com.clone();
        comm.addAll(hand.getHand());
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

    public static class BotAction {
        //this is an item that describes an action that the bot will take. It is put into a queue in playerhand.
        String type;//type of action: MR for MinRaise, R for raise, A for all in, CA for call any, C for call, F for check/fold
        int amount;
        public BotAction(String s, int a) {
            type = s;
            amount = a;
        }
        public BotAction() {
        }

        public String getType() {
            return type;
        }
        public int getAmount() {
            return amount;
        }
    }
}
