
package de.unratedfilms.moviefocus.fmlmod.shader;

/**
 * This class stores variables that can be used by custom shaders to render the currently configured depth of field effect.
 * Those variables (which define the current focus configuration) are injected into the shaders system as uniform variables.
 */
public class ShaderUniforms {

    /*
     * These variables are public so that the Shaders class can access them quicker (they are used as uniforms).
     * Please don't access them manually. Use the getters and setters instead.
     */
    public static int   enabled;        // boolean
    public static float focalDepth = 1f;

    public static boolean isEnabled() {

        return enabled == 1;
    }

    public static void setEnabled(boolean enabled) {

        ShaderUniforms.enabled = enabled ? 1 : 0;
    }

    public static float getFocalDepth() {

        return focalDepth;
    }

    public static void setFocalDepth(float focalDepth) {

        // The depth mustn't be below 0
        if (focalDepth < 0) {
            focalDepth = 0;
        }

        ShaderUniforms.focalDepth = focalDepth;
    }

    public static void addFocalDepth(float delta) {

        setFocalDepth(focalDepth + delta);
    }

}
