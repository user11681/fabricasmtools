package user11681.fabricasmtools.plugin.transformer;

import java.util.Objects;
import user11681.fabricasmtools.plugin.transformer.method.ContextMethodTransformer;

public class MethodTransformerEntry {
    public final String name;
    public final String descriptor;
    public ContextMethodTransformer transformer;

    public MethodTransformerEntry(final String name, final String descriptor, final ContextMethodTransformer transformer) {
        this.name = name;
        this.descriptor = descriptor;
        this.transformer = transformer;
    }

    public MethodTransformerEntry(final String name, final String descriptor) {
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public int hashCode() {
        return (this.descriptor == null ? 2 << 30 : 0) + this.name.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof MethodTransformerEntry) {
            final MethodTransformerEntry that = (MethodTransformerEntry) object;

            return Objects.equals(this.name, that.name) && (this.descriptor == null || that.descriptor == null || Objects.equals(this.descriptor, that.descriptor));
        }

        return false;
    }
}
