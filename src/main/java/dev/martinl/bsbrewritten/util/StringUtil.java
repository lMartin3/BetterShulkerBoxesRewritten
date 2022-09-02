package dev.martinl.bsbrewritten.util;

public class StringUtil {

    public static String convertToPascalCase(String string) {
        StringBuilder resultString = new StringBuilder();
        boolean nextUppercase = false;
        for (int i = 0; i < string.length(); i++) {
            String c = string.substring(i, i + 1);
            if (c.equals("_")) {
                nextUppercase = true;
            } else {
                resultString.append((nextUppercase ? c.toUpperCase() : c.toLowerCase()));
                nextUppercase = false;
            }
        }
        return resultString.toString();
    }

    public static String convertToSnakeCase(String string) {
        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            String c = string.substring(i, i + 1);
            if (c.equals(c.toUpperCase())) {
                resultString.append("_");
            }
            resultString.append(c.toLowerCase());
        }
        return resultString.toString();
    }
}