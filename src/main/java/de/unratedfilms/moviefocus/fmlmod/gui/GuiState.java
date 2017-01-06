
package de.unratedfilms.moviefocus.fmlmod.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiState {

    protected GuiScreen getScreen() {

        return null;
    }

    protected ImmutableList<Object> getEventHandlers() {

        return ImmutableList.of();
    }

    protected void enter() {

    }

    protected void exit() {

    }

}
