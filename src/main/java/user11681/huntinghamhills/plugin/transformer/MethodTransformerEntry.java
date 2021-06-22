package user11681.huntinghamhills.plugin.transformer;

import java.util.Objects;
import user11681.huntinghamhills.plugin.transformer.method.ContextMethodTransformer;

public record MethodTransformerEntry(String name, String descriptor, ContextMethodTransformer transformer) {
    public MethodTransformerEntry(String name, String descriptor) {
        this(name, descriptor, null);
    }

    @Override
    public int hashCode() {
        return (this.descriptor == null ? 1 << 31 : 0) + this.name.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof MethodTransformerEntry that
            && this.name.equals(that.name)
            && (this.descriptor == null || that.descriptor == null || Objects.equals(this.descriptor, that.descriptor));
    }
}
