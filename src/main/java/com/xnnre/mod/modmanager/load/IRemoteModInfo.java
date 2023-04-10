package com.xnnre.mod.modmanager.load;

import com.xnnre.mod.modmanager.gui.ModManagerScreen;
import net.minecraftforge.forgespi.language.IModInfo;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author guoq
 * @date 2023/4/9
 */
public interface IRemoteModInfo extends IModInfo {

    String getUploader();

    LocalDateTime getUploadTime();

    String getDesc();

    List<String> mutexMods();

    Optional<URL> getDownloadUrl();

    boolean isNotForgeMod();

    ModManagerScreen.ModState getState();


}
