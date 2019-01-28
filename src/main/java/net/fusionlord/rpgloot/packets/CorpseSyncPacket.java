package net.fusionlord.rpgloot.packets;

import io.netty.buffer.ByteBuf;
import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CorpseSyncPacket extends CorpsePacket {
    public NBTTagCompound corpseTag;

    public CorpseSyncPacket() {
    }

    public CorpseSyncPacket(EntityCorpse corpse) {
        super(corpse);
        corpseTag = new NBTTagCompound();
        corpse.writeToNBT(corpseTag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        corpseTag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeTag(buf, corpseTag);
    }

    public static class HANDLER implements IMessageHandler<CorpseSyncPacket, IMessage> {
        @Override
        public IMessage onMessage(CorpseSyncPacket message, MessageContext ctx) {
            World world = RPGLoot.proxy.getWorld();
            Entity entity = world.getEntityByID(message.corpseID);
            if (entity instanceof EntityCorpse)
                entity.readFromNBT(message.corpseTag);
            return null;
        }
    }
}
