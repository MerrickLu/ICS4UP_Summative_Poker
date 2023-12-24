import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class TotalHand {

    private List<List<Card>> nums = new ArrayList<List<Card>>(14);

    public enum HandStrength {
        ROYAL_FLUSH, STRAIGHT_FLUSH, QUADS, FULL_HOUSE, FLUSH, STRAIGHT, TRIPS, TWO_PAIR, PAIR, HIGH_CARD
    }


    private HandStrength strength;


    public TotalHand(ArrayList<Card> c) {
        if(c!=null) {
            c.sort((o1, o2) -> Integer.compare(o1.getCardNum(), o2.getCardNum()));
            //Accounting for ace high and low ace high and ace low
            ArrayList<Card> lowAces = new ArrayList<Card>();
            ArrayList<Card> highAces = new ArrayList<Card>();

            for (Card card : c) {
                if (card.getCardNum() == 0) {
                    lowAces.add(card);
                    highAces.add(new Card(13, card.getCardSuit()));
                }
            }
            nums.add(lowAces);
            for (int i = 1; i < 13; i++) {
                ArrayList<Card> toAdd = new ArrayList<Card>();
                for (Card card : c) {
                    if (card.getCardNum() == i) {
                        toAdd.add(card);
                    }
                }
                nums.add(toAdd);
            }
            nums.add(highAces);
        }
    }

    //gets all suited cards if there are more than 5 of them in a suit
    public ArrayList<Card> getFlushed() {
        ArrayList<Card> diamonds = new ArrayList<Card>();
        ArrayList<Card> clubs = new ArrayList<Card>();
        ArrayList<Card> hearts = new ArrayList<Card>();
        ArrayList<Card> spades = new ArrayList<Card>();

        for(List<Card> innerList : nums) {
            for(Card c : innerList) {
                switch(c.getCardSuit()) {
                    case 0: diamonds.add(c);
                    break;
                    case 1: clubs.add(c);
                        break;
                    case 2: hearts.add(c);
                        break;
                    case 3: spades.add(c);
                        break;
                }
            }
        }
        if(diamonds.size()>=5) return diamonds;
        else if(clubs.size()>=5) return clubs;
        else if(hearts.size()>=5) return hearts;
        else if(spades.size()>=5) return spades;
        else return null;
    }

    //returns the biggest flush
    public ArrayList<Card> getBiggestFlush() {
        ArrayList<Card> flush = getFlushed();
        if(flush==null) return null;
        while(flush.size()>5) {
            flush.removeFirst();
        }
        if(flush.getFirst().getCardNum()==0) return null;
        else {
            strength = HandStrength.FLUSH;
            return flush;
        }
    }

    //returns the biggest straight
    public ArrayList<Card> getStraight() {
        int count = 0;
        int minLoc = 0;
        int i = nums.size()-1;
        ArrayList<Card> straight = new ArrayList<>();
        done: while(i>=0) {
            while(i>=0 && nums.get(i).isEmpty()) {
                i--;
                count=0;
            }
            while(i>=0 && !nums.get(i).isEmpty()) {
                count++;
                if(count==5) {
                    minLoc = i;
                    break done;
                }
                i--;
            }
        }

        if(count<5) return null;

        for(int j = minLoc; j<minLoc+5; j++) {
            straight.add(nums.get(j).getFirst());
        }
        strength = HandStrength.STRAIGHT;
        return straight;
    }

    //gets suited straight
    public ArrayList<Card> getStraightFlush() {
        TotalHand flush = new TotalHand(getFlushed()); //make a new hand with all suited cards
        ArrayList<Card> straightFlush = flush.getStraight();
        if(straightFlush==null) return null;

        if(straightFlush.getFirst().getCardNum()==9) {
            //this is a royal flush
            strength = HandStrength.ROYAL_FLUSH;
        }
        else strength = HandStrength.STRAIGHT_FLUSH;
        return straightFlush;
    }

    //gets literally anything else
    public ArrayList<Card> getElse() {
        //check in ascending order for the number with the most frequency
        //this is guaranteed to be added
        HashSet<Integer> invalidIdx = new HashSet<Integer>();
        int maxLoc = 0;
        int spotsLeft = 5;
        ArrayList<Card> bestHand = new ArrayList<>();
        String str = ""; //this represents the strength of the hand

        while(spotsLeft>0) {
            maxLoc = 0;
            for(int i = 0; i<nums.size(); i++){
                if(!invalidIdx.contains(i) //checking if i is valid
                &&nums.get(i).size()>=nums.get(maxLoc).size() //checking if freq is bigger
                && nums.get(i).size()<=spotsLeft //checking if hand can fit
                ) {
                    maxLoc = i;
                }
            }
            bestHand.addAll(nums.get(maxLoc));
            spotsLeft-=nums.get(maxLoc).size();
            str+=nums.get(maxLoc).size();
            //if it was aces, invalidate high ace and low ace
            if(maxLoc==nums.size()-1) invalidIdx.add(0);
            invalidIdx.add(maxLoc);
        }

        switch(str) {
            case "41":
                strength = HandStrength.QUADS;
                break;
            case "32":
                strength = HandStrength.FULL_HOUSE;
                break;
            case "311":
                strength = HandStrength.TRIPS;
                break;
            case "221":
                strength = HandStrength.TWO_PAIR;
                break;
            case "2111":
                strength = HandStrength.PAIR;
                break;
            case "11111":
                strength = HandStrength.HIGH_CARD;
                break;
        }
        return bestHand;
    }

    public ArrayList<Card> getBestHand() {
        //check hands in ascending order
        ArrayList<Card> best = new ArrayList<>();

        best = getElse();
        switch(strength) {
            case QUADS, FULL_HOUSE:
                best = getStraightFlush()==null? best:getStraightFlush();
                break;
            default:
                best = getStraight()==null? best:getStraight();
                best = getBiggestFlush()==null? best:getBiggestFlush();
                best = getStraightFlush()==null? best:getStraightFlush();
                break;
        }
        return best;
    }

    public HandStrength getStrength() {
        return strength;
    }

    public String toString() {
        String str = "";
        for(int i = 0; i<nums.size(); i++){
            for(int j = 0; j<nums.get(i).size(); j++) {
                str+=nums.get(i).get(j) + "\n";
            }
//            str+="\n";
        }
        return str;
    }
}
