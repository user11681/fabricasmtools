package user11681.huntinghamhills.plugin.transformer.method;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

@FunctionalInterface
public interface ClassMethodTransformer extends ContextMethodTransformer {
    void transform(ClassNode klass, MethodNode method);

    @Override
    default void transform(MethodNode method, String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo info) {
        this.transform(targetClass, method);
    }
}
