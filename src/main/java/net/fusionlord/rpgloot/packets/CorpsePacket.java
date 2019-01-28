package net.fusionlord.rpgloot.packets;

import io.netty.buffer.ByteBuf;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CorpsePacket implements IMessage {
    public int corpseID;

    public CorpsePacket() {
    }

    public CorpsePacket(EntityCorpse corpse) {
        corpseID = corpse.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        corpseID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(corpseID);
    }
}
