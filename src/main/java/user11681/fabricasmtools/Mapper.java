package user11681.fabricasmtools;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public abstract class Mapper {
    protected static final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
    protected static final boolean development = FabricLoader.getInstance().isDevelopmentEnvironment();

    protected final Object2ObjectOpenHashMap<String, String> classes = new Object2ObjectOpenHashMap<>();
    protected final Object2ObjectOpenHashMap<String, String> fields = new Object2ObjectOpenHashMap<>();
    protected final Object2ObjectOpenHashMap<String, String> methods = new Object2ObjectOpenHashMap<>();

    protected static String klass(final int number) {
        final String mapped = "net.minecraft.class_" + number;

        if (development) {
            return mappingResolver.mapClassName("intermediary", mapped);
        }

        return mapped;
    }

    protected static String field(final int number) {
        return "field_" + number;
    }

    protected static String method(final int number) {
        return "method_" + number;
    }

    protected String internal(final String yarn) {
        return this.klass(yarn).replace('.', '/');
    }

    protected String internal(final int number) {
        return klass(number).replace('.', '/');
    }

    protected String putInternal(final String yarn, final int number) {
        return this.putClass(yarn, number).replace('.', '/');
    }

    protected String klass(final String yarn) {
        final String intermediary = this.classes.get(yarn);

        if (intermediary == null) {
            throw new IllegalArgumentException(yarn);
        }

        return intermediary;
    }

    protected String putClass(final String yarn, final int number) {
        final String mapped = klass(number);

        if (this.classes.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(mapped + number + " already exists.");
        }

        return mapped;
    }

    protected String field(final String yarn) {
        final String intermediary = this.fields.get(yarn);

        if (intermediary == null) {
            return yarn;
        }

        if (development) {
            throw new IllegalArgumentException(yarn);
        }

        return intermediary;
    }

    protected String putField(final String yarn, final int number) {
        final String mapped = field(number);

        if (this.fields.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(mapped + " already exists.");
        }

        return mapped;
    }

    protected String method(final String yarn) {
        final String intermediary = this.methods.get(yarn);

        if (intermediary == null) {
            return yarn;
        }

        if (development) {
            throw new IllegalArgumentException(yarn);
        }

        return intermediary;
    }

    protected String putMethod(final String yarn, final int number) {
        final String mapped = method(number);

        if (this.methods.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(mapped + " already exists.");
        }

        return mapped;
    }
}
