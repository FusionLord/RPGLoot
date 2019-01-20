package net.fusionlord.rpgloot;

import net.fusionlord.rpgloot.network.PacketHandler;
import net.fusionlord.rpgloot.packets.CorpseSyncPacket;
import net.fusionlord.rpgloot.packets.DisposePacket;
import net.fusionlord.rpgloot.packets.LootPacket;
import net.fusionlord.rpgloot.packets.ReqCorpseSyncPacket;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = RPGLoot.MODID, name = RPGLoot.MODNAME, version = RPGLoot.VERSION, guiFactory = "net.fusionlord.rpgloot.client.gui.GuiFactory")
public class RPGLoot
{
	public static final String MODID = "rpgloot";
	public static final String MODNAME = "RPGLoot";
	public static final String VERSION = "1.0";

	public static final Logger logger = new Logger();

	@Mod.Instance(RPGLoot.MODID)
	public static RPGLoot INSTANCE;

	@SidedProxy(clientSide = "net.fusionlord.rpgloot.client.ClientProxy", serverSide = "net.fusionlord.rpgloot.CommonProxy")
	public static CommonProxy proxy;

	private PacketHandler packetHandler;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
	    proxy.init(event);
		setUpPacketHandler();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
	}

	private void setUpPacketHandler()
	{
		packetHandler = new PacketHandler(RPGLoot.MODNAME);
		packetHandler.registerMessage(LootPacket.HANDLER.class, LootPacket.class, Side.SERVER);
		packetHandler.registerMessage(DisposePacket.HANDLER.class, DisposePacket.class, Side.SERVER);
		packetHandler.registerMessage(ReqCorpseSyncPacket.HANDLER.class, ReqCorpseSyncPacket.class, Side.SERVER);
		packetHandler.registerMessage(CorpseSyncPacket.HANDLER.class, CorpseSyncPacket.class, Side.CLIENT);
	}

	public PacketHandler getPacketHandler()
	{
		return packetHandler;
	}

}