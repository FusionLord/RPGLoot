package net.fusionlord.rpgloot.entities;

import net.fusionlord.rpgloot.CommonProxy;
import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.client.ClientProxy;
import net.fusionlord.rpgloot.config.RPGConfig;
import net.fusionlord.rpgloot.packets.CorpseSyncPacket;
import net.fusionlord.rpgloot.packets.ReqCorpseSyncPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class EntityCorpse extends Entity implements IInventory {
    private List<ItemStack> drops;
    private UUID owner;
    private NBTTagCompound oldEntityData;
    private boolean dispose;

    public EntityCorpse(World worldIn) {
        super(worldIn);
        oldEntityData = new NBTTagCompound();
        setEntityClass("");
        drops = new ArrayList<>();
        setSize(1f, 1f);
    }

    public EntityCorpse(World worldIn, EntityLivingBase entityLivingBase, EntityPlayer player, List<EntityItem> entityDrops) {
        this(worldIn);
        copyData(entityLivingBase);
        if (RPGConfig.collectDrops) {
            addDrops(entityDrops);
        }
        setLocationAndAngles(entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ, 0, 0);
        if (player != null) {
            owner = player.getPersistentID();
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("entity.corpse.name", oldEntityData.getString("CustomName"));
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    private void addDrops(List<EntityItem> itemEntities) {
        for (EntityItem entityItem : itemEntities) {
            if (entityItem == null) continue;
            addDrop(entityItem.getItem());
        }
    }

    private void copyData(EntityLivingBase entityLivingBase) {
        oldEntityData = new NBTTagCompound();
        setEntityClass(entityLivingBase.getClass().getCanonicalName());
        entityLivingBase.writeToNBT(oldEntityData);
        oldEntityData.setBoolean("NoAI", true);
        oldEntityData.removeTag("Fire");
        oldEntityData.setShort("HurtTime", (short) 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setTag("entityData", oldEntityData);

        tagCompound.setBoolean("hasOwner", owner != null);
        if (owner != null) {
            tagCompound.setLong("UUID1", owner.getMostSignificantBits());
            tagCompound.setLong("UUID2", owner.getLeastSignificantBits());
        }
        if (drops != null) {
            tagCompound.setInteger("dropCount", drops.size());
            NBTTagCompound dropsTag = new NBTTagCompound();
            for (int i = 0; i < drops.size(); i++) {
                if (drops.get(i) != null) {
                    NBTTagCompound dropTag = new NBTTagCompound();
                    drops.get(i).writeToNBT(dropTag);
                    dropsTag.setTag("drop:".concat(String.valueOf(i)), dropTag);
                }
            }
            tagCompound.setTag("drops", dropsTag);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        if (tagCompound.getBoolean("hasOwner")) {
            owner = new UUID(tagCompound.getLong("UUID1"), tagCompound.getLong("UUID2"));
        } else {
            owner = null;
        }
        oldEntityData = tagCompound.getCompoundTag("entityData");
        if (tagCompound.hasKey("dropCount")) {
            int count = tagCompound.getInteger("dropCount");
            drops = new ArrayList<>();
            NBTTagCompound dropsTag = tagCompound.getCompoundTag("drops");
            for (int i = 0; i < count; i++) {
                drops.add(new ItemStack(dropsTag.getCompoundTag("drop:".concat(String.valueOf(i)))));
            }
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (isUsableByPlayer(player) && !world.isRemote) {
            player.openGui(RPGLoot.INSTANCE, 0, world, getEntityId(), 0, 0);
            return true;
        } else if (!isUsableByPlayer(player) && world.isRemote) {
            //			player.addChatMessage(new ChatComponentText("That would be stealing."));
            if (world.isRemote) {
                CommonProxy proxy = RPGLoot.proxy;
                if (proxy instanceof ClientProxy) {
                    ((ClientProxy) proxy).playSound("looting.stealing");
                }
            }
            return true;
        }
        return true;
    }

    private void addDrop(ItemStack itemStack) {
        if (itemStack == null) return;
        for (ItemStack currentStack : drops) {
            if (ItemStack.areItemStacksEqual(currentStack, itemStack) && currentStack.getCount() < getInventoryStackLimit()) {
                int remainder = getInventoryStackLimit() - currentStack.getCount();
                if (itemStack.getCount() >= remainder) {
                    currentStack.setCount(getInventoryStackLimit());
                    itemStack.setCount(itemStack.getCount() - remainder);
                } else {
                    currentStack.setCount(currentStack.getCount() + itemStack.getCount());
                    return;
                }
            }
        }
        drops.add(itemStack);
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if ((getEntityClass() == null || getEntityClass().isEmpty()) && world.isRemote)
            RPGLoot.INSTANCE.getPacketHandler().sendToServer(new ReqCorpseSyncPacket(this));
        if (getPosition().getY() > 1)
            move(MoverType.SELF, 0, -.15, 0);

        if (world.isRemote && rand.nextFloat() <= (RPGConfig.particles.chance / 100f)) {
            float x = (float) posX - .5f;
            float y = (float) posY + .2f;
            float z = (float) posZ - .5f;
            float var1 = (rand.nextFloat() * 0.5F) * 2 - rand.nextInt(1);
            float var2 = (rand.nextFloat() * 0.5F) * 2 - rand.nextInt(1);
            float var3 = (rand.nextFloat() * 0.5F) * 3 - rand.nextInt(2);
            if (RPGConfig.particles.spawnEmpty && drops.isEmpty())
                world.spawnParticle(RPGConfig.particles.emptyParticle, (double) x + var1, (double) y + var3, (double) z + var2, .5D, .5D, .5D);
            else if (RPGConfig.particles.spawnItem)
                world.spawnParticle(RPGConfig.particles.itemParticle, (double) x + var1, (double) y + var3, (double) z + var2, .5D, .5D, .5D);
            return;
        }

        int decayTime = RPGConfig.corpseDecayTime;
        if ((decayTime > -1 && (ticksExisted / 20) / 60 > decayTime) || dispose) {
            setDead();
        }
    }

    public int getSizeInventory() {
        return drops != null ? drops.size() : 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public ItemStack getStackInSlot(int index) {
        if (index >= drops.size()) {
            return ItemStack.EMPTY;
        }
        return drops.get(index);
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack currentStack = getStackInSlot(index);
        if (currentStack != null) {
            ItemStack itemstack;

            if (currentStack.getCount() <= count) {
                itemstack = currentStack;
                drops.remove(currentStack);
                return itemstack;
            } else {
                itemstack = currentStack.splitStack(count);

                if (currentStack.getCount() == 0) {
                    drops.remove(currentStack);
                }
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        return getStackInSlot(index);
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < drops.size()) {
            drops.set(index, stack);
        } else {
            drops.add(stack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }


    @Override
    public void markDirty() {
        for (int i = 0; i < drops.size(); i++) {
            if (drops.get(i) == ItemStack.EMPTY) {
                drops.remove(i);
            }
        }
        RPGLoot.INSTANCE.getPacketHandler().sendToAllAround(new CorpseSyncPacket(this), new NetworkRegistry.TargetPoint(dimension, posX, posY, posZ, 64D));
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return owner == null || owner == player.getPersistentID();
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    public NBTTagCompound getOldEntityData() {
        return oldEntityData;
    }

    public String getEntityClass() {
        return oldEntityData.getString("entityClass");
    }

    public void setEntityClass(String entClass) {
        oldEntityData.setString("entityClass", entClass);
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    public boolean lootToPlayer(EntityPlayer player) {
        if (drops != null && player != null) {
            Iterator<ItemStack> iterator = drops.iterator();
            while (iterator.hasNext()) {
                ItemStack itemStack = iterator.next();
                if (!player.inventory.addItemStackToInventory(itemStack)) {
                    //					player.addChatComponentMessage(new ChatComponentText("Inventory is full."));
                    if (world.isRemote) {
                        CommonProxy proxy = RPGLoot.proxy;
                        if (proxy instanceof ClientProxy) {
                            ((ClientProxy) proxy).playSound("looting.inventoryfull");
                        }
                    }
                    return false;
                }
                iterator.remove();
            }
            return true;
        }
        return false;
    }

    public void dispose() {
        dispose = true;
    }
}
