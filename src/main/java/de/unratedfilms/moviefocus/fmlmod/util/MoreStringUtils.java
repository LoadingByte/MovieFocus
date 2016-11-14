
package de.unratedfilms.moviefocus.fmlmod.util;

import java.util.List;
import java.util.ListIterator;

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

    public static List<String> trimAll(List<String> strs) {

        for (ListIterator<String> iter = strs.listIterator(); iter.hasNext();) {
            iter.set(iter.next().trim());
        }

        return strs;
    }

    private MoreStringUtils() {}

}
