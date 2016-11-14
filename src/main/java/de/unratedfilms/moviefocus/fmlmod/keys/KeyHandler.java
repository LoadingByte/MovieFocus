
package de.unratedfilms.moviefocus.fmlmod.keys;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigManager;
import de.unratedfilms.moviefocus.fmlmod.gui.SettingsScreen;

public class KeyHandler {

    // We catch keyboard events AND mouse events in case the key has been set to a mouse button
    @SubscribeEvent
    public void onKeyInput(InputEvent event) {

        if (KeyBindings.openSettingsScreen.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new SettingsScreen(null));
        } else if (KeyBindings.toggleActivity.isPressed()) {
            FocusConfigManager.INSTANCE.setActivated(!FocusConfigManager.INSTANCE.isActivated());
        }
    }

}
