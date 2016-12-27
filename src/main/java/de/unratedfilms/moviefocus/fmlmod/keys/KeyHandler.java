
package de.unratedfilms.moviefocus.fmlmod.keys;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusFlowRunner;
import de.unratedfilms.moviefocus.fmlmod.gui.FocusFlowScreen;

public class KeyHandler {

    // We catch keyboard events AND mouse events in case the key has been set to a mouse button
    @SubscribeEvent
    public void onKeyInput(InputEvent event) {

        if (KeyBindings.FLOW.isPressed()) {
            if (FocusFlowRunner.isRunning()) {
                FocusFlowRunner.advance();
            } else {
                Minecraft.getMinecraft().displayGuiScreen(new FocusFlowScreen());
            }
        }
    }

}
