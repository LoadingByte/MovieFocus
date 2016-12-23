
package de.unratedfilms.moviefocus.fmlmod.conf;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FocusConfigRegistry {

    // Service loader wasn't really designed for loading classes, but we can take the instances and get their classes to build this list
    private static List<Class<? extends FocusConfig>> focusConfigTypes = StreamSupport.stream(ServiceLoader.load(FocusConfig.class).spliterator(), false)
            .map(FocusConfig::getClass).collect(Collectors.toList());

    public static List<Class<? extends FocusConfig>> getAllTypes() {

        return focusConfigTypes;
    }

    public static FocusConfig newInstance(Class<? extends FocusConfig> focusConfigType) {

        try {
            return focusConfigType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Some programmer decided that it was a good idea to make the class '" + focusConfigType.getName() + "' impossible to construct", e);
        }
    }

    private FocusConfigRegistry() {}

}
