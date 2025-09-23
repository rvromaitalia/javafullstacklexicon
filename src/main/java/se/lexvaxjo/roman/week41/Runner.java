package se.lexvaxjo.roman.week41;

import java.util.Random;
import java.util.Scanner;


/**
 * Week 41 runner: invokes the three examples.
 * Keeps main minimal and groups logic into small methods.
 */
public class Runner {   // ✅ Added class declaration

    public static void main(String[] args) {  // ✅ main must be inside a class
          Scanner scan = new Scanner(System.in);
//
//        // Example 1:
//        //  *Create a string with your name and printout to the console
//        //  *Create 2 strings. Print out strings with " " separating them
//        Person person = new Person();
//        person.age = 12;
//        person.name = "Roman";
//        person.surname = "Vanoyan";
//        person.nOfChildren = 2;
//        person.ReturnPersonData();
//
//        // Example 2: Create a Car object and display its information
//        System.out.println("Example 2: Create a Car object and display its information");
//        Car car = new Car("Tesla","Magnus" ,"YC344", (byte) 127, (short) 20000);
//        car.carInfo();
//
//        // Example 3: Read the user input for "score" and
//        // make decision according to the condition tree.
//        // OBS: Invalid inputs are not handled here — that is outside the scope.
//        System.out.println("\nExample 3:  Read the user input for \"score\" and\n" +
//                "        // make decision according to the condition tree.");
//
//
//        System.out.print("Enter your score : ");
//        int score = scan.nextInt();
//        System.out.print("Enter your name : ");
//        String name = scan.next();
//
//        if (score >= 65)
//            System.out.printf("Hurrah, you passed, %s!%n", name);
//        else if (score >= 55)
//            System.out.printf("You are almost there %s!%n", name);
//        else if (score >= 0)
//            System.out.printf("Sorry you didn't pass %s!%n", name);
//        else
//            System.out.println("Invalid score or name");
//
//        // Example 4: Read the user input for 2 variables and calculate
//        // sum in the third one
//        System.out.println("\nExample 4:  Read the user input for first and second " +
//                "numbers  and store the sum in the third variable  by printing it");
//        System.out.print("\n Please enter the first number: ");
//        int a = scan.nextInt();
//        System.out.print("Please enter the second number: ");
//        int b = scan.nextInt();
//        Sum sum = new Sum(a,b);
//        int result = sum.sumOfTwoNumbers();
//        System.out.printf("Sum of numbers %d and %d is: %d", a,b, result);
  /*      Create a program that takes a year as input from user and print if it’s leap year or not.

                Write a Java program to print the sum/multiplication/division and subtraction of two numbers
        Expected result:
        45 + 11 = 56
        12 * 4 = 48
        24 / 6 = 4
        55 – 12 = 43

        Write a Java program that prints the average of three numbers
        Expected result:
        (23 + 11 + 77) / 3 = 37

        Create a program that asks user to input his/her name and store it in a variable instead of having fixed name. Then print ‘Hello username’ where username is what you got from user as input.

        Create a program that asks user to input two numbers and print the sum/multiplication/division and subtraction of given numbers.

                Create a program that converts seconds to hours, minutes and seconds
        Input seconds: 86399
        */

        //Final exec riced of week 41
//
//        //Ex3
//        //Addition: print 45+11=56
//        System.out.printf("%n%d+%d = %d",45,11, IntOperations.add(45,11));
//        //Multiplication: print 12x4=48
//        System.out.printf("%n%d*%d=%d", 12,4,IntOperations.mult(12,4));
//        //Division: print 24/6=4
//        System.out.printf("%n%d/%d=%d", 24,6,IntOperations.div(24,6));
//        //Subtraction: print 55-12=43
//        int c = 55, d= 12, e;
//        e = IntOperations.sub(c,d);
//        System.out.printf("%n%d+%d = %d",c,d,e);
//        //ex4
//        System.out.printf("%n%d+%d+%d=%d", 23,11, 77, IntOperations.average(23,11,77));
//        //ex5
//        System.out.print("\nEnter your name and press 'Enter':");
//        String name = scan.next();
//        System.out.printf("Hello %s.", name);
//        /*EX6
//        * Create a program that asks user to input two numbers and print
//        * the sum/multiplication/division and subtraction of given numbers
//        */
//        System.out.print("\nEnter teh first number and press 'Enter'");
//        int number1 = scan.nextInt();
//        System.out.print("\nEnter the second number and press 'Enter'");
//        int number2 = scan.nextInt();
//        System.out.printf("%n%d+%d=%d", number1,number2, number1+ number2);
//        System.out.printf("%n%d*%d=%d", number1,number2, number1* number2);
//        System.out.printf("%n%d/%d=%d", number1,number2, IntOperations.div(number1,number2)); //Handled division by 90 case
//        System.out.printf("%n%d-%d=%d", number1,number2, number1- number2);

        /*Ex7
         * Create a program that converts seconds to hours, minutes and seconds
         *  Input seconds: 86399
         *  Expected output:
         *  23:59:59
         */
        //System.out.println(Ex7SecondConverter.secConverter(86399));

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
        int rdNumber = rnd.nextInt(501);
        System.out.println("Your number before guess is: " + rdNumber);
        GuessTheNumber guessTheNumber = new GuessTheNumber(0,500);
        int keptNumber = guessTheNumber.numberGuesser(rdNumber);
        System.out.println("Your number is: " + keptNumber);
    }
}

