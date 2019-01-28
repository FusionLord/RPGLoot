package net.fusionlord.rpgloot.handlers;

import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.config.RPGConfig;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;

@Mod.EventBusSubscriber(modid = RPGLoot.MODID)
public class CommonEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer && !RPGConfig.doPlayers) return;
        if (!event.getEntity().world.isRemote && !RPGConfig.isBlackListed(event.getEntityLiving()))
            event.getEntity().world.spawnEntity(new EntityCorpse(event.getEntity().world, event.getEntityLiving(), event.getSource().getTrueSource() instanceof EntityPlayer ? (EntityPlayer) event.getSource().getTrueSource() : null, event.getDrops()));
        if (RPGConfig.collectDrops)
            event.setCanceled(true);
    }

    @SubscribeEvent()
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        EntityEntry entry = new EntityEntry(EntityCorpse.class, "corpse");
        entry.setRegistryName(new ResourceLocation(RPGLoot.MODID, "corpse"));
        event.getRegistry().register(entry);
    }
}
