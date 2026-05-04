package Utils;

public class Format {

    /**
     * stretches text to be exactly width characters long, padding with fill equally
     * on both sides. If width is less than text's length, text is shortened to end with '...'
     * @param text the text to center
     * @param width the width of the centered string
     * @param fill the character to pad with
     * @return the centered string
     */
    public static String center(String text, int width, char fill) {
        int totalPaddingAmount = width - text.length();
        if (totalPaddingAmount < 0) {
            //never called with width < 3 but potentially dangerous if reused in another project
            return text.substring(0, width - 3) + "...";
        }

        int leftPaddingAmount = totalPaddingAmount / 2;
        int rightPaddingAmount = totalPaddingAmount - leftPaddingAmount;

        String leftPadding = Character.toString(fill).repeat(leftPaddingAmount);
        String rightPadding = Character.toString(fill).repeat(rightPaddingAmount);

        return leftPadding + text + rightPadding;
    }

    /**
     * centers the given string, padding equally with fill. The result will start and end with border, even if the string
     * is longer than width, truncating if necessary
     * @param string the string to center
     * @param width the width to pad to
     * @param fill the character to pad with
     * @param border the border character to use on either side
     * @return the centered string with the border
     */
    public static String centerWithBorder(String string, int width, char fill, String border) {
        String padded = center(string, width - 2 * border.length(), fill);
        return border + padded + border;
    }

    /**
     * pad text to width by inserting spaces on the left. If text is longer than width, shorten to end with '...'
     * @param text the text to align
     * @param width the width to pad to
     * @return the right-aligned string
     */
    public static String rightAlign(String text, int width) {
        int padding = width - text.length();
        if (padding < 0) {
            // never called with width < 3 but potentially risky if reused in another project
            return text.substring(0, width - 3) + "...";
        }
        if (padding == 0) {
            return text;
        }

        return " ".repeat(padding) + text;
    }

    /**
     * format the given number of seconds as H:MM:SS or MM:SS if hours is 0
     * @return the formatted time
     */
    public static String time(long totalSeconds) {
        int hours = (int) (totalSeconds / 3600);
        int minutes = (int) (totalSeconds / 60) % 60;
        int seconds = (int) (totalSeconds % 60);

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }
}
