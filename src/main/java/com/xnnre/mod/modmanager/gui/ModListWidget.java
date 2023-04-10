package com.xnnre.mod.modmanager.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.xnnre.mod.modmanager.load.IRemoteModInfo;
import com.xnnre.mod.modmanager.utils.CharacterUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author guoq
 * @date 2023/4/9
 */
public class ModListWidget extends ObjectSelectionList<ModListWidget.ModEntry> {
    private final ModManagerScreen parent;
    int listWidth;
    int right;

    public ModListWidget(ModManagerScreen parent, int listWidth, int top, int bottom) {
        super(parent.minecraft, listWidth, parent.height, top, bottom, parent.fontRenderer.lineHeight * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
        this.right = getRight();
        this.refreshList();
    }

    @Override
    protected int getScrollbarPosition() {
        return this.listWidth;
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    @Override
    protected void renderBackground(@NotNull PoseStack poseStack) {
        this.parent.renderBackground(poseStack);
    }

    void refreshList() {
        clearEntries();
        parent.buildRemoteModList(mod -> new ModListWidget.ModEntry(this.parent, mod), this::addEntry);
    }

    public class ModEntry extends ObjectSelectionList.Entry<ModListWidget.ModEntry> {
        private final ModManagerScreen parent;
        private final IRemoteModInfo modInfo;

        public ModEntry(ModManagerScreen parent, IRemoteModInfo modInfo) {
            this.parent = parent;
            this.modInfo = modInfo;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("modmanager.mod.select", modInfo.getDisplayName());
        }

        @Override
        public void render(@NotNull PoseStack poseStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTick) {
            ModManagerScreen.ModState modState = parent.modStateCalculate(modInfo, (remoteModInfo, localModList) -> {
                List<String> modIds = localModList.stream().map(IModInfo::getModId).toList();
                if (!remoteModInfo.isNotForgeMod()) {
                    return ModManagerScreen.ModState.INVALID;
                }
                String modId = remoteModInfo.getModId();
                if (!modIds.contains(modId)) {
                    return ModManagerScreen.ModState.NEW;
                }
                return ModManagerScreen.ModState.SAME;
            });
            Component name = Component.literal(CharacterUtils.stripCharacterColor(modInfo.getDisplayName()));
            Component version = Component.literal(CharacterUtils.stripCharacterColor(MavenVersionStringHelper.artifactVersionToString(modInfo.getVersion())));
            Font font = parent.fontRenderer;
            font.draw(poseStack, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))), left + 3, top + 2, 0xFFFFFF);
            font.draw(poseStack, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(version, listWidth))), left + 3, top + 2 + font.lineHeight, 0xCCCCCC);
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            parent.setSelected(this);
            ModListWidget.this.setSelected(this);
            return false;
        }

        public IRemoteModInfo getInfo() {
            return modInfo;
        }
    }

}
