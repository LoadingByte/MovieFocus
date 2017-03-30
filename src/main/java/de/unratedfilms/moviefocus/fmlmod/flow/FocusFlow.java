
package de.unratedfilms.moviefocus.fmlmod.flow;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;

public class FocusFlow {

    public static final List<FocusFlowEntry> sequence = new ArrayList<>();

    private FocusFlow() {}

    public static class FocusFlowEntry {

        private String            title = "";
        private final FocusConfig focusConfig;

        public FocusFlowEntry(FocusConfig focusConfig) {

            Validate.notNull(focusConfig, "Focus flow entry config cannot be null");

            this.focusConfig = focusConfig;
        }

        public String getTitle() {

            return title;
        }

        public void setTitle(String title) {

            Validate.notNull(title, "Focus flow entry title cannot be null");
            this.title = title;
        }

        public FocusConfig getFocusConfig() {

            return focusConfig;
        }

    }

}
