package com.en.remembrance.utilities;

import com.en.remembrance.configurations.ConfigurationConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class Util {

    public static boolean between(long number, long greaterThan, long lessThan) {
        return number > greaterThan && number < lessThan;
    }

    public static Boolean getConfigBoolean(String configVariable) {
        Boolean configBoolean = ConfigurationConstant.getProperty(configVariable, Boolean.class);
        return configBoolean != null && configBoolean;
    }

    public static String getConfigString(String configVariable) {
        String configString = ConfigurationConstant.getProperty(configVariable, String.class);
        return configString != null ? configString : "";
    }

    public static double convertBytesToMb(long bytes) {
        if (bytes <= 0)
            return 0;

        return bytes / 1024d / 1024d;
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double roundUptoTwo(double value) {
        return round(value, 2);
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);

        if (absB < 1024)
            return bytes + " B";

        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");

        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }

        value *= Long.signum(bytes);
        return String.format("%.1f %cB", value / 1024.0, ci.current());
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomUUIDWithSalt() {
        return getRandomString(8) + getRandomUUID() + getRandomString(8);
    }

    public static String getRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();

        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }

        return salt.toString();
    }

    public static long getDaysMillis(long days) {
        return TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS);
    }

    public static boolean isExpired(int expiryDays, long createdDate) {
        long expiryDaysMillis = getDaysMillis(expiryDays);
        long expiryDate = expiryDaysMillis + createdDate;
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis > expiryDate;
    }

    public static String formatDate(Long dateInMillis) {
        if (dateInMillis != null && dateInMillis > 0) {
            return new SimpleDateFormat("yyyy-MM-dd h:mm a").format(new Date(dateInMillis));
        }

        return null;
    }

}
