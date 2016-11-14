
package de.unratedfilms.moviefocus.fmlmod.hooks;

import static de.unratedfilms.moviefocus.shared.Consts.LOGGER;
import java.io.IOException;
import java.io.InputStream;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.ShaderPackPatcher;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.ShaderPackPatcherRegistry;
import de.unratedfilms.moviefocus.shared.Consts;
import shadersmodcore.client.IShaderPack;

public class ShaderPackHooks {

    public static ShaderPackPatcher findPatcher(IShaderPack shaderPack) {

        LOGGER.info("Searching for a shader pack patcher that supports the current shader pack");

        for (ShaderPackPatcher patcher : ShaderPackPatcherRegistry.getAll()) {
            try {
                if (patcher.isShaderPackSupported(shaderPack)) {
                    LOGGER.info("Found patcher: '{}'", patcher.getClass().getName());
                    return patcher;
                }
            } catch (IOException e) {
                LOGGER.error("Error while running shader pack patcher support check with patcher '{}'; continuing with the next patcher nevertheless", patcher.getClass().getName(), e);
            }
        }

        LOGGER.info("No patcher found; either the current shader pack is not supported, or it has {} support already built into it", Consts.MOD_NAME);
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
