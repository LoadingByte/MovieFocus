
package de.unratedfilms.moviefocus.fmlmod.shader.patcher;

import static de.unratedfilms.moviefocus.fmlmod.util.MoreStringUtils.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import de.unratedfilms.moviefocus.shared.Consts;
import shadersmodcore.client.IShaderPack;

/**
 * Patches versions 10.0 and 10.1 of SEUS by Sonic Ether.
 * The latest version 11.0 is also supported, but since DoF is disabled there, you have to manually enable it first.<br>
 * <br>
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1280299-sonic-ethers-unbelievable-shaders-v11-0
 */
public class SPP_SEUS implements ShaderPackPatcher {

    /*
     * For some strange reason, Sonic Ether decided to put license headers only in these files.
     * Lucky for us, we now have a way to determine whether a shader is in fact a copy of SEUS.
     */
    private static final String[] FILES_WITH_LICENSE_HEADER = {
            "/shaders/final.fsh",
            "/shaders/composite.fsh", "/shaders/composite1.fsh", "/shaders/composite2.fsh",
            "/shaders/gbuffers_terrain.fsh", "/shaders/gbuffers_entities.fsh"
    };

    @Override
    public boolean isShaderPackSupported(IShaderPack shaderPack) throws IOException {

        List<String> licenseHeader;
        try (InputStream licenseHeaderIn = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Consts.MOD_ID, "texts/spp/seus/license-header.txt")).getInputStream()) {
            licenseHeader = trimAll(IOUtils.readLines(licenseHeaderIn));
        }

        for (String filePath : FILES_WITH_LICENSE_HEADER) {
            try (InputStream fileIn = shaderPack.getResourceAsStream(filePath)) {
                if (fileIn == null) {
                    return false;
                }

                List<String> lines = trimAll(IOUtils.readLines(fileIn));
                if (!licenseHeader.equals(lines.subList(1 /* omit the first line, which contains #version XYZ */, licenseHeader.size() + 1))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public InputStream patchResourceStream(String resourcePath, InputStream resourceStream) throws IOException {

        switch (resourcePath) {
            case "/shaders/final.fsh":
                return patchFinalFragmentShader(resourceStream);
            default:
                return resourceStream;
        }
    }

    private InputStream patchFinalFragmentShader(InputStream resourceStream) throws IOException {

        String shader = IOUtils.toString(resourceStream);

        // Insert the uniforms on the second line (the first line is the GL version)
        shader = insertAfter(shader, "#version 120", "\n"
                + "uniform int " + Consts.MOD_ID + "_enabled;\n"
                + "uniform float " + Consts.MOD_ID + "_focalDepthNormalized;");

        // Replace the call that retrieves the depth from the middle of the screen with a reference to the focalDepth uniform
        shader = shader.replace("float cursorDepth = centerDepthSmooth;", "float cursorDepth = " + Consts.MOD_ID + "_focalDepthNormalized;"); // v10.0, v10.1
        shader = shader.replace("float cursorDepth = 0.0f;", "float cursorDepth = " + Consts.MOD_ID + "_focalDepthNormalized;"); // v11.0

        // Only run the DoF if the mod is actually enabled
        shader = insertBefore(shader, "float cursorDepth =", "\n if (" + Consts.MOD_ID + "_enabled == 1) {");
        shader = insertAfter(shader, "color = col/41;", "}\n");

        return IOUtils.toInputStream(shader);
    }

}
