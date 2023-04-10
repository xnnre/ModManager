package com.xnnre.mod.modmanager.mixin;

import com.xnnre.mod.modmanager.gui.ModManagerScreen;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author WeakJean
 * @date 2023/4/9
 */
@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Component component) {
        super(component);
    }

    @Inject(method = "init", at = {@At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
            ordinal = 0
    )})
    @SuppressWarnings("ALL")
    private void modManagerButton(CallbackInfo ci) {
        int l = this.height / 4 + 48;
        this.addRenderableWidget(new ImageButton(
                this.width / 2 - 124, l + 24 * 2, 20, 20,
                0, 0, 20,
                ModManagerScreen.BUTTON_LOCATION, 64, 64,
                (button) -> this.minecraft.setScreen(new ModManagerScreen(this, this.minecraft.options)),
                Component.translatable("modmanager.menu.title")));
    }
}
