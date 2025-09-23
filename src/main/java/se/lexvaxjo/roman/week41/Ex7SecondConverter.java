package se.lexvaxjo.roman.week41;

public final class Ex7SecondConverter {
    private static final int secPerHours = 3600;
    private static final int secPerMinute = 60;

    //prevent object creation
    private Ex7SecondConverter(){};

    public static String secConverter(int seconds){
        int hours = seconds/secPerHours;
        int min = (seconds - hours*secPerHours)%secPerMinute;
        int sec = seconds%secPerHours%secPerMinute;
        return hours +":"+ min + ":" + sec;
    }
}
