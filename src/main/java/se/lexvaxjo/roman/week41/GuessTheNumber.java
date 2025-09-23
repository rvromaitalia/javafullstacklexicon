package se.lexvaxjo.roman.week41;

/*
As the highest threshold is 500, we always can guess the number
after log2 500 times when n is the min  number of guesses >=
500. IN this case n= 9 as 2pow9 = 512. We need 9 guesses.
numbers to compare: 2, 4, 8, 16, 32, 64, 128, 256, 512
 */


public class GuessTheNumber {
    public enum Result { TOO_SMALL, TOO_BIG, CORRECT };
    private final int low;
    private final int high;

    public GuessTheNumber(int low, int high){
        if(low>=high)
            throw new IllegalArgumentException("Low must be smaller or equal to high");
        this.high = high; // 'high' and 'low' are threshold values for the kept number
        this.low= low;
    }

    int numberGuesser(int keptNumber){
        int myGuess = (this.low  + (this.high - this.low)/2);
        int newInterval = 0;
        do{
            if(myGuess-keptNumber == 1)
                return myGuess-1;
            if (myGuess > keptNumber)
                    newInterval= (keptNumber-myGuess)/2;
            else
                newInterval = (this.high-myGuess)/2; //10-5/2 = 2, (10-7)/2=1, (10-8)/2= 1
            myGuess = myGuess + newInterval;
        }while (myGuess!= keptNumber);
        return myGuess;
    }
}
