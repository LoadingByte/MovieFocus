
package de.unratedfilms.moviefocus.fmlmod.util;

import java.util.Collections;
import java.util.List;

public class ListUtils {

    public static enum Direction {

        UP, DOWN;

    }

    public static void moveElementIfPossible(List<?> list, int index, Direction direction) {

        switch (direction) {
            case UP:
                if (index > 0) {
                    Collections.rotate(list.subList(index - 1, index + 1), 1);
                }
                break;
            case DOWN:
                if (index < list.size() - 1) {
                    Collections.rotate(list.subList(index, index + 2), 1);
                }
                break;
        }
    }

    private ListUtils() {}

}
