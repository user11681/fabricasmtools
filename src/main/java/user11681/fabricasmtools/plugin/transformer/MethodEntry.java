package user11681.fabricasmtools.plugin.transformer;

import java.util.Objects;

public class MethodEntry {
    public final String name;
    public final String descriptor;

    public MethodEntry(final String name, final String descriptor) {
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof MethodEntry) {
            final MethodEntry that = (MethodEntry) object;

            return Objects.equals(this.name, that.name) && (this.descriptor == null || that.descriptor == null || Objects.equals(this.descriptor, that.descriptor));
        }

        return false;
    }
}
