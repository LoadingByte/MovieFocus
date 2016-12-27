
package de.unratedfilms.moviefocus.fmlmod.conf;

public abstract class FocusConfigAdapter implements FocusConfig {

    private boolean active;

    protected boolean isActive() {

        return active;
    }

    @Override
    public void setActive(boolean active) {

        this.active = active;
    }

}
