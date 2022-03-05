package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkHeaderParser {
    private static final String LINK_PATTERN = "<(?<link>.+?)>;\\s+?\\brel=\"\\b" +
            "(?<rel>\\bcurrent\\b|\\bprev\\b|\\bcurr\\b|\\bfirst\\b|\\blast\\b|\\bnext\\b)\"";

    public static boolean hasNext(Map<String, String> link) {
        return link.containsKey("next");
    }
    public static final String LAST_LINK = "";
    public static String getNext(Map<String, String> links) {
        return hasNext(links) ? links.get("next") : "";
    }

    public static Map<String, String> parseLinkHeader(String link) {
        Map<String, String> pagination = new HashMap();
        Pattern pattern = Pattern.compile(LINK_PATTERN);
        Matcher matcher = pattern.matcher(link);

        System.out.println(link);

        while (matcher.find()) {
            pagination.put(matcher.group("rel"), matcher.group("link"));
        }

        return pagination;
    }
}
