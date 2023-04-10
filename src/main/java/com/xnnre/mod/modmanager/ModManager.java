package com.xnnre.mod.modmanager;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.slf4j.Logger;

/**
 * @author WeakJean
 * @date 2023/4/8
 */
@Mod(ModManager.MOD_ID)
public class ModManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "modmanager";

    public ModManager() {
        ClientConfig.init();
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("<ModManager> inited!");
            MinecraftForge.EVENT_BUS.register(ClientEventHandle.class);
        }
    }
}
