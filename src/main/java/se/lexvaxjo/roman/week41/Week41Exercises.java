package se.lexvaxjo.roman.week41;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.util.Scanner;
import java.util.Random;
public class Week41Exercises {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        /*Ex1
         * Write a Java program to print 'Hello' on screen and then print your name on a separate line.
         * Expected result:
         * Hello,
         *  Ali!
         * */

        System.out.println("Hello\nAli!");
        //System.out.printf("Hello%nAli!");

        /*Ex2
         *Create a program that takes a year as input from
         * user and print if it’s leap year or not.
         * */

        System.out.print("\nEnter a year to check: ");
        int year = scan.nextInt();
        scan.nextLine();                 // <-- consume the pending newline her

        if ((year % 4 == 0 && year%100!=0) || year %400 ==0)
            System.out.printf("Year '%d' is a leap year!%n", year);
        else
            System.out.printf("Year '%d' is not a leap year!%n", year);

         /*Ex3
         *Write a Java program to print the sum/multiplication/division and subtraction of two numbers
         *Expected result:
         *45 + 11 = 56
         *12 * 4 = 48
         *24 / 6 = 4
         *55 – 12 = 43
         * */

        System.out.printf("%n%d+%d=%d%n",45,11,45+11);
        System.out.printf("%d*%d=%d%n",12,4,12*4);
        System.out.printf("%d/%d=%d%n",24,6,24/6);
        System.out.println("55-12=" + (55-12));

        /*Ex4
        * Write a Java program that prints the average of three numbers
        * Expected result:
        * (23 + 11 + 77) / 3 = 37
        * */

        int a = 23, b = 11, c = 77, average;
        average = (23 + 11 +77)/3;
        System.out.printf("%n(%d+%d+%d)/3=%d",a,b,c,average);

        /*Ex5
          *Create a program that asks user to input
          *  his/her name and store it in a variable
          * instead of having fixed name.
          * Then print
          * ‘Hello username’
          *  where username is
          *  what you got from user as input.
          * */

        System.out.print("\n\nEnter your name please :");
        String name = scan.nextLine();
        System.out.printf("Hello %s%n",name);

        /*Ex6
         *Create a program that asks user to
         * input two numbers and print the
         * sum/multiplication/division and subtraction
         * of given numbers.
         * */

        System.out.print("\nEnter the first number :");
        int x = scan.nextInt();
        System.out.print("Enter the second number :");
        int y = scan.nextInt();
        System.out.printf("%d + %d = %d", x,y, x+y);
        System.out.printf("%n%d * %d = %d", x,y, x*y);
        if(y!=0)
            System.out.printf("%n%d / %d = %d", x,y, x/y);
        else
            System.out.println("\nDivision by 0 is not allowed");
        System.out.printf("%n%d - %d = %d%n", x,y, x-y);

        /*Ex7
         *Create a program that converts seconds to
         *  hours, minutes and seconds
         *Input seconds: 86399
         *Expected output: 23:59:59
         **/

        System.out.print("\nEnter seconds to be converted: ");
        int seconds = scan.nextInt();
        int hours = seconds/3600;
        int min = seconds%3600/60;
        int sec = seconds%3600%60;
        System.out.printf("%d:%d:%d%n", hours, min,sec);

        /*Ex8
         *Write a program that first generates a random
         *  number between 1 and 500 and stores it into a variable
         *  (see the Random class). Then let the user make a guess
         * for which number it is. If the user types the correct number,
         * he should be presented with a message (including the number of
         *  guesses he has made). If he types a number that is greater or
         * smaller than the given number, display either “Your guess was
         * too small” or “Your guess was too big”. The program should keep
         * executing until the user input the correct guess.
        * */
        Random random = new Random();
        int storedNumber = random.nextInt(501);
        System.out.printf("%nYour stored number is :%d%n", storedNumber);
        int guess = 250;
        int nbrOfGuess = 0;
        do {
            if (guess > storedNumber) {
                System.out.println("Your guess was too big");
                guess--;
                nbrOfGuess++;
            }
            else{
                System.out.println("Your guess was too small");
                guess++;
                nbrOfGuess++;
            };
        }while (guess != storedNumber);
        System.out.printf("%nYour guessed the number in '%d' tries!", nbrOfGuess );
        }
    }

