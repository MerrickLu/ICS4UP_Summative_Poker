public class Hand {

    public static enum handStrength {
        ROYAL_FLUSH, STRAIGHT_FLUSH, QUADS, FULL_HOUSE, FLUSH, STRAIGHT, TRIPS, TWO_PAIR, HIGH_CARD
    }

    public int[] cardNums = new int[14]; //Frequency array for cardNumbers **Ace is both 0 and 13
    public int[] cardSuits = new int[4]; //Frequency array for cardSuits

    public Card[] hand = new Card[7];

    //contains 7 cards: the 5 community cards and the 2 player cards
    public Hand(Card[] h) {
        for(int i = 0; i<h.length; i++) {
            hand[i] = h[i];
            cardNums[h[i].getCardNum()]++;
            cardSuits[h[i].getCardSuit()]++;
        }

        //accounting for aces
        cardNums[13] = cardNums[0];
    }

    //check if the hand is a flush
    public handStrength checkHand() {
        return handStrength.ROYAL_FLUSH;
    }

    private boolean isFlush() {

        return false;
    }



}
