package user11681.fabricasmtools;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public abstract interface MixinConfigPlugin extends IMixinConfigPlugin {
    @Override
    default void onLoad(final String mixinPackage) {}

    @Override
    default String getRefMapperConfig() {
        return null;
    }

    @Override
    default boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        return true;
    }

    @Override
    default void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {}

    @Override
    default List<String> getMixins() {
        return null;
    }

    @Override
    default void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {}

    @Override
    default void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {}
}
