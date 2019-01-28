package net.fusionlord.rpgloot.client.events;

import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.client.gui.GuiMobsScreen;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RPGLoot.MODID)
public class ClientEvents {
    private static Minecraft mcClient;

    @SubscribeEvent
    public static void drawLootIcon(RenderGameOverlayEvent event) {
        if (mcClient == null) mcClient = Minecraft.getMinecraft();
        if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS || mcClient.currentScreen != null) return;

        TextureManager tm = mcClient.getTextureManager();
        EntityPlayerSP player = mcClient.player;
        World world = mcClient.world;

        if (player == null || world == null) return;
        RayTraceResult target = mcClient.objectMouseOver;
        if (target != null && target.entityHit instanceof EntityCorpse) {
            EntityCorpse corpse = (EntityCorpse) target.entityHit;
            ScaledResolution scale = new ScaledResolution(mcClient);
            int width = scale.getScaledWidth();
            int height = scale.getScaledHeight();

            if (corpse.isUsableByPlayer(player)) GlStateManager.color(1, 1, 1);
            GlStateManager.enableAlpha();
            tm.bindTexture(new ResourceLocation(RPGLoot.MODID, "textures/loot.png"));
            Gui.drawModalRectWithCustomSizedTexture(width / 2 - 7, height / 2 - 7, 0f, 0f, 14, 14, 14, 14);
        }
    }

    @SubscribeEvent
    public static void onGuiScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiConfig) {
            GuiConfig guiConfig = (GuiConfig)event.getGui();
            if (guiConfig.modID.equals(RPGLoot.MODID) && guiConfig.titleLine2 != null && guiConfig.titleLine2.equals("mobs list")) {
                event.setGui(new GuiMobsScreen(guiConfig));
            }
        }
    }
}