
package de.unratedfilms.moviefocus.fmlmod.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FocusConfigRegistry {

    private static List<Supplier<FocusConfig>> suppliers = new ArrayList<>();

    public static List<FocusConfig> createAll() {

        List<FocusConfig> instances = new ArrayList<>();
        for (Supplier<FocusConfig> supplier : suppliers) {
            instances.add(supplier.get());
        }

        return instances;
    }

    public static void register(Supplier<FocusConfig> focusConfigSupplier) {

        suppliers.add(focusConfigSupplier);
    }

    private FocusConfigRegistry() {}

}
