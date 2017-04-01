
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import org.apache.commons.lang3.Validate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

@FocusConfig.InternalName ("entity")
public class EntityFocusConfig implements FocusConfig {

    private static final Minecraft MC              = Minecraft.getMinecraft();

    private int                    focusedEntityId;
    private float                  envsphereRadius = 0.5f;

    public EntityFocusConfig() {}

    public EntityFocusConfig(int focusedEntityId, float envsphereRadius) {

        this.focusedEntityId = focusedEntityId;
        this.envsphereRadius = envsphereRadius;
    }

    public int getFocusedEntityId() {

        return focusedEntityId;
    }

    public void setFocusedEntityId(int focusedEntityId) {

        this.focusedEntityId = focusedEntityId;
    }

    public boolean isDescribingExistingFocusedEntity() {

        if (MC.world == null) {
            return false;
        } else {
            Entity focusedEntity = MC.world.getEntityByID(focusedEntityId);
            return focusedEntity != null && focusedEntity.isEntityAlive();
        }
    }

    public Entity getFocusedEntity() {

        Validate.validState(isDescribingExistingFocusedEntity(), "Cannot get the focused entity if the described one with ID %d simply doesn't exist", focusedEntityId);
        return MC.world.getEntityByID(focusedEntityId);
    }

    public float getEnvsphereRadius() {

        return envsphereRadius;
    }

    public void setEnvsphereRadius(float envsphereRadius) {

        this.envsphereRadius = envsphereRadius;
    }

    public Vec3d getEnvsphereCenter() {

        Entity focusedEntity = getFocusedEntity();
        return new Vec3d(focusedEntity.posX, focusedEntity.posY + focusedEntity.getEyeHeight(), focusedEntity.posZ);
    }

    @Override
    public boolean isAvailable() {

        return isDescribingExistingFocusedEntity();
    }

    @Override
    public float getFocalDepth() {

        return (float) GeometryUtils.getDepth(getEnvsphereCenter()) - envsphereRadius;
    }

}
