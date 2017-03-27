
package de.unratedfilms.moviefocus.fmlmod.main;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyBindings;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyHandler;
import de.unratedfilms.moviefocus.shared.Consts;

@Mod (modid = Consts.MOD_ID, version = Consts.MOD_VERSION)
public class MovieFocusFmlmod {

    @EventHandler
    public void init(FMLInitializationEvent event) {

        // Initialize the key bindings
        KeyBindings.initialize();
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
    }

}
