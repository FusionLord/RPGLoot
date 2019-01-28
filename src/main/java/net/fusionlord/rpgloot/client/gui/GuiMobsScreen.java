package net.fusionlord.rpgloot.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.*;
import net.minecraftforge.fml.common.Loader;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuiMobsScreen extends GuiConfig {
    protected GuiButtonExt nextMod;
    protected GuiButtonExt prevMod;
    protected Map<String, List<IConfigElement>> modElements = Maps.newHashMap();
    protected int index = 0;

    public GuiMobsScreen(GuiConfig guiConfig) {
        super(guiConfig.parentScreen, guiConfig.modID, guiConfig.title);
        for (IConfigElement element : guiConfig.configElements) {
            List<IConfigElement> list;
            String modid = element.getName().split(":")[0];
            list = modElements.getOrDefault(modid, Lists.newArrayList());
            list.add(element);
            modElements.put(modid, list);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(nextMod = new GuiButtonExt(100000, width - 60, 5, 55, 20, "Next Mod"));
        buttonList.add(prevMod = new GuiButtonExt(100001, 5, 5, 55, 20, "Prev Mod"));
        updateEntries();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if ((button == nextMod && next()) || (button == prevMod && prev()))
            updateEntries();
        else
            super.actionPerformed(button);
    }

    private boolean prev() {
        return (index = clamp(index - 1)) != -1;
    }

    private boolean next() {
        return (index = clamp(index + 1)) != -1;
    }

    private int clamp(int index) {
        return MathHelper.clamp(index, 0, modElements.size() - 1);
    }

    private void updateEntries() {
        String modid = (String) modElements.keySet().toArray()[index];
        entryList.saveConfigElements();
        configElements.clear();
        configElements.addAll(modElements.get(modid));
        titleLine2 = Loader.instance().getModList().stream().filter((c) -> c.getModId().equals(modid)).collect(Collectors.toList()).get(0).getName();
        needsRefresh = true;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.entryList == null || this.needsRefresh) {
            this.entryList = new GuiConfigEntries(this, mc);
            this.needsRefresh = false;
        }
    }
}
