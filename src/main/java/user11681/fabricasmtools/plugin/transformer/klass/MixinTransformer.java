package user11681.fabricasmtools.plugin.transformer.klass;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

@FunctionalInterface
public interface MixinTransformer extends ContextMixinTransformer {
    void transform(ClassNode klass);

    @Override
    default void transform(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo info) {
        this.transform(targetClass);
    }
}
