package com.xnnre.mod.modmanager.utils;

import net.minecraft.util.StringUtil;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

/**
 * @author guoq
 * @date 2023/4/9
 */
public final class CharacterUtils {


    private CharacterUtils() {
    }

    public static String stripCharacterColor(String value) {
        return StringUtil.stripColor(value);
    }

    public static String fillCharacterColor(String value, Ansi.Color color) {
        try {
            AnsiConsole.systemInstall();
            return Ansi.ansi().fg(color).a(value).toString();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }


}
