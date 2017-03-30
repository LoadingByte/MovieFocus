
package de.unratedfilms.moviefocus.fmlmod.events;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is fired whenever someone wants to know the current focal depth.
 * Anyone who wants to pull focus at that time just needs to listen for the event and then call {@link #supplyFocalDepth(float)} with his value.
 * The first one who does that wins.<br>
 * <br>
 * Note that this mechanism is general an has no knowledge about focus configs or focus flows, so other mods could also be the focus supplier and even bypass most of this mod.
 */
public class FocalDepthRequestEvent extends Event {

    private boolean supplied = false;
    private float   focalDepth;

    public boolean isFocalDepthSupplied() {

        return supplied;
    }

    public float getSuppliedFocalDepth() {

        return focalDepth;
    }

    public void supplyFocalDepth(float focalDepth) {

        if (!supplied) {
            supplied = true;
            this.focalDepth = focalDepth;
        }
    }

}
