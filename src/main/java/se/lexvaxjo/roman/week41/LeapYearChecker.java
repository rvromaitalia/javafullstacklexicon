package se.lexvaxjo.roman.week41;

public class LeapYearChecker {
    private LeapYearChecker() {
    }

    /**
     * Returns true if the given year is a leap year in the Gregorian calendar.
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}