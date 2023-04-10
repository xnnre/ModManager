package com.xnnre.mod.modmanager.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author guoq
 * @date 2023/4/9
 */
public class ModDetailInfoPanel extends ScrollPanel {
    private final ModManagerScreen parent;
    private List<FormattedCharSequence> lines = Collections.emptyList();

    public ModDetailInfoPanel(ModManagerScreen parent, Minecraft minecraft, int width, int height, int top) {
        super(minecraft, width, height, top, parent.modListWidget.right);
        this.parent = parent;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput output) {
    }

    @Override
    public int getContentHeight() {
        int height = 50;
        height += (lines.size() * parent.fontRenderer.lineHeight);
        if (height < this.bottom - this.top - 8)
            height = this.bottom - this.top - 8;
        return height;
    }

    @Override
    protected int getScrollAmount() {
        return 3 * parent.fontRenderer.lineHeight;
    }

    @Override
    protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        for (FormattedCharSequence line : lines) {
            if (line != null) {
                RenderSystem.enableBlend();
                parent.fontRenderer.drawShadow(poseStack, line, left + ModManagerScreen.PADDING, relativeY, 0xFFFFFF);
                RenderSystem.disableBlend();
            }
            relativeY += parent.fontRenderer.lineHeight;
        }

        final Style style = findTextLine(mouseX, mouseY);
        if (style != null) {
            parent.renderComponentHoverEffect(poseStack, style, mouseX, mouseY);
        }
    }

    void clearPanel() {
        this.lines = Collections.emptyList();
    }

    void setInfo(List<Component> lines) {
        this.lines = resize(lines);
    }

    private List<FormattedCharSequence> resize(List<Component> lines) {
        List<FormattedCharSequence> ret = new ArrayList<>();
        int maxTextLength = this.width - 12;
        if (maxTextLength < 0) {
            return ret;
        }
        lines.forEach(line -> ret.addAll(Language.getInstance().getVisualOrder(parent.fontRenderer.getSplitter().splitLines(line, maxTextLength, Style.EMPTY))));
        return ret;
    }

    private Style findTextLine(final int mouseX, final int mouseY) {
        if (!isMouseOver(mouseX, mouseY))
            return null;

        double offset = (mouseY - top) + border + scrollDistance + 1;
        //        if (logoPath != null) {
        //            offset -= 50;
        //        }
        if (offset <= 0) {
            return null;
        }
        int lineIdx = (int) (offset / parent.fontRenderer.lineHeight);
        if (lineIdx >= lines.size() || lineIdx < 1) {
            return null;
        }
        FormattedCharSequence line = lines.get(lineIdx - 1);
        if (line != null) {
            return parent.fontRenderer.getSplitter().componentStyleAtWidth(line, mouseX - left - border);
        }
        return null;
    }
}
