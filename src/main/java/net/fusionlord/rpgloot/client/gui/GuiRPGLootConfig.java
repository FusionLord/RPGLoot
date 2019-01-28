package net.fusionlord.rpgloot.client.gui;

import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.config.RPGConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiConfig;

public class GuiRPGLootConfig extends GuiConfig {
    GuiRPGLootConfig(GuiScreen gui) {
        super(gui, RPGLoot.MODID, false, false, "RPGLootConfig", RPGConfig.class);
    }
}
