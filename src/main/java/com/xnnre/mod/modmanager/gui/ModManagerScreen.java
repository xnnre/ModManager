package com.xnnre.mod.modmanager.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.xnnre.mod.modmanager.ModManager;
import com.xnnre.mod.modmanager.load.IRemoteModInfo;
import com.xnnre.mod.modmanager.load.RemoteModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author WeakJean
 * @date 2023/4/8
 */
@OnlyIn(Dist.CLIENT)
public class ModManagerScreen extends Screen {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocation BUTTON_LOCATION = new ResourceLocation(ModManager.MOD_ID, "textures/gui/mod_download.png");
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Screen parentScreen;
    private Options options;
    private List<IModInfo> localMods;
    private List<RemoteModInfo> remoteMods;
    Minecraft minecraft;
    Font fontRenderer;

    static final int PADDING = 6;
    int modListWidth;
    int modDetailWidth;

    Button downloadButton;
    Button doneButton;
    ModListWidget modListWidget;
    ModDetailInfoPanel modDetailInfoPanel;
    EditBox search;
    ModListWidget.ModEntry selected;


    public ModManagerScreen(Screen parentScreen, Options options) {
        super(Component.translatable("modmanager.menu.title"));
        this.parentScreen = parentScreen;
        this.options = options;
        this.minecraft = parentScreen.getMinecraft();
        this.fontRenderer = minecraft.font;
        this.localMods = Collections.unmodifiableList(ModList.get().getMods());
    }

    @Override
    protected void init() {
        modListWidth = Math.min(Math.max(this.width / 3, 80), 100);
        modDetailWidth = this.width - this.modListWidth - 3 * PADDING;
        int doneAndDownloadButtonWidth = Math.min(this.modDetailWidth / 2 - 9, 100);
        int y = this.height - 20 - PADDING;
        int buttonAndPaddingHeight = 20 + 2 * PADDING;
        int buttonPadding = (this.modDetailWidth - 2 * doneAndDownloadButtonWidth) / 3;

        downloadButton = new Button(
                this.modListWidth + 2 * PADDING + buttonPadding,
                y,
                doneAndDownloadButtonWidth,
                20,
                Component.translatable("modmanager.mod.download"),
                b -> {

                }
        );
        doneButton = new Button(
                this.modListWidth + 2 * PADDING + 2 * buttonPadding + doneAndDownloadButtonWidth,
                y,
                doneAndDownloadButtonWidth,
                20,
                Component.translatable("modmanager.mod.done"),
                b -> this.onClose()
        );
        search = new EditBox(
                fontRenderer,
                PADDING + 1,
                y,
                this.modListWidth - 2,
                14,
                Component.translatable("modmanager.mod.search")
        );
        modListWidget = new ModListWidget(
                this,
                modListWidth,
                buttonAndPaddingHeight,
                search.y - fontRenderer.lineHeight - PADDING
        );
        modListWidget.setLeftPos(PADDING);
        modDetailInfoPanel = new ModDetailInfoPanel(
                this,
                minecraft,
                modDetailWidth,
                this.height - PADDING - buttonAndPaddingHeight,
                PADDING
        );

        addRenderableWidget(downloadButton);
        addRenderableWidget(doneButton);
        addRenderableWidget(search);
        addRenderableWidget(modListWidget);
        addRenderableWidget(modDetailInfoPanel);
        search.setFocus(false);
        search.setCanLoseFocus(true);
        downloadButton.active = false;


    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.modListWidget.render(poseStack, mouseX, mouseY, partialTick);
        if (this.modDetailInfoPanel != null)
            this.modDetailInfoPanel.render(poseStack, mouseX, mouseY, partialTick);

        Component text = Component.translatable("modmanager.mod.search");
        int x = modListWidget.getLeft() + ((modListWidget.getRight() - modListWidget.getLeft()) / 2) - (fontRenderer.width(text) / 2);
        this.search.render(poseStack, mouseX, mouseY, partialTick);
        super.render(poseStack, mouseX, mouseY, partialTick);
        fontRenderer.draw(poseStack, text.getVisualOrderText(), x, search.y - fontRenderer.lineHeight, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parentScreen);
    }

    @Override
    public void renderComponentHoverEffect(@NotNull PoseStack poseStack, @Nullable Style style, int mouseX, int mouseY) {
        super.renderComponentHoverEffect(poseStack, style, mouseX, mouseY);
    }

    public void onModDownload() {

    }

    public void setSelected(ModListWidget.ModEntry selected) {
        this.selected = selected;
        updateCache();
    }

    public void buildRemoteModList(Function<RemoteModInfo, ModListWidget.ModEntry> map, Consumer<ModListWidget.ModEntry> consumer) {
        Optional.ofNullable(remoteMods).orElse(new ArrayList<>()).forEach(mod -> consumer.accept(map.apply(mod)));
    }

    public ModState modStateCalculate(IRemoteModInfo info, BiFunction<IRemoteModInfo, List<IModInfo>, ModState> bimap) {
        return bimap.apply(info, Optional.ofNullable(localMods).orElse(new ArrayList<>()));
    }


    //    private void reloadMods() {
    //        try {
    //            ModLoader.get().gatherAndInitializeMods(ModWorkManager.syncExecutor(), ModWorkManager.parallelExecutor(), () -> {
    //                // 先不操作 考虑screen转圈
    //            });
    //            this.localMods = Collections.unmodifiableList(ModList.get().getMods());
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }

    private void updateCache() {
        if (selected == null) {
            modDetailInfoPanel.clearPanel();
            return;
        }
        IRemoteModInfo selectedRemoteMod = selected.getInfo();
        List<Component> lines = new ArrayList<>();

        // todo 干掉
        VersionChecker.CheckResult vercheck = VersionChecker.getResult(selectedRemoteMod);

        lines.add(Component.literal(selectedRemoteMod.getDisplayName()));
        lines.add(Component.translatable("modmanager.info.version", MavenVersionStringHelper.artifactVersionToString(selectedRemoteMod.getVersion())));
        lines.add(Component.translatable("modmanager.info.modid", selectedRemoteMod.getModId()));
        selectedRemoteMod.getConfig().getConfigElement("authors").ifPresent(authors -> lines.add(Component.translatable("modmanager.info.authors", authors)));
        lines.add(Component.translatable("modmanager.info.license", selectedRemoteMod.getOwningFile().getLicense()));
        lines.add(Component.empty());

        lines.add(Component.translatable("modmanager.info.uploader", selectedRemoteMod.getUploader()));
        lines.add(Component.translatable("modmanager.info.uploadtime", FORMATTER.format(selectedRemoteMod.getUploadTime())));

        lines.add(Component.empty());
        lines.add(Component.literal(selectedRemoteMod.getDesc()));

        modDetailInfoPanel.setInfo(lines);
    }


    public enum ModState {
        NEW(new ResourceLocation(ModManager.MOD_ID, "textures/gui/new_check_icons.png"), Component.translatable("modmanager.state.new")),
        SAME(null, Component.translatable("modmanager.state.same")),
        DIFF_LEADED(new ResourceLocation(ModManager.MOD_ID, "textures/gui/update_check_icons.png"), Component.translatable("modmanager.state.leaded")),
        DIFF_OUTDATED(null, Component.translatable("modmanager.state.outdated")),
        INCOMPATIBLE(null, Component.translatable("modmanager.state.incompatible")),
        INVALID(null, Component.translatable("modmanager.state.invalid"));

        private ResourceLocation texture;

        private Component text;

        ModState(ResourceLocation texture, Component text) {
            this.texture = texture;
            this.text = text;
        }
    }

}
