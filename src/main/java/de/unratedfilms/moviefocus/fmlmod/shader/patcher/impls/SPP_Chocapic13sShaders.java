
package de.unratedfilms.moviefocus.fmlmod.shader.patcher.impls;

import static de.unratedfilms.moviefocus.fmlmod.util.MoreStringUtils.insert;
import static de.unratedfilms.moviefocus.fmlmod.util.MoreStringUtils.insertBefore;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.ShaderPackPatcher;
import de.unratedfilms.moviefocus.shared.Consts;
import shadersmodcore.client.IShaderPack;

/**
 * Patches version 6 of Chocapic13's Shaders by Chocapic13.
 * Note that derived shader packs like SFLP and Lagless Shaders are detected, and then probably, but not guaranteed to be supported.<br>
 * <br>
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1293898-chocapic13s-shaders
 */
public class SPP_Chocapic13sShaders implements ShaderPackPatcher {

    @Override
    public boolean isShaderPackSupported(IShaderPack shaderPack) throws IOException {

        try (InputStream finalFshIn = shaderPack.getResourceAsStream("/shaders/final.fsh")) {
            if (finalFshIn != null) {
                String text = IOUtils.toString(finalFshIn);
                if (text.contains("This code is from Chocapic13' shaders") && text.contains("#define DOF") /* some versions don't even contain the DOF code */) {
                    return true;
                }
            }
        }

        return false;
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

        // Insert the uniforms just before the first comment starts (other typical places like "#version" are not consistent across derivatives)
        shader = insertBefore(shader, "/*",
                ""
                        + "uniform int " + Consts.MOD_ID + "_enabled;\n"
                        + "uniform float " + Consts.MOD_ID + "_focalDepthNormalized;"
                        + "\n");

        // Uncomment #define DOF, so that the DoF is actually enabled
        shader = shader.replace("//#define DOF", "#define DOF");

        // Replace the call that retrieves the depth from the middle of the screen with a reference to the focalDepth uniform
        shader = shader.replace("ld(texture2D(depthtex0, vec2(0.5)).r)*far", "ld(" + Consts.MOD_ID + "_focalDepthNormalized)*far");

        // Only run the DoF if the mod is actually enabled
        int openIdx = shader.indexOf("float z =");
        shader = insert(shader, openIdx, "if (" + Consts.MOD_ID + "_enabled == 1) {\n");

        // Close the if statement from above
        // Because there's no other reference point, we have to check the #if(n)def and #endif balance until we arrive at the insertion point
        int idx = openIdx;
        int stack = 0;
        while (stack >= 0) {
            idx++;
            if (shader.substring(idx, idx + "#ifdef".length()).equals("#ifdef")) {
                stack++;
            } else if (shader.substring(idx, idx + "#ifndef".length()).equals("#ifndef")) {
                stack++;
            } else if (shader.substring(idx, idx + "#endif".length()).equals("#endif")) {
                stack--;
            }
        }
        shader = insert(shader, idx, "}\n");

        return IOUtils.toInputStream(shader);
    }

}
