package net.fusionlord.rpgloot.packets;

import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LootPacket extends CorpsePacket {
    public LootPacket() {
    }

    public LootPacket(EntityCorpse corpse) {
        super(corpse);
    }

    public static class HANDLER implements IMessageHandler<LootPacket, IMessage> {
        @Override
        public IMessage onMessage(LootPacket message, MessageContext ctx) {
            ((EntityCorpse) ctx.getServerHandler().player.world.getEntityByID(message.corpseID)).lootToPlayer(ctx.getServerHandler().player);
            return null;
        }
    }
}
