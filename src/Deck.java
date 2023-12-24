import java.sql.Array;
import java.util.*;

public class Deck {

    public HashSet<Card> drawnCards = new HashSet<Card>();
    Queue<Card> deck = new ArrayDeque<Card>();


    //This makes a new deck that is completely in order
    public Deck() {
        for(int i = 0; i<52; i++) {
            deck.add(new Card(i));
        }
    }

    public void printDeck() {
        while(deck.peek()!=null) {
            System.out.println(deck.poll());
        }
    }

    public void shuffle() {
        List<Card> shuffled = new ArrayList<Card>(deck);
        Collections.shuffle(shuffled);
        deck = new ArrayDeque<Card>(shuffled);
    }

    //deals one card
    public Card deal() {
        return deck.poll();
    }

    public static Card[] Sort(Card[] arr) {
        Card item;
        int i;
        for(int top = 0; top<arr.length; top++) {
            item = arr[top];
            i = top;

            while(i>0 && item.getCardNum()<arr[i-1].getCardNum()) {
                arr[i] = arr[i-1];
                i--;
            }
            arr[i] = item;
        }
        return arr;
    }

}
