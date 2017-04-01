
package de.unratedfilms.moviefocus.fmlmod.conf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;

public interface FocusConfig {

    /**
     * Returns the internal name of the given focus config type (i.e. {@code fixed}), or {@code unknown} if it is not specified via an {@link FocusConfig.InternalName} annotation.
     *
     * @param clazz The focus config type whose internal name should be returned.
     * @return The internal name of the given focus config, or {@code unknown} if it is undefined.
     */
    public static String getInternalName(Class<? extends FocusConfig> clazz) {

        if (clazz.isAnnotationPresent(FocusConfig.InternalName.class)) {
            return clazz.getAnnotation(FocusConfig.InternalName.class).value();
        } else {
            return "unknown";
        }
    }

    /**
     * Returns the internal name of this focus config (i.e. {@code fixed}), or {@code unknown} if it is not specified via an {@link FocusConfig.InternalName} annotation.
     * Note that this is just a shortcut for {@code FocusConfig.getName(fc.getClass())}.
     *
     * @return The internal name of this given focus config, or {@code unknown} if it is undefined.
     */
    public default String getInternalName() {

        return getInternalName(getClass());
    }

    /**
     * Returns whether {@link #getFocalDepth()} provides a sensible focal depth value that should be rendered.
     * If this is {@code false}, no DoF effect will be rendered at all.
     * For example, that might be the case if an entity the focus depth calculation has been bound to has died.
     * Note that if this returns {@code false}, {@link #getFocalDepth()} won't be called afterwards, meaning that you can use this method like a validation check.
     *
     * @return Whether a DoF effect should be rendered with the return value from {@link #getFocalDepth()}.
     */
    public boolean isAvailable();

    /**
     * Returns the current <b>linear</b> depth value which should be in focus right now, at this point in time.
     * "Linear" means that the distance is returned in regular units (1 unit = 1 block) and not mapped to {@code [0; 1]}.<br>
     * <br>
     * If external information like the position of the camera in space or the current client world object is needed,
     * the focus config is free to access all sources which contain that information, e.g. {@link ActiveRenderInfo} or {@link Minecraft#world}.
     *
     * @return The <b>linear</b> focused depth.
     */
    public float getFocalDepth();

    @Retention (RetentionPolicy.RUNTIME)
    @Target (ElementType.TYPE)
    @Documented
    public static @interface InternalName {

        String value ();

    }

}
