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
        Queue<Card> newDeck = new ArrayDeque<Card>();
        for(Card c : shuffled){
            newDeck.add(c);
        }
        deck = newDeck;


    }

}
