
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import java.lang.ref.WeakReference;
import org.apache.commons.lang3.Validate;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfigAdapter;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

@FocusConfig.InternalName ("entity")
public class EntityFocusConfig extends FocusConfigAdapter {

    private WeakReference<Entity> focusedEntity;
    private float                 envsphereRadius = 0.5f;

    public boolean hasFocusedEntity() {

        return focusedEntity != null && focusedEntity.get() != null && focusedEntity.get().isEntityAlive();
    }

    public Entity getFocusedEntity() {

        Validate.validState(hasFocusedEntity(), "Cannot get the focused entity if there isn't one");
        return focusedEntity.get();
    }

    public void setFocusedEntity(Entity focusedEntity) {

        this.focusedEntity = new WeakReference<>(focusedEntity);
    }

    public float getEnvsphereRadius() {

        return envsphereRadius;
    }

    public void setEnvsphereRadius(float envsphereRadius) {

        this.envsphereRadius = envsphereRadius;
    }

    public Vec3d getEnvsphereCenter() {

        Entity focusedEntity = this.focusedEntity.get();
        return new Vec3d(focusedEntity.posX, focusedEntity.posY + focusedEntity.getEyeHeight(), focusedEntity.posZ);
    }

    @Override
    public boolean isAvailable() {

        return hasFocusedEntity();
    }

    @Override
    public float getFocalDepth() {

        return (float) GeometryUtils.getDepth(getEnvsphereCenter()) - envsphereRadius;
    }

}
