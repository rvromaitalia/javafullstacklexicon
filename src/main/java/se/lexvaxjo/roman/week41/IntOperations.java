package se.lexvaxjo.roman.week41;

public final class IntOperations {

    private  IntOperations(){} //to prevent creating an object

    public static int add( int a, int b ){ return a+b;}
    public static int sub(int a, int b) {return a-b;}
    public static int mult(int a, int b) {return a*b;}
    public static int div(int a, int b){
        if(b!=0)
            return a/b;
        else{
            throw new ArithmeticException("Division by zero is not allowed");

        }
    }
    public static int average(int x, int y, int z){ return (x+y+z)/3;}
}
