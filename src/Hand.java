import java.util.Arrays;
import java.util.List;

public class Hand {

    public int[] frqNums = new int[14];
    public int[] frqSuits = new int[4];
    public int[] cardNums;
    public int[] cardSuits;

    public enum HandStrength {
        ROYAL_FLUSH, STRAIGHT_FLUSH, QUADS, FULL_HOUSE, FLUSH, STRAIGHT, TRIPS, TWO_PAIR, PAIR, HIGH_CARD
    }

    public HandStrength strength;

    public Hand (Card[] c) {
        c = Deck.Sort(c);//sort the hand

        //getting frequency of numbers and suits
        for(int i = 0; i<c.length; i++) {
            frqSuits[c[i].getCardSuit()]++;
            frqNums[c[i].getCardNum()]++;
        }

        //accounting for aces
        frqNums[13] = frqNums[0];
        cardNums = new int[c.length + frqNums[0]];
        cardSuits = new int[c.length + frqNums[0]];
        for(int i = 0; i<frqNums[0]; i++) {
            cardNums[cardNums.length-frqNums[0]+i] = 13; //ace
            cardSuits[cardSuits.length-frqNums[0]+i] = c[i].getCardSuit();
        }

        //getting the actual numbers and suits
        for(int i = 0; i<c.length; i++) {
            cardNums[i] = c[i].getCardNum();
            cardSuits[i] = c[i].getCardSuit();
        }
    }

    public Card[] getFlush() {
        int count = 0;
        int suit = -1;
        //going through frequency array
        for(int i = 0; i<frqSuits.length; i++) {
            if(frqSuits[i]>=5) {
                suit = i;

            }
        }
        if(suit==-1) return null;

        Card[] flush = new Card[frqSuits[suit]+1];

        //grabbing all suited cards
        for(int i = cardNums.length-1; i> -1; i--) {
            if(cardSuits[i]==suit) {
                flush[count] = new Card(cardNums[i], suit);
                count++;
            }
        }
        return flush;
    }

    public Card[] getBiggestFlush() {
        Card[] suited = getFlush();
        if(suited==null) return null;
        Card[] flush = new Card[5];
        for(int i = 0; i<5; i++) {
            flush[i] = new Card(suited[suited.length-(5-i)].getCardNum(), suited[0].getCardSuit());
        }
        strength = HandStrength.FLUSH;
        return flush;
    }

    public Card[] getStraightFlush() {
        Card[] straightFlush = new Card[5];
        Card[] suited = getFlush(); //this will contain all cards of the correct suit

        //there cannot be a straight flush if there is no flush
        if(suited==null) return null;

        Hand h = new Hand(suited);
        straightFlush = h.getStraight();

        if(straightFlush!=null) {
            if(straightFlush[0].getCardNum()==9) {
                strength = HandStrength.ROYAL_FLUSH;
            }
            else {
                strength = HandStrength.STRAIGHT_FLUSH;
            }
        }
        return straightFlush;
    }

    public Card[] getStraight() {
        Card[] straight = new Card[5];
        int count = 0;
        int i = frqNums.length-1;
        while(i>-1) {
            //checks if there is at least one of the next number
            if(frqNums[i]>0) {
                count++;
            }
            //restarts if the number is not there
            else {
                count = 0;
            }
            //if 5 consecutive cards have been found
            if(count==5) {
                count = 0;
                //find cards that with numbers 5 higher
                for(int j = 0; j<cardNums.length; j++) {
                    if(cardNums[j] == i) {
                        straight[count] = new Card(i, cardSuits[j]);
                        count++;
                        i++;
                    }
                }
                strength = HandStrength.STRAIGHT;
                return straight;
            }
            i--;
        }
        return null;
    }

    //returns anything else
    public Card[] getElse() {
        int[] freqs = frqNums.clone();
        Arrays.sort(freqs);
        Card[] bestHand = new Card[5];

        int quadLoc = -1, tripLoc = -1, pairLocs[] = {-1,-1}, highLoc = -1, temp;

        for(int i = 0; i<cardNums.length; i++) {
            if(frqNums[cardNums[i]]==4) {
                quadLoc = i;
            }
            else if(frqNums[cardNums[i]]==3) {
                tripLoc = i;
            }
            else if(frqNums[cardNums[i]]==2) {
                pairLocs[1] = pairLocs[0];
                pairLocs[0] = i;
            }
            else if(frqNums[cardNums[i]]==1) {
                highLoc=i;
            }
        }

        //quads
        if(quadLoc!=-1) {
            strength = HandStrength.QUADS;
            for(int i = 0; i<4; i++) {
                bestHand[i] = new Card(cardNums[quadLoc-i], cardSuits[quadLoc-i]);
            }
            bestHand[4] = new Card(cardNums[highLoc], cardSuits[highLoc]);
            return bestHand;
        }

        //trips
        if(tripLoc!=-1) {
            for(int i = 0; i<3; i++) {
                bestHand[i] = new Card(cardNums[tripLoc-i], cardSuits[tripLoc-i]);
            }
            //full house
            if(pairLocs[0]!=-1) {
                int loc = pairLocs[1]==-1? pairLocs[0]:pairLocs[1];
                for(int i = 0; i<2; i++) {
                    bestHand[i+3] = new Card(cardNums[loc-i], cardSuits[loc-i]);
                }
            }

        }

        //quads
        if(freqs[freqs.length-1]==4) {
            strength = HandStrength.QUADS;
            int num = 0;
            while(frqNums[num]!=4) num++;
            int highCard = cardNums.length-1;
            while(cardNums[highCard]%13==num) highCard--;
            return new Card[]{new Card(num, 0), new Card(num, 1), new Card(num, 2), new Card(num, 3), new Card(cardNums[highCard], cardSuits[highCard])};
        }

        //trips
        else if(freqs[freqs.length-1]==3) {
            if(freqs[freqs.length-2]==2 || freqs[freqs.length-3]==2) {
                //full house
                strength = HandStrength.FULL_HOUSE;

            }
        }



        return null;
    }

    public String toString() {
        String str = "";
        for(int i = 0; i<cardNums.length; i++) {
            str+= Card.numToString(cardNums[i]) + " of " + Card.SUITS[cardSuits[i]] + "\n";
        }
        for(int c: frqNums) {
            str += (c + " ");
        }
        str+="\n";
        for(int c: frqSuits) {
            str += c + " ";
        }
        return str;
    }
}