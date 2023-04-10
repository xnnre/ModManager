package com.xnnre.mod.modmanager.mixin;

import net.minecraftforge.forgespi.locating.ForgeFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

/**
 * @author guoq
 * @date 2023/4/9
 */
@Mixin(value = ForgeFeature.class, remap = false)
public class MixinClientModReloadable {

    @Shadow
    @Final
    private static Map<String, ForgeFeature.IFeatureTest<?>> features;

    /**
     * @author WeakJean
     * @reason to reload mod list
     */
    @Overwrite
    public static <T> void registerFeature(final String featureName, final ForgeFeature.IFeatureTest<T> featureTest) {
        features.putIfAbsent(featureName, featureTest);
    }
}
