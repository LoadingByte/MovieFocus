
package de.unratedfilms.moviefocus.fmlmod.util;

public class MoreStringUtils {

    public static int indexAfter(String str, String searchStr) {

        return str.indexOf(searchStr) + searchStr.length();
    }

    public static String insert(String str, int index, String insertedStr) {

        return new StringBuilder(str).insert(index, insertedStr).toString();
    }

    public static String insertBefore(String str, String searchStr, String insertedStr) {

        return insert(str, str.indexOf(searchStr), insertedStr);
    }

    public static String insertAfter(String str, String searchStr, String insertedStr) {

        return insert(str, indexAfter(str, searchStr), insertedStr);
    }

}
