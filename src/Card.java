public class Card {

    public static final String CARDS = "123456789TJQK";
    public int cardNum; //number from 1-13
    public int cardSuit; //0 - diamonds, 1 - clubs, 2 - hearts, 3 - spades

    public Card (int num) {
        cardNum = num%13;
        cardSuit = num%4;
    }

    public Card (int num, int s) {
        cardNum = num;
        cardSuit = s;
    }

    public int getCardNum() {
        return cardNum;
    }

    public static String numToString(int num) {
        return String.valueOf(CARDS.charAt(num-1));
    }

    public String getCardSuit() {
        if(cardSuit==0) {
            return "Diamonds";
        }

        else if(cardSuit == 1) {
            return "Clubs";
        }
        else if (cardSuit == 2) {
            return "Hearts";
        }
        else return "Spades";
    }

    public void changeNum(int n) {
        cardNum = n;
    }

    public void changeSuit(String s) {
        switch(s.toUpperCase()) {
            case "DIAMONDS":
                cardSuit = 0;
                break;
            case "CLUBS":
                cardSuit = 1;
                break;
            case "HEARTS":
                cardSuit = 2;
                break;
            case "SPADES":
                cardSuit = 3;
                break;
        }
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
        if(getCardSuit().equals(c.getCardSuit())) {
            arr[1]=0;
        }
        else {
            arr[1]=-1;
        }
        return arr;
    }
}
