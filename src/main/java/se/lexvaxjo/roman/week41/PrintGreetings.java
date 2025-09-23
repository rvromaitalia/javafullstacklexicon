   /*Ex1
    * Write a Java program to print 'Hello' on screen and then print your name on a separate line.
    * It is uesed for both ex1 and 5 depending on 'isOneLine' parameter value
    * Expected result:
    * Hello,
    *  Ali!
    * */
package se.lexvaxjo.roman.week41;

public final class PrintGreetings {
    private PrintGreetings(){}
    public static String greetMe( String name, boolean isOneLine){
        if(isOneLine)
            return "Hello "  + name ; //ex5
        else
            return "Hello" + "\n" + name ; //ex1
    }
}
