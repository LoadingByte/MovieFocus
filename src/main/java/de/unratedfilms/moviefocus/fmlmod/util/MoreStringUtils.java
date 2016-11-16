
package de.unratedfilms.moviefocus.fmlmod.util;

import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoreStringUtils {

    public static int indexBefore(String str, Pattern searchPattern) {

        Matcher matcher = searchPattern.matcher(str);
        matcher.find();
        return matcher.start();
    }

    public static int indexAfter(String str, String searchStr) {

        return str.indexOf(searchStr) + searchStr.length();
    }

    public static int indexAfter(String str, Pattern searchPattern) {

        Matcher matcher = searchPattern.matcher(str);
        matcher.find();
        return matcher.end();
    }

    public static String insert(String str, int index, String insertedStr) {

        return new StringBuilder(str).insert(index, insertedStr).toString();
    }

    public static String insertBefore(String str, String searchStr, String insertedStr) {

        return insert(str, str.indexOf(searchStr), insertedStr);
    }

    public static String insertBefore(String str, Pattern searchPattern, String insertedStr) {

        return insert(str, indexBefore(str, searchPattern), insertedStr);
    }

    public static String insertAfter(String str, String searchStr, String insertedStr) {

        return insert(str, indexAfter(str, searchStr), insertedStr);
    }

    public static String insertAfter(String str, Pattern searchPattern, String insertedStr) {

        return insert(str, indexAfter(str, searchPattern), insertedStr);
    }

    public static List<String> trimAll(List<String> strs) {

        for (ListIterator<String> iter = strs.listIterator(); iter.hasNext();) {
            iter.set(iter.next().trim());
        }

        return strs;
    }

    private MoreStringUtils() {}

}
