package net.fusionlord.rpgloot.config;

import com.google.common.collect.Maps;
import net.fusionlord.rpgloot.RPGLoot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    @Config.Comment("Should we collect drops in the corpse?")
    public static boolean collectDrops = true;

    @Config.Name("Mob list is blacklist:")
    @Config.Comment("Use mobList as a blacklist")
    public static boolean isBlacklist = true;

    @Config.Name("Player Corpses:")
    @Config.Comment("Players spawn corpses upon death.")
    public static boolean doPlayers = true;

    @Config.Name("mobs list")
    @Config.Comment("List of mobs to toggle on and off.")
    public static Map<String, Boolean> mobsList = Maps.newHashMap();

    @Config.Name("Particles")
    @Config.Comment("Change corpse particle settings.")
    public static ParticleCategory particles = new ParticleCategory();

    public static class ParticleCategory {
        @Config.Name("Show loot particle")
        @Config.Comment("Show particles if a corpse contains Items.")
        public boolean spawnItem = true;

        @Config.Name("Loot particle")
        @Config.Comment("Particle if a corpse contains Items.")
        public EnumParticleTypes itemParticle = EnumParticleTypes.VILLAGER_HAPPY;

        @Config.Name("Show empty particle")
        @Config.Comment("Show particles if a corpse doesn't contains Items.")
        public boolean spawnEmpty;

        @Config.Name("Empty particle")
        @Config.Comment("Particle if a corpse doesn't contains Items.")
        public EnumParticleTypes emptyParticle = EnumParticleTypes.SMOKE_NORMAL;

        @Config.Name("Chance")
        @Config.Comment("Particle chance per tick.")
        @Config.RangeInt(min = 0, max = 100)
        public int chance = 15;
    }

    public static void initializeMobList() {
        for (ResourceLocation rl : EntityList.getEntityNameList()) {
            Class c = EntityList.getClass(rl);
            if (c != null && EntityLivingBase.class.isAssignableFrom(c)) {
                mobsList.put(rl.toString(), mobsList.getOrDefault(rl.toString(), false));
                RPGLoot.logger.info("Registering :" + rl);
            }
        }
        ConfigManager.sync(RPGLoot.MODID, Config.Type.INSTANCE);
    }

    public static boolean isBlackListed(Entity entity) {
        ResourceLocation rl = EntityList.getKey(entity);
        return rl == null || isBlackListed(rl);
    }

    public static boolean isBlackListed(ResourceLocation resourceLocation) {
        return mobsList != null && (mobsList.containsKey(resourceLocation.toString()) && mobsList.get(resourceLocation.toString())) && isBlacklist;
    }

    @SubscribeEvent
    public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }
}
