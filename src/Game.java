import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    //has to do with table
    public static Scanner s = new Scanner(System.in);
    public static final int NUM_PLAYERS = 6;
    public Deck d;
    public int sb;
    public int bb;
    public int sbPos;
    public int currentPos;
    public int yourPos;

    public boolean canCheck;
    public int toCall;
    public int minRaise;
    public int pot;

    public int[] bets = new int[NUM_PLAYERS];

    ArrayList<Card> comm = new ArrayList<>(); //community cards
    PlayerHand[] players = {new PlayerHand(), new PlayerHand(), new PlayerHand(), new PlayerHand(), new PlayerHand(), new PlayerHand()};
    PokerBot[] bots;
    public Game(int s, int b) {
        d = new Deck();
        sb = s;
        bb = b;
        sbPos = 0;
        PreflopRanges f = new PreflopRanges();
        bots = new PokerBot[f.getRanges().length];
        for(int i = 0; i<f.getRanges().length; i++) {
            bots[i] = new PokerBot(f.getRanges()[i]);
        }
        currentPos = sbPos;
        startGame();


    }

    public void startGame() {
        System.out.println("What position (0-5)");
        yourPos = s.nextInt();
        while(true) {
            resetGame();
            preflop();
        }
    }

    public void preflop() {
        System.out.println("Shuffling...");
        d.shuffle();
        System.out.println("Dealing hands...");
        dealHands();
        System.out.println("Your hand is " );
        players[yourPos].printHand();
        //print all other hands while you're at it
        for(int i = 0; i<players.length; i++) {
            System.out.print(i + "'s hand is: ");
            players[i].printHand();
            System.out.println();
            System.out.println();
        }

        //get all bot decisions
        for(int i = 0; i<NUM_PLAYERS; i++) {
            if(currentPos != yourPos) {
                int idx = (int) (Math.random() * bots.length);
                bots[idx].getDecisionPreFlop(this);
            }
            next();
        }
        toCall = 0;
        //sb and bb bet
        raise(sb);
        raise(bb);
        betRound();
        //deal the flop
        System.out.println("The flop comes...");
        comm.add(d.deal());
        comm.add(d.deal());
        comm.add(d.deal());
        printComm();
        collect();
        System.out.println("The pot is now: " + getPot());
        turn();
    }

    public void turn() {
        resetRound();
        //get all bot decisions
        for(int i = 0; i<NUM_PLAYERS; i++) {
            if(currentPos != yourPos) {
                int idx = (int) (Math.random() * bots.length);
                bots[idx].getDecision(this);
            }
            next();
        }
        betRound();
        //deal the turn
        System.out.println("The turn comes...");
        comm.add(d.deal());
        printComm();
        collect();
        System.out.println("The pot is now: " + getPot());
        river();
    }

    public void river() {
        resetRound();
        //get all bot decisions
        for(int i = 0; i<NUM_PLAYERS; i++) {
            if(currentPos != yourPos) {
                int idx = (int) (Math.random() * bots.length);
                bots[idx].getDecision(this);
            }
            next();
        }
        betRound();

        //deal the river
        System.out.println("The river comes...");
        comm.add(d.deal());
        printComm();
        collect();
        System.out.println("The pot is now: " + getPot());
        ArrayList<Integer> winners = getWinner();
        for(int c: winners) {
            System.out.println("Winners are: " + c);
            players[c].addToStack(pot/winners.size());
        }
    }

    public void betRound() {//goes around the table
        int count = 0;
        while(!canContinue() || count<NUM_PLAYERS) {
            canCheck = bets[currentPos] == toCall; //you can check

            //check if the player is all in or folded, and skip their turn
            if (players[currentPos].isAllIn || players[currentPos].isFold) {
                count++;
                next();
                continue;
            }
            else {
                //check if the current turn is yours
                if (currentPos == yourPos) {
                    int temp = currentPos;
                    actionOnYou();
                    if (temp == currentPos) count--;
                } else {
                    actionOnBot();
                }
            }
            count++;
        }
    }


    public void actionOnYou() {
        //print msg depending on if you can check
        if(canCheck) System.out.println("""
                    [R] - Raise
                    [K] - Check
                    [F] - Fold""");

        else System.out.println("""
                    [R] - Raise
                    [C] - Call
                    [F] - Fold""");

        s.nextLine();
        String str = s.nextLine();
        switch(str.toUpperCase()) {
            case "R":
                System.out.println("How much would you like to raise? The minimum raise is " + minRaise);
                raise(s.nextInt());
                break;
            case "C":
                call();
                break;
            case "K":
                if(canCheck) {
                    check();
                }
                else System.out.println("Can't do that");
                return;
            case "F":
                fold();
                break;
            default:
                System.out.println("Not an option");
        }
    }

    public void actionOnBot() {
        PokerBot.BotAction a = new PokerBot.BotAction();
        a = players[currentPos].getAction();
        if(a==null) {
            call();
            return;
        }
        switch(a.getType()) {
            case "MR":         //first case: MinRaise
                raise(minRaise);
                players[currentPos].nextAction();
                break;
            case "R": //raise
                //check the amount to raise by
                if(a.getAmount()>minRaise) {
                    raise(a.getAmount());
                }
                else {
                    call();
                }
                players[currentPos].nextAction();
                break;
            case "C":
                if(a.getAmount()<toCall) {
                    fold();
                }
                else {
                    call();
                }
                break;
            case "A":
                allIn();
                break;
            case "CA":
                call();
                break;
            default:
                if(canCheck){
                    check();
                }
                else {
                    fold();
                }
                break;
        }
    }
    public boolean canContinue() {
        //check each player's bets and see if they are the same
        for(int i = 0; i<players.length; i++) {
            //if the bet does not match to the call amount AND (the player has not folded OR the player is not all in)
            if(bets[i]!=toCall && !(players[i].isFold || players[i].isAllIn)) {
                return false;
            }
        }
        return true;
    }
    public void raise(int r) {
        //first, check if the raise is possible (minRaise is 2x the last raise)
        if(r<minRaise) {
            System.out.println("Minimum raise is " + minRaise);
            return; //raise failed
        }
        //second, check if the raise is larger than the players stack
        else if(r>players[currentPos].getStack()) {
            //in this case, the player goes all in
            allIn();
        }
        else {
            //otherwise, the raise is valid
            bets[currentPos] = r;
            updateCall(r);
            System.out.println(currentPos + " raises to " + r);
            next(); //successful raise
        }
    }

    public void call() {
        //check first if it is a call or a check
        if(bets[currentPos]==toCall) { //this is just a check
            check();
        }
        else if(toCall>players[currentPos].getStack()) {//all in
            allIn();
        }
        else {
            bets[currentPos] = toCall;
            System.out.println(currentPos + " calls for " + toCall);
            next();
        }
    }

    public void check() {
        //check if you can check
        if(bets[currentPos]==toCall) {
            System.out.println(currentPos + " checks.");
            next();
        }
        else return;
    }
    public void allIn() {
        bets[currentPos] = players[currentPos].getStack();
        players[currentPos].allIn();
        updateCall(bets[currentPos]);
        System.out.println(currentPos + " goes all in for " + players[currentPos].getStack());
        next();
    }

    public void fold() {
        players[currentPos].fold();
        System.out.println(currentPos + " folds");
        next();
    }

    public ArrayList<Integer> getWinner() {
        ArrayList<Integer> maxloc = new ArrayList<>();
        maxloc.add(0);
        TotalHand[] h = new TotalHand[players.length];
        for(int i = 0; i<players.length; i++) {
            //check if they are folded
            if(players[i].isFold) continue;
            //check the strength of each hand
            comm.addAll(players[i].getHand());
            h[i] = new TotalHand(comm);
            if(h[i].compareTo(h[maxloc.getFirst()])>0) {
                maxloc.clear();
                maxloc.add(i);
            }
            else if(h[i].compareTo(h[maxloc.getFirst()])>0) {
                maxloc.add(i);
            }
            comm.removeLast();
            comm.removeLast();
        }
        return maxloc;
    }

    public void collect() {
        //dealer collects all bets and adds them to the pot
        for(int i = 0; i<NUM_PLAYERS; i++) {
            players[i].bet(bets[i]);
            pot+=bets[i];
            bets[i] = 0;
        }
    }

    public void updateCall(int r) {
        //update the calling values and minraise values
        minRaise = toCall + 2*(r-toCall);
        toCall = r;
    }

    public void resetRound() {
        minRaise = bb;
        toCall = 0;
        currentPos = sbPos;
    }

    public void resetGame() {
        minRaise = 0;
        toCall = 0;
        for(int i = 0; i<players.length; i++) {
            players[i].reset();
        }
    }

    public int getPot() {
        return pot;
    }

    public int getBB() {
        return bb;
    }

    public int getMinRaise() {
        return minRaise;
    }

    public ArrayList<Card> getComm() {
        return comm;
    }
    public void next() {//go to next position
        currentPos = (currentPos+1)%NUM_PLAYERS;
    }
    public void printComm() {
        for(Card c: comm){
            System.out.print(c + ", ");
        }
        System.out.println();
    }
    public void dealHands() {
        //deal each player 2 cards;
        for(int i = 0; i<2*NUM_PLAYERS; i++) {
            players[i%NUM_PLAYERS].assignNum(i%NUM_PLAYERS);
            players[i%NUM_PLAYERS].addToHand(d.deal());
        }
    }
}
