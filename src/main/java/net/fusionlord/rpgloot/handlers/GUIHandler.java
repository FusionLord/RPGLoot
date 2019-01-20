package net.fusionlord.rpgloot.handlers;

import net.fusionlord.rpgloot.client.gui.LootGui;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.fusionlord.rpgloot.inventory.LootContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		Container container = null;
		switch (ID)
		{
			case 0:
				EntityCorpse corpse = (EntityCorpse) world.getEntityByID(x);
				container = new LootContainer(corpse, player.inventory);
				break;
		}
		return container;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
			case 0:
				return new LootGui((LootContainer) getServerGuiElement(ID, player, world, x, y, z), (EntityCorpse) world.getEntityByID(x), player);
		}
		return null;
	}
}