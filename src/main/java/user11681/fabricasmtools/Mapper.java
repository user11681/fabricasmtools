package user11681.fabricasmtools;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.lang.invoke.MethodType;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.fabricmc.mappings.EntryTriple;
import net.gudenau.lib.unsafe.Unsafe;
import user11681.reflect.Accessor;
import user11681.reflect.Classes;
import user11681.reflect.Invoker;

@SuppressWarnings("unchecked")
public class Mapper {
    public static final MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();
    public static final boolean development = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static final Object namespaceData;

    public static final Object2ObjectOpenHashMap<String, String> environmentFieldNames = new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<String, String> environmentMethodNames = new Object2ObjectOpenHashMap<>();

    public final Object2ObjectOpenHashMap<String, String> classes = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, String> fields = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, String> methods = new Object2ObjectOpenHashMap<>();

    public static String klass(final int number) {
        final String mapped = "net.minecraft.class_" + number;

        if (development) {
            return mappingResolver.mapClassName("intermediary", mapped);
        }

        return mapped;
    }

    public static String field(final int number) {
        final String intermediary = "field_" + number;

        if (development) {
            return environmentFieldNames.get(intermediary);
        }

        return intermediary;
    }

    public static String method(final int number) {
        final String intermediary = "method_" + number;

        if (development) {
            return environmentMethodNames.get(intermediary);
        }

        return intermediary;
    }

    public String internal(final String yarn) {
        return this.klass(yarn).replace('.', '/');
    }

    public String internal(final int number) {
        return klass(number).replace('.', '/');
    }

    public String putInternal(final String yarn, final int number) {
        return this.putClass(yarn, number).replace('.', '/');
    }

    public String klass(final String yarn) {
        final String intermediary = this.classes.get(yarn);

        if (intermediary == null) {
            throw new IllegalArgumentException(yarn);
        }

        return intermediary;
    }

    public String putClass(final String yarn, final int number) {
        final String mapped = klass(number);

        if (this.classes.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(mapped + number + " already exists.");
        }

        return mapped;
    }

    public String field(final String yarn) {
        final String intermediary = this.fields.get(yarn);

        if (intermediary == null) {
            return yarn;
        }

        if (development) {
            throw new IllegalArgumentException(yarn);
        }

        return intermediary;
    }

    public String putField(final String yarn, final int number) {
        final String mapped = field(number);

        if (this.fields.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(mapped + " already exists.");
        }

        return mapped;
    }

    public String method(final String yarn) {
        final String intermediary = this.methods.get(yarn);

        if (intermediary == null) {
            return yarn;
        }

        if (development) {
            throw new IllegalArgumentException(yarn);
        }

        return intermediary;
    }

    public String putMethod(final String yarn, final int number) {
        final String mapped = method(number);

        if (this.methods.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(mapped + " already exists.");
        }

        return mapped;
    }

    static {
        try {
            namespaceData = Invoker.bind(mappingResolver, "getNamespaceData", MethodType.methodType(Classes.load(Mapper.class.getClassLoader(), false, "net.fabricmc.loader.FabricMappingResolver$NamespaceData"), String.class)).invoke("intermediary");

            for (final Map.Entry<EntryTriple, String> entry : ((Map<EntryTriple, String>) Accessor.getObject(namespaceData, "fieldNames")).entrySet()) {
                environmentFieldNames.put(entry.getKey().getName(), entry.getValue());
            }
            for (final Map.Entry<EntryTriple, String> entry : ((Map<EntryTriple, String>) Accessor.getObject(namespaceData, "methodNames")).entrySet()) {
                environmentMethodNames.put(entry.getKey().getName(), entry.getValue());
            }

        } catch (final Throwable throwable) {
            throw Unsafe.throwException(throwable);
        }
    }
}
