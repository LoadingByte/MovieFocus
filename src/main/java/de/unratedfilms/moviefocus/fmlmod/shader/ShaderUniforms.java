
package de.unratedfilms.moviefocus.fmlmod.shader;

import net.minecraft.client.Minecraft;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfiguration;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigurationManager;

/**
 * This class acts as an adapter layer between the frontend {@link FocusConfiguration} and the backend shader objects.
 * The {@link shadersmodcore.client.Shaders} class uses the fields from this adapter to get the values it should inject into the shader objects as uniform variables.
 */
public class ShaderUniforms {

    /*
     * These variables are public so that the Shaders class can access them quicker (they are used as uniforms).
     */

    public static int   enabled;             // boolean

    public static float focalDepthLinear;
    public static float focalDepthNormalized;

    public static void refreshUniforms() {

        // If the game hasn't started yet or is not in playing mode at the moment, uniform refreshes could potentially lead to crashes
        if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().theWorld == null) {
            return;
        }

        enabled = FocusConfigurationManager.getCurrentConfiguration().isActive() ? 1 : 0;

        if (enabled == 1) {
            refreshFocalDepthLinear();
            refreshFocalDepthNormalized();
        }
    }

    private static void refreshFocalDepthLinear() {

        focalDepthLinear = FocusConfigurationManager.getCurrentConfiguration().getFocalDepth();
        focalDepthLinear = focalDepthLinear < 0.1f ? 0.1f : focalDepthLinear; // the focal depth mustn't be below 0
    }

    private static void refreshFocalDepthNormalized() {

        // These two sizes are actually taken directly from the shadersmodcore.client.Shaders class
        float near = 0.05f;
        float far = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16;

        /*
         * You can convert from normalized to linear depth using this widely-known formula:
         * l = (2 * n * f) / (f + n - (2 * d - 1) * (f - n))
         * with n = near clipping plane, f = far clipping plane, d = normalized depth, l = linear depth
         *
         * By solving the equation for d, we get the following formula back:
         */
        focalDepthNormalized = far * (focalDepthLinear - near) / (focalDepthLinear * (far - near));
    }

}
