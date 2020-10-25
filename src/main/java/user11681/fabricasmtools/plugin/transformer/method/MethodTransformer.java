package user11681.fabricasmtools.plugin.transformer.method;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public interface MethodTransformer extends ContextMethodTransformer {
    void transform(MethodNode method);

    @Override
    default void transform(MethodNode method, String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo info) {
        this.transform(method);
    }
}
