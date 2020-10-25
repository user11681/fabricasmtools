package user11681.fabricasmtools;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.mappings.EntryTriple;
import net.gudenau.lib.unsafe.Unsafe;
import user11681.reflect.Accessor;
import user11681.reflect.Classes;
import user11681.reflect.Invoker;

@SuppressWarnings("unchecked")
public class Mapper {
    public static final boolean development = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static final Object2ObjectOpenHashMap<String, String> namespaceClassNames = new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<String, String> namespaceFieldNames = new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<String, String> namespaceMethodNames = new Object2ObjectOpenHashMap<>();

    public final Object2ObjectOpenHashMap<String, String> classes = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, String> fields = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, String> methods = new Object2ObjectOpenHashMap<>();

    public static String internal(final int... numbers) {
        return klass(numbers).replace('.', '/');
    }

    public static String internal(final int number) {
        return klass(number).replace('.', '/');
    }

    public static String klass(final int... numbers) {
        final StringBuilder intermediary = new StringBuilder("class_").append(numbers[0]);

        for (int i = 1; i != numbers.length; i++) {
            intermediary.append('$').append(numbers[i]);
        }

        if (development) {
            return namespaceClassNames.get(intermediary.toString());
        }

        return "net.minecraft." + intermediary.toString();
    }

    public static String field(final int number) {
        final String intermediary = "field_" + number;

        if (development) {
            return namespaceFieldNames.get(intermediary);
        }

        return intermediary;
    }

    public static String method(final int number) {
        final String intermediary = "method_" + number;

        if (development) {
            return namespaceMethodNames.get(intermediary);
        }

        return intermediary;
    }

    public String internal(final String yarn) {
        return this.klass(yarn).replace('.', '/');
    }

    public String putInternal(final String yarn, final int... numbers) {
        return this.putClass(yarn, numbers).replace('.', '/');
    }

    public String klass(final String yarn) {
        final String mapped = this.classes.get(yarn);

        if (mapped == null) {
            throw new IllegalArgumentException(yarn + " does not exist.");
        }

        return mapped;
    }

    public String putClass(final String yarn, final int... number) {
        final String mapped = klass(number);

        if (this.classes.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(yarn + " already exists.");
        }

        return mapped;
    }

    public String field(final String yarn) {
        final String mapped = this.fields.get(yarn);

        if (mapped == null) {
            throw new IllegalArgumentException(yarn);
        }

        return mapped;
    }

    public String putField(final String yarn, final int number) {
        final String mapped = field(number);

        if (this.fields.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(yarn + " already exists.");
        }

        return mapped;
    }

    public String method(final String yarn) {
        final String mapped = this.methods.get(yarn);

        if (mapped == null) {
            throw new IllegalArgumentException(yarn);
        }

        return mapped;
    }

    public String putMethod(final String yarn, final int number) {
        final String mapped = method(number);

        if (this.methods.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(yarn + " already exists.");
        }

        return mapped;
    }

    static {
        try {
            final Object namespaceData = Invoker.bind(FabricLoader.getInstance().getMappingResolver(), "getNamespaceData", Classes.load(Mapper.class.getClassLoader(), false, "net.fabricmc.loader.FabricMappingResolver$NamespaceData"), String.class).invoke("intermediary");

            for (final Map.Entry<String, String> entry : ((Map<String, String>) Accessor.getObject(namespaceData, "classNames")).entrySet()) {
                String intermediary = entry.getKey();

                namespaceClassNames.put(intermediary.substring(Math.max(intermediary.lastIndexOf('.'), intermediary.lastIndexOf("$class_")) + 1), entry.getValue());
            }

            for (final Map.Entry<EntryTriple, String> entry : ((Map<EntryTriple, String>) Accessor.getObject(namespaceData, "fieldNames")).entrySet()) {
                namespaceFieldNames.put(entry.getKey().getName(), entry.getValue());
            }

            for (final Map.Entry<EntryTriple, String> entry : ((Map<EntryTriple, String>) Accessor.getObject(namespaceData, "methodNames")).entrySet()) {
                namespaceMethodNames.put(entry.getKey().getName(), entry.getValue());
            }
        } catch (final Throwable throwable) {
            throw Unsafe.throwException(throwable);
        }
    }
}
