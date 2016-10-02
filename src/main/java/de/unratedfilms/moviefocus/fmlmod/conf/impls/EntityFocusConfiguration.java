
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import java.lang.ref.WeakReference;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfiguration;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

public class EntityFocusConfiguration implements FocusConfiguration {

    private boolean               enabled;
    private WeakReference<Entity> focusedEntity;

    public Entity getFocusedEntity() {

        return focusedEntity.get();
    }

    public void setFocusedEntity(Entity focusedEntity) {

        this.focusedEntity = new WeakReference<>(focusedEntity);
    }

    @Override
    public boolean isActive() {

        return enabled && focusedEntity != null && focusedEntity.get() != null && focusedEntity.get().isEntityAlive();
    }

    @Override
    public void toggleActivity() {

        enabled = !enabled;
    }

    @Override
    public float getFocalDepth() {

        Entity focusedEntity = this.focusedEntity.get();
        Vec3 entityPos = Vec3.createVectorHelper(focusedEntity.posX, focusedEntity.posY, focusedEntity.posZ);

        return (float) GeometryUtils.getDepth(entityPos);
    }

}
