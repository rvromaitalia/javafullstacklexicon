package se.lexvaxjo.roman.week41;

import java.awt.desktop.SystemEventListener;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Week41App {
    /**
     * Week 41 runner: invokes the three examples.
     * Keeps main minimal and groups logic into small methods.
     */
    public static void main(String[] args) {  // ✅ main must be inside a class
        Scanner scan = new Scanner(System.in);

        /*Ex1
         * Write a Java program to print 'Hello' on screen and then print your name on a separate line.
         * Expected result:
         * Hello,
         *  Ali!
         * */
        System.out.println(PrintGreetings.greetMe("Roman", false));

        /*Ex2
         *Create a program that takes a year as input from
         * user and print if it’s leap year or not.
         */


        System.out.print("\nEnter a year :");
        int  i = scan.nextInt();
        String consumerNewLine = scan.nextLine();
        if (LeapYearChecker.isLeapYear(i))
            System.out.printf("Year %d is a Leap", i);
        else
            System.out.printf("Year %d is not Leap", i);

        /*Ex3
         *Write a Java program to print the sum/multiplication/division and subtraction of two numbers
         *Expected result:
         *45 + 11 = 56
         *12 * 4 = 48
         *24 / 6 = 4
         *55 – 12 = 43
         */
        System.out.printf("%n%n%d + %d = %d", 45, 11, IntOperations.add(45, 11));
        System.out.printf("%n%d * %d = %d", 12, 4, IntOperations.mult(12, 4));
        System.out.printf("%n%d/%d = %d", 24, 6, IntOperations.div(24, 6));
        System.out.printf("%n%d - %d = %d", 55, 12, IntOperations.sub(55, 13));

        /*Ex4
         * Write a Java program that prints the average of three numbers
         * Expected result:
         * (23 + 11 + 77) / 3 = 37
         */
        System.out.printf("%n%d + %d +%d = %d%n", 23, 11, 77, IntOperations.average(23, 11, 77));

        /*Ex5
         *Create a program that asks user to input
         *  his/her name and store it in a variable
         * instead of having fixed name.
         * Then print
         * ‘Hello username’
         *  where username is
         *  what you got from user as input.
         */
        System.out.print("Enter your name, please: ");
        String userName = scan.nextLine();
        if (userName.isEmpty())
            throw new IllegalArgumentException("User name can not be empty");
        else {
            String greetingsText = PrintGreetings.greetMe(userName, true);
            System.out.println(greetingsText);
            Car car = new Car("Tesla", "YFC388", "Roman Vanoyan", (short) 280, 202000);
            System.out.println(car.carInfo());

            /*Ex8.
             *  Write a program that first generates a random number
             *  between 1 and 500 and stores it into a variable (see the Random class).
             *  Then let the user make a guess for which number it is. If the user types
             *  the correct number, he should be presented with a message (including
             *  the number of guesses he has made). If he types a number that is
             *  greater or smaller than the given number, display either “Your guess
             *  was too small” or “Your guess was too big”. The program should keep
             *  executing until the user input the correct guess.
             */
            Random rnd = new Random();
            int rdNumber = ThreadLocalRandom.current().nextInt(1, 501);//501 exclusive
            System.out.println("Your number before guess is: " + rdNumber);
            GuessTheNumber guessTheNumber = new GuessTheNumber(0, 500);
            System.out.println("My guesssed number is :" + guessTheNumber.numberGuesser(rdNumber));

        }
    }
}
