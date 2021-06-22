package user11681.huntinghamhills;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.mappings.EntryTriple;
import net.gudenau.lib.unsafe.Unsafe;
import user11681.reflect.Accessor;
import user11681.reflect.Invoker;

public class Mapper {
    public static final boolean development = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static final Object2ObjectOpenHashMap<String, String> namespaceClassNames = new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<String, String> namespaceFieldNames = new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<String, String> namespaceMethodNames = new Object2ObjectOpenHashMap<>();

    public final Object2ObjectOpenHashMap<String, String> classes = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, String> fields = new Object2ObjectOpenHashMap<>();
    public final Object2ObjectOpenHashMap<String, String> methods = new Object2ObjectOpenHashMap<>();

    public static String internal(int... numbers) {
        return klass(numbers).replace('.', '/');
    }

    public static String klass(int... numbers) {
        StringBuilder intermediary = new StringBuilder("net.minecraft.class_").append(numbers[0]);

        for (int i = 1; i != numbers.length; i++) {
            intermediary.append("$class_").append(numbers[i]);
        }

        return development ? namespaceClassNames.get(intermediary.toString()) : intermediary.toString();
    }

    public static String field(int number) {
        String intermediary = "field_" + number;

        return development ? namespaceFieldNames.get(intermediary) : intermediary;
    }

    public static String method(int number) {
        String intermediary = "method_" + number;

        return development ? namespaceMethodNames.get(intermediary) : intermediary;
    }

    public String internal(String yarn) {
        return this.klass(yarn).replace('.', '/');
    }

    public String putInternal(String yarn, int... numbers) {
        return this.putClass(yarn, numbers).replace('.', '/');
    }

    public String klass(String yarn) {
        String mapped = this.classes.get(yarn);

        if (mapped == null) {
            throw new IllegalArgumentException(yarn + " does not exist.");
        }

        return mapped;
    }

    public String putClass(String yarn, int... number) {
        String mapped = klass(number);

        if (this.classes.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(yarn + " already exists.");
        }

        return mapped;
    }

    public String field(String yarn) {
        String mapped = this.fields.get(yarn);

        if (mapped == null) {
            throw new IllegalArgumentException(yarn);
        }

        return mapped;
    }

    public String putField(String yarn, int number) {
        String mapped = field(number);

        if (this.fields.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(yarn + " already exists.");
        }

        return mapped;
    }

    public String method(String yarn) {
        String mapped = this.methods.get(yarn);

        if (mapped == null) {
            throw new IllegalArgumentException(yarn);
        }

        return mapped;
    }

    public String putMethod(String yarn, int number) {
        String mapped = method(number);

        if (this.methods.put(yarn, mapped) != null) {
            throw new IllegalArgumentException(yarn + " already exists.");
        }

        return mapped;
    }

    static {
        try {
            Object namespaceData = Invoker.bind(FabricLoader.getInstance().getMappingResolver(), "getNamespaceData", Class.forName("net.fabricmc.loader.FabricMappingResolver$NamespaceData", false, Mapper.class.getClassLoader()), String.class).invoke("intermediary");

            for (Map.Entry<String, String> entry : Accessor.<Map<String, String>>getObject(namespaceData, "classNames").entrySet()) {
                namespaceClassNames.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<EntryTriple, String> entry : Accessor.<Map<EntryTriple, String>>getObject(namespaceData, "fieldNames").entrySet()) {
                namespaceFieldNames.put(entry.getKey().getName(), entry.getValue());
            }

            for (Map.Entry<EntryTriple, String> entry : Accessor.<Map<EntryTriple, String>>getObject(namespaceData, "methodNames").entrySet()) {
                namespaceMethodNames.put(entry.getKey().getName(), entry.getValue());
            }
        } catch (Throwable throwable) {
            throw Unsafe.throwException(throwable);
        }
    }
}
