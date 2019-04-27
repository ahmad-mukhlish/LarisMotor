package com.yayanheryanto.larismotor.helper;

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


}
