
package de.unratedfilms.moviefocus.fmlmod.conf.impls;

import static de.unratedfilms.moviefocus.shared.Consts.MOD_ID;
import java.lang.ref.WeakReference;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import de.unratedfilms.moviefocus.fmlmod.conf.FocusConfig;
import de.unratedfilms.moviefocus.fmlmod.util.GeometryUtils;

abstract class EntityFocusConfigWithoutGui implements FocusConfig {

    protected WeakReference<Entity> focusedEntity;

    // ----- Properties -----

    @Override
    public String getTitle() {

        return I18n.format("gui." + MOD_ID + ".focusConfigTitle.entity");
    }

    public Entity getFocusedEntity() {

        return focusedEntity.get();
    }

    public boolean hasFocusedEntity() {

        return focusedEntity != null && focusedEntity.get() != null && focusedEntity.get().isEntityAlive();
    }

    public void setFocusedEntity(Entity focusedEntity) {

        this.focusedEntity = new WeakReference<>(focusedEntity);
    }

    // ----- Queries -----

    @Override
    public boolean isAvailable() {

        return hasFocusedEntity();
    }

    @Override
    public float getFocalDepth() {

        Entity focusedEntity = this.focusedEntity.get();
        Vec3 entityPos = Vec3.createVectorHelper(focusedEntity.posX, focusedEntity.posY, focusedEntity.posZ);

        return (float) GeometryUtils.getDepth(entityPos);
    }

}
