package net.fusionlord.rpgloot.client;

import net.fusionlord.rpgloot.CommonProxy;
import net.fusionlord.rpgloot.client.rendering.CorpseRenderFactory;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityCorpse.class, new CorpseRenderFactory());
    }

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	public void playSound(String sound)
	{
//		FMLClientHandler.instance().getClientPlayerEntity().playSound(RPGLoot.MODID + ":" + sound, Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.AMBIENT), 1f);
	}

    @Override
    public World getWorld() {
        return Minecraft.getMinecraft().world;
    }
}
