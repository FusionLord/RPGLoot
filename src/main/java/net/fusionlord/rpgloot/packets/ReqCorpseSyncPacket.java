package net.fusionlord.rpgloot.packets;

import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ReqCorpseSyncPacket extends CorpsePacket
{
	public ReqCorpseSyncPacket(){}
	public ReqCorpseSyncPacket(EntityCorpse corpse)
	{
		super(corpse);
	}

	public static class HANDLER implements IMessageHandler<ReqCorpseSyncPacket, IMessage>
	{
		@Override
		public IMessage onMessage(ReqCorpseSyncPacket message, MessageContext ctx)
		{
			World world = ctx.getServerHandler().player.world;
			Entity entity = world.getEntityByID(message.corpseID);
			if (entity != null && entity instanceof EntityCorpse)
			{
				((EntityCorpse) entity).markDirty();
			}
			return null;
		}
	}
}
