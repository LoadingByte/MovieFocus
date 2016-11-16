
package de.unratedfilms.moviefocus.fmlmod.main;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigRegistry;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.EntityFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.FixedFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.impls.PointFocusConfig;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyBindings;
import de.unratedfilms.moviefocus.fmlmod.keys.KeyHandler;
import de.unratedfilms.moviefocus.fmlmod.render.OverlayRenderer;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.ShaderPackPatcherRegistry;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.impls.SPP_Chocapic13sShaders;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.impls.SPP_OnlyDoF;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.impls.SPP_SEUS;
import de.unratedfilms.moviefocus.shared.Consts;

@Mod (modid = Consts.MOD_ID, version = Consts.MOD_VERSION)
public class MovieFocusFmlmod {

    @EventHandler
    public void init(FMLInitializationEvent event) {

        // Register the focus config suppliers
        FocusConfigRegistry.register(FixedFocusConfig::new);
        FocusConfigRegistry.register(PointFocusConfig::new);
        FocusConfigRegistry.register(EntityFocusConfig::new);

        // Register the shader pack patchers
        ShaderPackPatcherRegistry.register(new SPP_OnlyDoF());
        ShaderPackPatcherRegistry.register(new SPP_SEUS());
        ShaderPackPatcherRegistry.register(new SPP_Chocapic13sShaders());

        // Initialize the key bindings
        KeyBindings.initialize();
        FMLCommonHandler.instance().bus().register(new KeyHandler());

        // Initialize the overlay renderer
        MinecraftForge.EVENT_BUS.register(new OverlayRenderer());
    }

}
