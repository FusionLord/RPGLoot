package net.fusionlord.rpgloot.client.rendering;

import com.mojang.authlib.GameProfile;
import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nonnull;
import java.util.UUID;

public class CorpseRenderFactory implements IRenderFactory<EntityCorpse> {

    public CorpseRenderFactory() {
    }

    @Override
    public Render<? super EntityCorpse> createRenderFor(RenderManager manager) {
        return new RenderCorpse(manager);
    }

    private static class RenderCorpse extends Render<EntityCorpse> {

        RenderCorpse(RenderManager renderManager) {
            super(renderManager);
        }

        @Override
        public void doRenderShadowAndFire(@Nonnull Entity corpse, double x, double y, double z, float yaw, float partialTicks) {
            //Don't want shadow or fire
        }

        @Override
        protected ResourceLocation getEntityTexture(@Nonnull EntityCorpse corpse) {
            return null;
        }

        @Override
        public void doRender(@Nonnull EntityCorpse corpse, double x, double y, double z, float whoknows, float partialTicks) {
            try {
                String entClass = corpse.getEntityClass();
                Entity entInstance;
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) x, (float) y, (float) z);
                Class entClazz;
                if (entClass.contains("EntityPlayerMP")) {

                    UUID playerID = new UUID(corpse.getOldEntityData().getLong("UUIDMost"), corpse.getOldEntityData().getLong("UUIDLeast"));
                    entInstance = new EntityOtherPlayerMP(corpse.world, new GameProfile(playerID, ""));
                    entInstance.setSneaking(true);

                } else {
                    entClazz = Class.forName(entClass);
                    entInstance = (Entity) entClazz.getConstructor(World.class).newInstance(corpse.world);

                    if (corpse.getOldEntityData() != null) {
                        entInstance.readFromNBT(corpse.getOldEntityData());
                    }
                }

                GlStateManager.translate(0, (entInstance.getEntityBoundingBox().maxX - entInstance.getEntityBoundingBox().minX) / 2, 0);

                GlStateManager.rotate((int) entInstance.prevRotationYaw, 0F, 1F, 0F);

                if (entInstance.getEntityBoundingBox().maxY > 1.5) {
                    GlStateManager.rotate(-90F, 1F, 0F, 0F);
                }

                if (entInstance instanceof EntitySpider) {
                    GlStateManager.rotate(-90F, 1F, 0F, 0F);
                } else if (entInstance instanceof EntityAnimal || entInstance instanceof EntityAmbientCreature) {
                    GlStateManager.rotate(90F, 0F, 1F, 0F);
                    GlStateManager.translate((float) (entInstance.getEntityBoundingBox().maxZ - entInstance.getEntityBoundingBox().minZ) / 2F, 0F, 0F);
                } else if (entInstance instanceof EntityOtherPlayerMP) {
                    GlStateManager.rotate(180F, 1F, 0F, 0F);
                    GlStateManager.rotate(90F, 0F, 0F, 1F);
                    GlStateManager.translate(0F, 0F, (float) (entInstance.getEntityBoundingBox().maxZ - entInstance.getEntityBoundingBox().minZ) / 2F);
                }

                GlStateManager.translate(0, (float) -(entInstance.getEntityBoundingBox().maxY - entInstance.getEntityBoundingBox().minY) / 2F, 0);
                renderManager.setRenderShadow(false);
                renderManager.renderEntity(entInstance, 0, 0, 0, 0, 0, false);
                GlStateManager.popMatrix();
            } catch (Exception exception) {
                RPGLoot.logger.error(exception);
                GlStateManager.popMatrix();
            }
        }
    }
}
