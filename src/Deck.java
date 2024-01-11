import java.sql.Array;
import java.util.*;

public class Deck {
    Queue<Card> deck = new ArrayDeque<Card>();
    public static final int SIZE = 52;


    //This makes a new deck that is completely in order
    public Deck() {
        for(int i = 0; i<52; i++) {
            deck.add(new Card(i));
        }
    }

    //prints the entire deck
    public void printDeck() {
        while(deck.peek()!=null) {
            System.out.println(deck.poll());
        }
    }

    //shuffles
    public void shuffle() {
        List<Card> shuffled = new ArrayList<Card>(deck);
        Collections.shuffle(shuffled);
        deck = new ArrayDeque<Card>(shuffled);
    }

    //deals one card
    public Card deal() {
        return deck.poll();
    }

//    public static Card[] Sort(Card[] arr) {
//        Card item;
//        int i;
//        for(int top = 0; top<arr.length; top++) {
//            item = arr[top];
//            i = top;
//
//            while(i>0 && item.getCardNum()>arr[i-1].getCardNum()) {
//                arr[i] = arr[i-1];
//                i--;
//            }
//            arr[i] = item;
//        }
//        return arr;
//    }

//    public static ArrayList<Card> dealStraightFlush() {
//        ArrayList<Card> ret = new ArrayList<>();
//        int suit = (int) (Math.random()*4);
//        int min = (int)(Math.random()*10);
//        for(int i = 0; i<7; i++) {
//            ret.add(new Card((min+i)%13, suit));
//        }
//        return ret;
//    }
}
