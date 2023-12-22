public class Card {
    // wsg
    public static final String CARDS = "A23456789TJQK";
    public static final String[] SUITS = {"Diamonds", "Clubs", "Hearts", "Spades"};
    public int cardNum; //number from 0-12
    public int cardSuit; //0 - diamonds, 1 - clubs, 2 - hearts, 3 - spades

    public Card (int num) {
        cardNum = num/4;
        cardSuit = num%4;
    }

    public Card (int num, int s) {
        cardNum = num;
        cardSuit = s;
    }

    public int getCardNum() {
        return cardNum;
    }

    public int getCardSuit() {
        return cardSuit;
    }

    public static String numToString(int num) {
        return String.valueOf(CARDS.charAt(num));
    }

    public void changeNum(int n) {
        cardNum = n;
    }

    public void changeSuit(int i) {
        cardSuit = i;
    }

    public int[] CompareTo(Card c) {
        int[] arr = new int[2];
        //Comparing the numbers
        if(cardNum == c.getCardNum()){
            arr[0]=0;
        }
        else if(cardNum < c.getCardNum()) {
            arr[0]=-1;
        }
        else if(cardNum>c.getCardNum()) {
            arr[0]=1;
        }
        if(getCardSuit() == (c.getCardSuit())) {
            arr[1]=0;
        }
        else {
            arr[1]=-1;
        }
        return arr;
    }

    public String toString() {
        return (numToString(this.getCardNum()) + " of " + SUITS[this.getCardSuit()]);
    }
}
