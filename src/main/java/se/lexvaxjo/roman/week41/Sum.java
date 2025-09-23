package se.lexvaxjo.roman.week41;

/**
 * Holds 2 variables and computes sum of them (return value)
 *
 */
public class Sum {

    private final int tal1;
    private final int tal2;

    public  Sum(int tal1, int tal2){
        this.tal1 = tal1;
        this.tal2 = tal2;
    }

    public  int sumOfTwoNumbers(){
      return  tal1 + tal2;
    }
}
