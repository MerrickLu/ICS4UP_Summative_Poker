import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class PlayerHand {

    private int playerNum;//don't actually think we need this, can't remember why i added it
    public int stack;
    public int inPot;
    public ArrayList<Card> hand = new ArrayList<>();

    public boolean isFold;
    public boolean isAllIn;

    Queue<PokerBot.BotAction> botAction = new ArrayDeque<PokerBot.BotAction>(); //queue of bot actions


    public PlayerHand() {
        isFold = false;
        isAllIn = false;
        stack = 1000;//all players start with 1000 credits
        inPot = 0;//no credits in the pot currently
    }

    public void assignNum(int n) {
        playerNum = n;
    }

    public int getStack() {
        return stack;
    }

    public void addToStack(int n) {
        stack+=n;
    }

    public void bet(int n) {
        stack-=n;
        inPot+=n;//removes from stack and adds to pot
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
    public void allIn() {
        isAllIn = true;
    }

    public void reset() {
        isFold = false;
        isAllIn = false;
        hand.clear();//clear hand
        resetActions();  //clear bot actions
        inPot = 0;
    }
    public void resetActions() {
        botAction.clear();
    }

    public void addAction(String s, int n) {
        botAction.add(new PokerBot.BotAction(s, n));
    }

    public PokerBot.BotAction getAction() {
        return botAction.peek();
    }

    public void nextAction() {
        botAction.poll();
    }

    public void printHand(){
        for(Card c: hand) {
            System.out.print(c + ", ");
        }
    }
}
