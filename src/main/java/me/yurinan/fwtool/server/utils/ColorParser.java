package me.yurinan.fwtool.server.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author CarmJos, Yurinan
 * @since 2022/2/27 16:44
 */

public class ColorParser {

    public static String parse(String text) {
        text = parseHexColor(text);
        return parseColor(text);
    }

    public static String parseColor(final String text) {
        return text.replaceAll("&", "§").replace("§§", "&");
    }

    private static final Pattern REGEX = Pattern.compile("&\\((&?#[0-9a-fA-F]{6})\\)");

    public static String parseHexColor(String text) {
        Matcher matcher = REGEX.matcher(text);
        while (matcher.find()) {
            String hexColor = text.substring(matcher.start() + 2, matcher.end() - 1);
            hexColor = hexColor.replace("&", "");
            StringBuilder bukkitColorCode = new StringBuilder('§' + "x");
            for (int i = 1; i < hexColor.length(); i++) {
                bukkitColorCode.append('§').append(hexColor.charAt(i));
            }
            text = text.replaceAll("&\\(" + hexColor + "\\)", bukkitColorCode.toString().toLowerCase());
            matcher.reset(text);
        }
        return text;
    }

}
