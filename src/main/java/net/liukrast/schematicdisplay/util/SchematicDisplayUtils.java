package net.liukrast.schematicdisplay.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class SchematicDisplayUtils {
    private SchematicDisplayUtils() {}

    private static final String[] COUNT_SUFFIXES = new String[] {"k", "m", "b", "t"};
    private static final DecimalFormatSymbols ROOT_LOCALE_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.ROOT);
    private static final DecimalFormat TWO_DIGIT_PRECISION;
    private static final DecimalFormat ONE_DIGIT_PRECISION;

    static {
        TWO_DIGIT_PRECISION = new DecimalFormat("#.00", ROOT_LOCALE_FORMAT_SYMBOLS);
        TWO_DIGIT_PRECISION.setRoundingMode(RoundingMode.DOWN);
        ONE_DIGIT_PRECISION = new DecimalFormat("##.0", ROOT_LOCALE_FORMAT_SYMBOLS);
        ONE_DIGIT_PRECISION.setRoundingMode(RoundingMode.DOWN);
    }

    public static String abbreviate(long count) {
        return abbreviate(count, 4);
    }

    public static String abbreviate(long count, int maxCharacters) {
        if (count < 1000)
            return String.valueOf(count);

        int digits = (int) Math.log10(count) + 1;
        if (digits <= maxCharacters)
            return String.format(Locale.ROOT, "%,d", count);

        int thousandsExponent = ((digits - maxCharacters) / 3) + 1;
        if (thousandsExponent > COUNT_SUFFIXES.length) return "99t+";

        String suffix = COUNT_SUFFIXES[thousandsExponent - 1];
        double divisionResult = count / Math.pow(1000, thousandsExponent);
        int wholeDigits = digits - thousandsExponent * 3;
        int precisionDigits = maxCharacters - 1 - wholeDigits;

        String numberPart = "";
        if (wholeDigits > 3 || precisionDigits == 0)
            numberPart = String.format(Locale.ROOT, "%d", (long) divisionResult);
        else if (precisionDigits == 2)
            numberPart = TWO_DIGIT_PRECISION.format(divisionResult);
        else if (precisionDigits == 1)
            numberPart = ONE_DIGIT_PRECISION.format(divisionResult);
        else
            numberPart = String.format(Locale.ROOT, "%d", (long) divisionResult);

        return numberPart + suffix;
    }
}