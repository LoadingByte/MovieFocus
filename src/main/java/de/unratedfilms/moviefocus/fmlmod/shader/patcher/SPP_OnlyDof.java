
package de.unratedfilms.moviefocus.fmlmod.shader.patcher;

import static de.unratedfilms.moviefocus.fmlmod.util.MoreStringUtils.insertAfter;
import static de.unratedfilms.moviefocus.fmlmod.util.MoreStringUtils.insertBefore;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import de.unratedfilms.moviefocus.shared.Consts;
import shadersmodcore.client.IShaderPack;

/**
 * Patches versions 1.0 and 1.1 of OnlyDoF by MrY.<br>
 * <br>
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2392600-shader-pack-only-depth-of-field-dof-shaders
 */
public class SPP_OnlyDof implements ShaderPackPatcher {

    @Override
    public boolean isShaderPackSupported(IShaderPack shaderPack) throws IOException {

        try (InputStream readme = shaderPack.getResourceAsStream("/readme.txt")) {
            if (readme != null) {
                String firstLine = IOUtils.lineIterator(readme, Charset.defaultCharset()).nextLine();
                if (firstLine.equals("Only Depth of Field Shader byMrY: https://youtube.com/hdjellybeanlp")) {
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

        // Insert the uniforms on the second line (the first line is the GL version)
        shader = insertAfter(shader, "#version 120", "\n"
                + "uniform int " + Consts.MOD_ID + "_enabled;\n"
                + "uniform float " + Consts.MOD_ID + "_focalDepth;");

        // Replace the call that retrieves the depth from the middle of the screen with a reference to the focalDepth uniform
        shader = shader.replace("getDepth(vec2(0.5, 0.5))", Consts.MOD_ID + "_focalDepth");

        // Only run the DOF if the mod is actually enabled
        shader = insertAfter(shader, "vec4 color = texture2D(composite, texcoord.st);", "\n if (" + Consts.MOD_ID + "_enabled == 1) {");
        shader = insertBefore(shader, "gl_FragColor = color;", "}\n");

        return IOUtils.toInputStream(shader);
    }

}
