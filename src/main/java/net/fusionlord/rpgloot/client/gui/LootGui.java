package net.fusionlord.rpgloot.client.gui;

import net.fusionlord.rpgloot.RPGLoot;
import net.fusionlord.rpgloot.entities.EntityCorpse;
import net.fusionlord.rpgloot.inventory.LootContainer;
import net.fusionlord.rpgloot.packets.DisposePacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class LootGui extends GuiContainer
{
	private EntityCorpse corpse;
	private EntityPlayer player;
	private GuiButton dispose;

	public LootGui(LootContainer lootContainer, EntityCorpse entCorpse, EntityPlayer entPlayer)
	{
		super(lootContainer);
		corpse = entCorpse;
		player = entPlayer;
	}

    @Override
    public void initGui() {
        super.initGui();
        dispose = new GuiButton(0, guiLeft, guiTop - 25, xSize, 20, "Dispose");
        addButton(dispose);
    }

    @Override
	public void actionPerformed(GuiButton button)
	{
		if (button == dispose)
		{
			RPGLoot.INSTANCE.getPacketHandler().sendToServer(new DisposePacket(corpse));
			player.closeScreen();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		mc.renderEngine.bindTexture(new ResourceLocation(RPGLoot.MODID, "textures/loottable.png"));
		drawTexturedModalRect(guiLeft,  guiTop, 0, 0, 176, 177);
		for (Slot s : inventorySlots.inventorySlots)
		{
			drawTexturedModalRect(guiLeft + s.xPos-1,  guiTop + s.yPos-1, 176, 0, 20, 20);
		}
	}
}
