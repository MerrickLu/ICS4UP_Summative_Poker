public class Card implements Cloneable{
    // wsg
    public static final String CARDS = "A23456789TJQKA";
    public static final String[] SUITS = {"Diamonds", "Clubs", "Hearts", "Spades"};
    private int cardNum; //number from 0-12
    private int cardSuit; //0 - diamonds, 1 - clubs, 2 - hearts, 3 - spades

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

    public int compare(Card c) {
        if(getCardNum()>c.getCardNum()) {
            return 1;
        }
        else if(getCardNum()<c.getCardNum()) {
            return -1;
        }
        else return 0;
    }


    public String toString() {
        return (numToString(this.getCardNum()) + " of " + SUITS[this.getCardSuit()]);
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }

}
