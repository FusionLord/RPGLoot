package net.fusionlord.rpgloot.network;


import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
	private SimpleNetworkWrapper handler;
	private int packetCount = 0;
	public PacketHandler(String channelName)
	{
		handler = new SimpleNetworkWrapper(channelName);
	}

	public void registerMessage(Class handlerClass, Class packetClass, Side side)
	{
		handler.registerMessage(handlerClass, packetClass, packetCount++, side);
	}

	public void sendToServer(IMessage packet)
	{
		handler.sendToServer(packet);
	}

	public void sendToAllAround(IMessage packet, NetworkRegistry.TargetPoint targetPoint)
	{
		handler.sendToAllAround(packet, targetPoint);
	}
}
