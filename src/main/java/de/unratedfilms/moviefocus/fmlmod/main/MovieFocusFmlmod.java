
package de.unratedfilms.moviefocus.fmlmod.main;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyBindings;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyHandler;
import de.unratedfilms.moviefocus.shared.Consts;

@Mod (modid = Consts.MOD_ID, version = Consts.MOD_VERSION)
public class MovieFocusFmlmod {

    @EventHandler
    public void init(FMLInitializationEvent event) {

        // Initialize the key bindings
        KeyBindings.initialize();
        FMLCommonHandler.instance().bus().register(new KeyHandler());
    }

}
