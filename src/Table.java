//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class Table {
//
//    //these have to do with the table
//    PlayerHand[] players;
//    PlayerHand comm;//community cards
//    Deck d = new Deck();
//    int you; //your place in the table
//    int currentPos;
//    int startingPos = -1;
//
//    Scanner s = new Scanner(System.in);
//    public static final int TABLE_SIZE = 6;
//
//    //these have to do with betting
//    public int totalPot;
//    public int[] bets = new int[Table.TABLE_SIZE];
//    public int[] pot = new int[Table.TABLE_SIZE];
//    public int sb; //small blind amount
//    public int bb; //big blind amount
//    public int minRaise;
//    public int toCall;//this is the amount to call
//    public boolean canCheck;
//
//    //These are the poker bots
//    PokerBot[] bots;
//    PreflopRanges ranges = new PreflopRanges();
//
//
//    public Table(int numPlayers, int s, int b) {
//        players = new PlayerHand[numPlayers];
//        comm = new PlayerHand();
//        for(int i = 0; i<players.length; i++) {
//            players[i] = new PlayerHand();
//        }
//        sb = s;
//        bb = b;
//        minRaise = 0;
//        bots = new PokerBot[ranges.getRanges().length];
//        for(int i = 0; i<ranges.getRanges().length; i++) {
//            bots[i] = new PokerBot(ranges.getRanges()[i]);
//        }
//    }
//
//    public void startGame() {
//        System.out.println("Table Position (1-6): ");
//        you = s.nextInt()-1;
//        while(true) {
//            preflop();
//        }
//    }
//
//    public void preflop() {
//        System.out.println("\nShuffling Deck...");
//        d.shuffle();
//
//        //dealing the hands
//        dealHands();
//
//        //switching positions
//        startingPos++;
//        startingPos%=TABLE_SIZE;
//        currentPos = startingPos;
//
//        //small blind and big blind bet
//        raise(sb, currentPos);
//        currentPos++;
//        raise(bb, currentPos);
//        currentPos++;
//
//        //betting begins
//        betting();
//        collect();
//        System.out.println("The Flop comes: ");
//        comm.addToHand(d.deal());
//        comm.addToHand(d.deal());
//        comm.addToHand(d.deal());
//        for(Card c: comm.getHand()) System.out.println(c);
//        turn();
//
//    }
//
//    public void turn() {
//        currentPos = startingPos;
//        betting();
//        collect();
//        comm.addToHand(d.deal());
//        for(Card c: comm.getHand()) System.out.println(c);
//        river();
//    }
//
//    public void river() {
//        currentPos = startingPos;
//        betting();
//        collect();
//        comm.addToHand(d.deal());
//        for(Card c: comm.getHand()) System.out.println(c);
//        System.out.println("Winner: " + getWinner());
//        ArrayList<Card> c = new ArrayList<>(comm.getHand());
//        c.addAll(players[getWinner().getFirst()].getHand());
//        TotalHand winningHand = new TotalHand(c);
//
//        System.out.println("Winning hand is: " + winningHand.getBestHand());
//        System.out.println("Strength is: " + winningHand.getStrength());
//    }
//
//    public void betting() {
//        int count = (currentPos-startingPos);
//        done:
//        while (true) {
//            if(players[currentPos].isFold || players[currentPos].isAllIn) {
//                currentPos = (currentPos+1)%TABLE_SIZE;
//                continue;
//            }
//            if(bets[currentPos] == toCall) canCheck = true;
//            else canCheck = false;
//
//            if (currentPos == you) {
//                //check if you can check
//                actionOnYou();
//            }
//            else {
//                actionOnBot();
//            }
//            //check if we can continue
//            boolean roundDone = true;
//            count++;
//            for(int i = 0; i<TABLE_SIZE; i++) {
//                if((bets[i]!=toCall && (!players[i].isFold && !players[i].isAllIn)) || count<TABLE_SIZE) {
//                    roundDone = false;
//                    break;
//                }
//            }
//            if(roundDone) break done;
//            currentPos = (currentPos+1)%TABLE_SIZE;
//        }
//
//    }
//
//    public void actionOnYou() {
//        if(canCheck) System.out.println("""
//            [c] - Call
//            [k] - Check
//            [r] - Raise
//            [f] - Fold""");
//        else System.out.println("""
//            [c] - Call
//            [r] - Raise
//            [f] - Fold""");
//        s.nextLine();
//        String str = s.nextLine().toUpperCase();
//        switch(str) {
//            case "C":
//                call(currentPos);
//                return;
//            case "K":
//                if(canCheck) {
//                    check(currentPos);
//                    return;
//                }
//                else {
//                    System.out.println("You cannot Check right now. Call or Raise. ");
//                    break;
//                }
//            case "F":
//                fold(currentPos);
//                return;
//            case "R":
//                System.out.println("The minimum raise is " + minRaise
//                        + "\nHow much would you like to raise?");
//                try {
//                    int r = s.nextInt();
//                    if(raise(r, currentPos)) {
//                        return;
//                    }
//                    else {
//                        break;
//                    }
//                } catch(Exception e) {
//                    System.out.println("Please enter an integer between " + Math.min(minRaise,players[currentPos].getStack()) + " and " + players[currentPos].getStack());
//                    break;
//                }
//        }
//        actionOnYou();
//    }
//
//    public void actionOnBot() {
//        String str;
//        int idx = (int)(Math.random()*bots.length);
//        bots[idx].setHand(players[currentPos].getHand());
//
//        if(comm==null) {
//            str = bots[idx].getDecision();
//        }
//        else {
//
//            str = bots[idx].getDecision(comm.getHand());
//        }
//        switch(str) {
//            case "R3":
//                if((((minRaise-toCall)*3)+toCall)>(players[currentPos].getStack())/2) {
//                call(currentPos);
//            }
//                raise(((minRaise-toCall)*3)+toCall, currentPos);
//                return;
//            case "R2":
//                if((((minRaise-toCall)*3)+toCall)>(players[currentPos].getStack())/4) {
//                    call(currentPos);
//                }
//                raise(((minRaise-toCall)*2)+toCall, currentPos);
//                return;
//            case "R":
//                if((((minRaise-toCall)*3)+toCall)>(players[currentPos].getStack())/10) {
//                    call(currentPos);
//                }
//                raise(minRaise, currentPos);
//                return;
//            case "C":
//                call(currentPos);
//                return;
//            case "K":
//                if(canCheck) check(currentPos);
//                else fold(currentPos);
//                return;
//        }
//    }
//
//
//
//    public int getTotalPot() {
//        totalPot = 0;
//        for(int c: pot) {
//            totalPot+=c;
//        }
//        return totalPot;
//    }
//    public boolean raise(int bet, int pos) {//return false if bet failed
//        if(bet>=players[pos].getStack()) {
//            allIn(pos);
//            System.out.println("Player " + currentPos + " goes ALLIN!");
//            return true;
//        }
//        if(bet<minRaise) {
//            System.out.println("Sorry! If you'd like to raise, you must raise at least " + minRaise + ".");
//            return false;
//        }
//        else {
//            bets[pos]=bet;
//            minRaise = (bet-toCall) + bet; //If you want to reraise, you need to reraise to double the raise
//            toCall = bets[pos]; //You MUST call this bet to continue playing
//            System.out.println("Position "+ pos + " bet " + bet + ". This is " + ((double)bet/getTotalPot())*100 + "% of the pot");
//            return true;
//        }
//    }
//    public void call(int pos) {
//        if(toCall>players[pos].getStack()) {
//            allIn(pos);
//        }
//        else {
//            if(bets[pos]==toCall) {
//                check(pos);
//                return;
//            }
//            else {
//                bets[pos] = toCall;
//                System.out.println("Position " + pos + " calls. This is " + ((double) bets[pos] / getTotalPot()) * 100 + "% of the pot");
//            }
//        }
//    }
//    public void check(int pos) {
//        if(bets[pos]==toCall) {
//            System.out.println("Position " + pos + " checks.");
//        }
//        else {
//            System.out.println("You can't Check!");
//        }
//    }
//
//    public void collect() {//dealer collects all the bets and adds them to the pot
//        for(int i = 0; i<TABLE_SIZE; i++) {
//            players[i].bet(bets[i]);
//            pot[i]+=bets[i];
//            bets[i] = 0;
//        }
//        toCall = 0;
//        minRaise = 0;
//    }
//
//    public void allIn(int pos) {
//        System.out.println("Position "+ pos + " goes all in for " + players[pos].getStack() +"!");
//        minRaise = Math.max(players[pos].getStack(),minRaise);
//        bets[pos]=players[pos].getStack();
//        players[pos].allIn();
//    }
//
//    public void dealHands() {
//        //deal each player 2 cards;
//        for(int i = 0; i<2*TABLE_SIZE; i++) {
//            players[i%TABLE_SIZE].assignNum(i%TABLE_SIZE);
//            players[i%TABLE_SIZE].addToHand(d.deal());
//        }
//    }
//
//    public ArrayList<Integer> getWinner() {
//        TotalHand[] arr = new TotalHand[TABLE_SIZE];
//        ArrayList<Card> c = new ArrayList<>();
//        c.addAll(comm.getHand());
//        ArrayList<Integer> winner = new ArrayList<>();
//        winner.add(0);
//        for(int i = 0; i<TABLE_SIZE; i++) {
//            c.addAll(players[i].getHand());
//            arr[i] = new TotalHand(c);
//            if(arr[i].getStrength().compareTo(arr[winner.getFirst()].getStrength())>0) {
//                winner.clear();
//                winner.add(i);
//            }
//            else if(arr[i].getStrength().compareTo(arr[winner.getFirst()].getStrength())==0) {
//                winner.add(i);
//            }
//            c.removeLast();
//            c.removeLast();
//        }
//        return winner;
//    }
//
//    public void fold(int pos) {
//        players[pos].fold();
//        System.out.println("Player " + pos + " folds.");
//    }
//}
