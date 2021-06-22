package user11681.huntinghamhills.plugin.transformer.klass;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

@FunctionalInterface
public interface ContextMixinTransformer {
    void transform(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo info);
}
