import java.util.Scanner;

public class Table {

    public String betMsg = """
            [c] - Call
            [k] - Check
            [r] - Raise
            [f] - Fold""";

    //these have to do with the table
    PlayerHand[] players;
    PlayerHand comm;//community cards
    Deck d = new Deck();
    int you; //your place in the table
    int currentPos;
    int startingPos = -1;

    Scanner s = new Scanner(System.in);
    public static final int TABLE_SIZE = 6;

    //these have to do with betting
    public int totalPot;
    public int[] bets = new int[Table.TABLE_SIZE];
    public int[] pot = new int[Table.TABLE_SIZE];

    public int sb; //small blind amount
    public int bb; //big blind amount
    public int minRaise;
    public int toCall;//this is the amount to call

    public boolean canCheck;


    public Table(int numPlayers, int s, int b) {
        players = new PlayerHand[numPlayers];
        sb = s;
        bb = b;
        minRaise = 0;
    }

    public void startGame() {
        PreflopRanges.populateNine();
        PreflopRanges.populateFifteen();
        PreflopRanges.populateTwenty();
        PreflopRanges.populateTwentyFive();
        PreflopRanges.populateThirtyFive();
        PreflopRanges.populateFifty();

        System.out.println("Table Position (1-6): ");
        you = s.nextInt()-1;
        while(true) {

        }
    }

    public void preflop() {
        System.out.println("\nShuffling Deck...");
        d.shuffle();

        //dealing the hands
        dealHands();

        //switching positions
        startingPos++;
        startingPos%=TABLE_SIZE;
        currentPos = startingPos;

        //small blind and big blind bet
        raise(sb, currentPos);
        currentPos++;
        raise(bb, currentPos);
        currentPos++;

        //betting begins
        for(int i = currentPos; i<currentPos+TABLE_SIZE; i++) {

        }
    }

    public void betting() {
        done:
        while (true) {
            if (currentPos == you) {
                //check if you can check
                if(bets[you] == toCall) canCheck = true;
                else canCheck = false;

                action:
                while(true) {
                    if(canCheck) System.out.println("""
            [c] - Call
            [k] - Check
            [r] - Raise
            [f] - Fold""");
                    else System.out.println("""
            [c] - Call
            [r] - Raise
            [f] - Fold""");
                    switch(s.nextLine().toUpperCase()) {
                        case "C":
                            call(currentPos);
                            break action;
                        case "K":
                            check(currentPos);
                            break action;
                        case "F":
                            players[currentPos].fold();
                            break action;
                    }
                }


            }
        }

    }



    public int getTotalPot() {
        totalPot = 0;
        for(int c: pot) {
            totalPot+=c;
        }
        return totalPot;
    }
    public boolean raise(int bet, int pos) {//return false if bet failed
        if(bet>=players[pos].getStack()) {
            allIn(pos);
            return true;
        }
        if(bet<minRaise) {
            System.out.println("Sorry! If you'd like to raise, you must raise at least " + minRaise + ".");
            return false;
        }
        else {
            bets[pos]=bet;
            minRaise = (bet-toCall) + bet; //If you want to reraise, you need to reraise to double the raise
            toCall = bets[pos]; //You MUST call this bet to continue playing
            System.out.println("Position "+ pos + " bet " + bet + ". This is " + ((double)bet/getTotalPot())*100 + "% of the pot");
            return true;
        }
    }
    public void call(int pos) {
        if(toCall>players[pos].getStack()) {
            allIn(pos);
        }
        else {
            bets[pos] = toCall;
            System.out.println("Position " + pos + " calls. This is " + ((double) bets[pos] / getTotalPot()) * 100 + "% of the pot");
        }
    }
    public void check(int pos) {
        if(bets[pos]==toCall) {
            System.out.println("Position " + pos + " checks.");
        }
        else {
            System.out.println("You can't Check!");
        }
    }

    public void collect() {//dealer collects all the bets and adds them to the pot
        for(int i = 0; i<TABLE_SIZE; i++) {
            players[i].bet(bets[i]);
            pot[i]+=bets[i];
        }
    }

    public void allIn(int pos) {
        System.out.println("Position "+ pos + " goes all in for " + players[pos].getStack() +"!");
        minRaise = Math.max(players[pos].getStack(),minRaise);
        bets[pos]=players[pos].getStack();
    }

    public void dealHands() {
        //deal each player 2 cards;
        for(int i = 0; i<2*TABLE_SIZE; i++) {
            players[i%TABLE_SIZE].assignNum(i%TABLE_SIZE);
            players[i%TABLE_SIZE].addToHand(d.deal());
            players[i%TABLE_SIZE].addToHand(d.deal());
        }
    }
}
