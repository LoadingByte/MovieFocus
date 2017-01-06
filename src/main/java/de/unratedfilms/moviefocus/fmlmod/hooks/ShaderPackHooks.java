
package de.unratedfilms.moviefocus.fmlmod.hooks;

import static de.unratedfilms.moviefocus.shared.Consts.LOGGER;
import java.io.IOException;
import java.io.InputStream;
import java.util.ServiceLoader;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.ShaderPackPatcher;
import de.unratedfilms.moviefocus.shared.Consts;
import shadersmodcore.client.IShaderPack;

public class ShaderPackHooks {

    // shaderPackName is only used for logging purposes and doesn't need to be supplied for the method itself to function.
    public static ShaderPackPatcher findPatcher(String shaderPackName, IShaderPack shaderPack) {

        LOGGER.info("Searching for a shader pack patcher that supports the current shader pack '{}'", shaderPackName);

        for (ShaderPackPatcher patcher : ServiceLoader.load(ShaderPackPatcher.class)) {
            try {
                if (patcher.isShaderPackSupported(shaderPack)) {
                    LOGGER.info("Found patcher: '{}'", patcher.getClass().getName());
                    return patcher;
                }
            } catch (IOException e) {
                LOGGER.error("Error while running shader pack patcher support check with patcher '{}'; continuing with the next patcher nevertheless", patcher.getClass().getName(), e);
            }
        }

        LOGGER.info("No patcher found; either the current shader pack '{}' is not supported, or it has {} support already built into it", shaderPackName, Consts.MOD_NAME);
        return null;
    }

    public static InputStream patchResourceStreamIfPatcherExists(InputStream resourceStream, String resourcePath, ShaderPackPatcher patcher) {

        if (patcher != null) {
            try {
                return patcher.patchResourceStream(resourcePath, resourceStream);
            } catch (IOException e) {
                LOGGER.error("Error while running shader pack patcher '{}' on resource '{}'; returning original unpatched resource instead, maybe it still works ...",
                        patcher.getClass().getName(), resourcePath, e);
            }
        }

        return resourceStream;
    }

    private ShaderPackHooks() {}

}
