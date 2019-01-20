package net.fusionlord.rpgloot;

import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.fusionlord.rpgloot.handlers.CommonEvents;
import net.fusionlord.rpgloot.handlers.GUIHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(RPGLoot.INSTANCE, new GUIHandler());
        EntityRegistry.registerModEntity(new ResourceLocation(RPGLoot.MODID, "corpse"), EntityCorpse.class, "rpgloot_corpse", 0, RPGLoot.INSTANCE, 64, 20, false);
    }

	public void postInit(FMLPostInitializationEvent event) {

	}

	public World getWorld() {
	    return null;
    }
}
