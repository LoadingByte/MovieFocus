
package de.unratedfilms.moviefocus.coremod.main;

import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import de.unratedfilms.moviefocus.coremod.asm.MainClassTransformer;
import de.unratedfilms.moviefocus.shared.Consts;

@IFMLLoadingPlugin.Name (Consts.MOD_ID)
@IFMLLoadingPlugin.MCVersion ("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions ("de.unrated.moviefocus.coremod.")
// Run after the shaders mod
@IFMLLoadingPlugin.SortingIndex (value = 1150)
public class MovieFocusCoremod implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {

        return new String[] { MainClassTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {

        return null;
    }

    @Override
    public String getSetupClass() {

        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {

        return null;
    }

}
