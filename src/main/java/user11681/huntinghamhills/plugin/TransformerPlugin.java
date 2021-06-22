package user11681.huntinghamhills.plugin;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import user11681.huntinghamhills.Mapper;
import user11681.huntinghamhills.plugin.transformer.MethodTransformerEntry;
import user11681.huntinghamhills.plugin.transformer.klass.ContextMixinTransformer;
import user11681.huntinghamhills.plugin.transformer.klass.MixinTransformer;
import user11681.huntinghamhills.plugin.transformer.method.ClassMethodTransformer;
import user11681.huntinghamhills.plugin.transformer.method.ContextMethodTransformer;
import user11681.huntinghamhills.plugin.transformer.method.MethodTransformer;

public abstract class TransformerPlugin extends Mapper implements MixinConfigPlugin {
    private static final Function<String, ObjectArrayList<MethodTransformerEntry>> mapFunction = name -> new ObjectArrayList<>();

    protected transient Map<String, ContextMixinTransformer> classBefore = new Object2ReferenceOpenHashMap<>();
    protected transient Map<String, ContextMixinTransformer> classAfter = new Object2ReferenceOpenHashMap<>();
    protected transient Map<String, List<MethodTransformerEntry>> methodBefore = new Object2ReferenceOpenHashMap<>();
    protected transient Map<String, List<MethodTransformerEntry>> methodAfter = new Object2ReferenceOpenHashMap<>();

    protected boolean canRegister = true;

    protected String internalPackageName;

    @Override
    public void onLoad(String mixinPackage) {
        this.internalPackageName = mixinPackage.replace('.', '/');
    }

    @Override
    public List<String> getMixins() {
        this.canRegister = false;

        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (this.classBefore != null) {
            ContextMixinTransformer transformer = this.classBefore.remove(targetClassName);

            if (transformer != null) {
                transformer.transform(targetClassName, targetClass, mixinClassName, mixinInfo);

                if (this.classBefore.size() == 0) {
                    this.classBefore = null;
                }
            }
        }

        if (this.methodBefore != null) {
            List<MethodTransformerEntry> methodTransformers = this.methodBefore.remove(targetClassName);

            if (methodTransformers != null) {
                for (MethodNode method : targetClass.methods) {
                    int entryIndex = methodTransformers.indexOf(new MethodTransformerEntry(method.name, method.desc));

                    if (entryIndex >= 0) {
                        methodTransformers.get(entryIndex).transformer().transform(method, targetClassName, targetClass, mixinClassName, mixinInfo);
                    }
                }
            }

            if (this.methodBefore.isEmpty()) {
                this.methodBefore = null;
            }
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (this.classAfter != null) {
            ContextMixinTransformer transformer = this.classAfter.remove(targetClassName);

            if (transformer != null) {
                transformer.transform(targetClassName, targetClass, mixinClassName, mixinInfo);

                if (this.classAfter.size() == 0) {
                    this.classAfter = null;
                }
            }
        }

        if (this.methodAfter != null) {
            List<MethodTransformerEntry> methodTransformers = this.methodAfter.remove(targetClassName);

            if (methodTransformers != null) {
                for (MethodNode method : targetClass.methods) {
                    int entryIndex = methodTransformers.indexOf(new MethodTransformerEntry(method.name, method.desc));

                    if (entryIndex >= 0) {
                        methodTransformers.get(entryIndex).transformer().transform(method, targetClassName, targetClass, mixinClassName, mixinInfo);
                    }
                }
            }

            if (this.methodAfter.isEmpty()) {
                this.methodAfter = null;
            }
        }
    }

    protected void classBefore(String targetBinaryName, ContextMixinTransformer transformer) {
        this.verify();

        if (this.classBefore.put(targetBinaryName, transformer) != null) {
            throw new IllegalArgumentException("a pre-Mixin transformer for class %s was already registered by this plugin.".formatted(targetBinaryName));
        }
    }

    protected void classBefore(String targetBinaryName, MixinTransformer transformer) {
        this.verify();

        if (this.classBefore.put(targetBinaryName, transformer) != null) {
            throw new IllegalArgumentException("a pre-Mixin transformer for class %s was already registered by this plugin.".formatted(targetBinaryName));
        }
    }

    protected void classAfter(String targetBinaryName, ContextMixinTransformer transformer) {
        this.verify();

        if (this.classAfter.put(targetBinaryName, transformer) != null) {
            throw new IllegalArgumentException("a post-Mixin transformer for class %s was already registered by this plugin.".formatted(targetBinaryName));
        }
    }

    protected void classAfter(String type, MixinTransformer transformer) {
        this.verify();

        if (this.classAfter.put(type, transformer) != null) {
            throw new IllegalArgumentException("a post-Mixin transformer for class %s was already registered by this plugin.".formatted(type));
        }
    }

    protected void methodBefore(String type, String method, String descriptor, ContextMethodTransformer transformer) {
        this.registerMethod(type, method, descriptor, transformer, this.methodBefore);
    }

    protected void methodBefore(String type, String method, String descriptor, ClassMethodTransformer transformer) {
        this.methodBefore(type, method, descriptor, (ContextMethodTransformer) transformer);
    }

    protected void methodBefore(String type, String method, String descriptor, MethodTransformer transformer) {
        this.methodBefore(type, method, descriptor, (ContextMethodTransformer) transformer);
    }

    protected void methodAfter(String type, String method, String descriptor, ContextMethodTransformer transformer) {
        this.registerMethod(type, method, descriptor, transformer, this.methodAfter);
    }

    protected void methodAfter(String type, String method, String descriptor, ClassMethodTransformer transformer) {
        this.methodAfter(type, method, descriptor, (ContextMethodTransformer) transformer);
    }

    protected void methodAfter(String type, String method, String descriptor, MethodTransformer transformer) {
        this.methodAfter(type, method, descriptor, (ContextMethodTransformer) transformer);
    }

    private void registerMethod(String type, String method, String descriptor, ContextMethodTransformer transformer, Map<String, List<MethodTransformerEntry>> transformers) {
        this.verify();

        List<MethodTransformerEntry> entries = transformers.computeIfAbsent(type, mapFunction);
        MethodTransformerEntry entry = new MethodTransformerEntry(method, descriptor, transformer);

        for (MethodTransformerEntry existing : entries) {
            if (existing.name().equals(entry.name()) && Objects.equals(existing.descriptor(), entry.descriptor())) {
                throw new IllegalArgumentException("a post-Mixin transformer for method %s in class %s was already registered by this plugin.".formatted(
                    descriptor == null ? method : method + descriptor,
                    type
                ));
            }
        }

        entries.add(entry);
    }

    private void verify() {
        if (!this.canRegister) {
            throw new IllegalStateException("getMixins() was already called on this plugin.");
        }
    }
}
