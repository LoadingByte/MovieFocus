
package de.unratedfilms.moviefocus.fmlmod.keys;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import de.unratedfilms.moviefocus.shared.Consts;

public class KeyBindings {

    public static KeyBinding toggleActivity   = createKeyBinding("toggleActivity", Keyboard.KEY_G);

    private static KeyBinding createKeyBinding(String name, int key) {

        return new KeyBinding("key." + Consts.MOD_ID + "." + name, key, "key.categories." + Consts.MOD_ID);
    }

    public static void initialize() {

        ClientRegistry.registerKeyBinding(toggleActivity);
    }

}
