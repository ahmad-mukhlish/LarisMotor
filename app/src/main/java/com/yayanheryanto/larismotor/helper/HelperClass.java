package com.yayanheryanto.larismotor.helper;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class HelperClass {

    public static String formatter(String input) {
        if (!input.isEmpty()) {
            DecimalFormatSymbols symbol = new DecimalFormatSymbols();
            symbol.setGroupingSeparator('.');

            DecimalFormat format = new DecimalFormat("Rp ###,###");
            format.setDecimalFormatSymbols(symbol);

            return format.format(Double.parseDouble(input));
        } else {
            return "";
        }

    }

    public static String createDot(String input) {
        if (!(input == null)) {
            DecimalFormatSymbols symbol = new DecimalFormatSymbols();
            symbol.setGroupingSeparator('.');

            DecimalFormat format = new DecimalFormat("###,###");
            format.setDecimalFormatSymbols(symbol);

            Log.v("cikk", format.format(Double.parseDouble(input)));


            return format.format(Double.parseDouble(input));

        } else {
            return "";
        }

    }


    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    public static String clearDot(String input) {

        input = input.replace(".", "");
        Log.v("cik", input);
        return input;
    }

    public static String clearDash(String input) {

        if (!input.isEmpty()) {
            input = input.replace("-", "");
            Log.v("cik", input);
        } else {
            return "";
        }

        return input;

    }


}
