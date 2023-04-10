package com.xnnre.mod.modmanager.load;

import com.mojang.logging.LogUtils;
import com.xnnre.mod.modmanager.gui.ModManagerScreen;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IConfigurable;
import org.slf4j.Logger;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author WeakJean
 * @date 2023/4/8
 */
public class RemoteModInfo extends ModInfo implements IRemoteModInfo {
    private static final Logger LOGGER = LogUtils.getLogger();

    public RemoteModInfo(ModFileInfo owningFile, IConfigurable config) {
        super(owningFile, config);
    }

    @Override
    public String getUploader() {
        return null;
    }

    @Override
    public LocalDateTime getUploadTime() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public List<String> mutexMods() {
        return null;
    }

    @Override
    public Optional<URL> getDownloadUrl() {
        return Optional.empty();
    }

    @Override
    public boolean isNotForgeMod() {
        return false;
    }

    @Override
    public ModManagerScreen.ModState getState() {
        return null;
    }
}
