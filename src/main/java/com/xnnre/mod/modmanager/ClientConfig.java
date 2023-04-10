package com.xnnre.mod.modmanager;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

/**
 * @author WeakJean
 * @date 2023/4/9
 */
public class ClientConfig {
    public static ForgeConfigSpec.ConfigValue<Boolean> clientModEnableReload;


    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, config(new ForgeConfigSpec.Builder()).build());
    }

    public static ForgeConfigSpec.Builder config(ForgeConfigSpec.Builder builder) {
        builder.push("Mod Loader");
        clientModEnableReload = builder.define("Mod list reloadable", true);
        builder.pop();
        return builder;
    }
}
