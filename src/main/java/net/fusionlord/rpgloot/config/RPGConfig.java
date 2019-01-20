package net.fusionlord.rpgloot.config;

import com.google.common.collect.Maps;
import net.fusionlord.rpgloot.Logger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

import java.util.Map;

import static net.fusionlord.rpgloot.RPGLoot.MODID;

@Config(modid = MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class RPGConfig {

    @Config.Name("Corpse life time:")
    @Config.RangeInt(min = -1)
    @Config.Comment("Time before corpses decay.")
    public static int corpseDecayTime = -1;

    @Config.Name("Collect drops:")
    @Config.Comment("If set to false, drops will be looted from the corpse.")
    public static boolean scatterDrops = false;

    @Config.Name("Mob list is blacklist:")
    @Config.Comment("Use mobList as a blacklist")
    public static boolean isBlacklist = true;

    @Config.Name("Player Corpses:")
    @Config.Comment("Players spawn corpses upon death.")
    public static boolean doPlayers = true;

    @Config.Name("mob list")
    @Config.Comment("List of mobs to toggle on and off.")
    public static Map<String, Boolean> mobList = initializeMobList();

    private static Map<String, Boolean> initializeMobList() {
        Map<String, Boolean> ret = Maps.newHashMap();

        for (ResourceLocation rl : EntityList.getEntityNameList())
        {
            Logger.slog(Level.DEBUG, "Loading Entity: " + rl);
            Class c = EntityList.getClass(rl);
            if (c != null && EntityLivingBase.class.isAssignableFrom(c))
                ret.put(rl.toString(), ret.getOrDefault(rl.toString(), false));
        }

        return ret;
    }

    public static boolean isBlackListed(Entity entity) {
	    return isBlackListed(EntityList.getKey(entity));
    }

    public static boolean isBlackListed(ResourceLocation entity) {
        return mobList != null && (mobList.containsKey(entity.toString()) && mobList.get(entity.toString())) && isBlacklist;
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID))
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }
}
