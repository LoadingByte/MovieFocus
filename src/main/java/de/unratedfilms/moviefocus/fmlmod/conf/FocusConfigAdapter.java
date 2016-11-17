
package de.unratedfilms.moviefocus.fmlmod.conf;

public abstract class FocusConfigAdapter implements FocusConfig {

    private boolean selected;
    private boolean activated;

    protected boolean isSelected() {

        return selected;
    }

    protected boolean isActivated() {

        return activated;
    }

    @Override
    public void setStatus(boolean selected, boolean activated) {

        this.selected = selected;
        this.activated = activated;
    }

}
