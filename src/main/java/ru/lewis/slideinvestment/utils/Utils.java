package ru.lewis.slideinvestment.utils;

import java.util.Random;

public class Utils {

    private static Random random = new Random();

    public static boolean calculateChance(int chance) {
        return random.nextInt(100) <= chance;
    }

    public static String convertSecondsToMinutesAndSeconds(int seconds) {
        int minute = seconds / 60;
        int second = seconds % 60;

        StringBuilder result = new StringBuilder();

        if (minute > 0) {
            result.append(String.format("%02d м. ", minute));
        }

        result.append(String.format("%02d с.", second));

        return result.toString();
    }

}
