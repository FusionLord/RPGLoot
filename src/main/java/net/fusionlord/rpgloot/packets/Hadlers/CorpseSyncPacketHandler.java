package net.fusionlord.rpgloot.packets.Hadlers;

import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.fusionlord.rpgloot.packets.CorpseSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CorpseSyncPacketHandler implements IMessageHandler<CorpseSyncPacket, IMessage>
{
	@Override
	public IMessage onMessage(CorpseSyncPacket message, MessageContext ctx)
	{
		World world = Minecraft.getMinecraft().world;
		Entity entity = world.getEntityByID(message.corpseID);
		if(entity instanceof EntityCorpse)
		{
			entity.readFromNBT(message.corpseTag);
		}
		return null;
	}
}
