package user11681.fabricasmtools.plugin;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import user11681.fabricasmtools.Mapper;
import user11681.fabricasmtools.plugin.transformer.MethodTransformerEntry;
import user11681.fabricasmtools.plugin.transformer.klass.ContextMixinTransformer;
import user11681.fabricasmtools.plugin.transformer.klass.MixinTransformer;
import user11681.fabricasmtools.plugin.transformer.method.ClassMethodTransformer;
import user11681.fabricasmtools.plugin.transformer.method.ContextMethodTransformer;
import user11681.fabricasmtools.plugin.transformer.method.MethodTransformer;

public abstract class TransformerPlugin extends Mapper implements MixinConfigPlugin {
    private static final Function<String, ObjectArrayList<MethodTransformerEntry>> mapFunction = (final String name) -> ObjectArrayList.wrap(new MethodTransformerEntry[1], 0);

    protected transient Object2ReferenceOpenHashMap<String, ContextMixinTransformer> preMixinTransformers = new Object2ReferenceOpenHashMap<>(1);
    protected transient Object2ReferenceOpenHashMap<String, ContextMixinTransformer> postMixinTransformers = new Object2ReferenceOpenHashMap<>(1);
    protected transient Object2ReferenceOpenHashMap<String, ObjectArrayList<MethodTransformerEntry>> preMixinMethodTransformers = new Object2ReferenceOpenHashMap<>(1);
    protected transient Object2ReferenceOpenHashMap<String, ObjectArrayList<MethodTransformerEntry>> postMixinMethodTransformers = new Object2ReferenceOpenHashMap<>(1);

    protected boolean canRegister = true;

    protected String internalPackageName;

    @Override
    public void onLoad(final String mixinPackage) {
        this.internalPackageName = mixinPackage.replace('.', '/');
    }

    @Override
    public List<String> getMixins() {
        this.canRegister = false;

        return null;
    }

    @Override
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
        if (this.preMixinTransformers != null) {
            final ContextMixinTransformer transformer = this.preMixinTransformers.remove(targetClassName);

            if (transformer != null) {
                transformer.transform(targetClassName, targetClass, mixinClassName, mixinInfo);

                if (this.preMixinTransformers.size() == 0) {
                    this.preMixinTransformers = null;
                }
            }
        }

        if (this.preMixinMethodTransformers != null) {
            final ObjectArrayList<MethodTransformerEntry> methodTransformers = this.preMixinMethodTransformers.get(targetClassName);

            if (methodTransformers != null) {
                for (final MethodNode method : targetClass.methods) {
                    final int entryIndex = methodTransformers.indexOf(new MethodTransformerEntry(method.name, method.desc));

                    if (entryIndex >= 0) {
                        methodTransformers.get(entryIndex).transformer.transform(method, targetClassName, targetClass, mixinClassName, mixinInfo);
                    }
                }

                this.preMixinMethodTransformers.remove(targetClassName);

            }

            if (this.preMixinMethodTransformers.isEmpty()) {
                this.preMixinMethodTransformers = null;
            }
        }
    }

    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
        if (this.postMixinTransformers != null) {
            final ContextMixinTransformer transformer = this.postMixinTransformers.remove(targetClassName);

            if (transformer != null) {
                transformer.transform(targetClassName, targetClass, mixinClassName, mixinInfo);

                if (this.postMixinTransformers.size() == 0) {
                    this.postMixinTransformers = null;
                }
            }
        }

        if (this.postMixinMethodTransformers != null) {
            final ObjectArrayList<MethodTransformerEntry> methodTransformers = this.postMixinMethodTransformers.get(targetClassName);

            if (methodTransformers != null) {
                for (final MethodNode method : targetClass.methods) {
                    final int entryIndex = methodTransformers.indexOf(new MethodTransformerEntry(method.name, method.desc));

                    if (entryIndex >= 0) {
                        methodTransformers.get(entryIndex).transformer.transform(method, targetClassName, targetClass, mixinClassName, mixinInfo);
                    }
                }

                this.postMixinMethodTransformers.remove(targetClassName);
            }

            if (this.postMixinMethodTransformers.isEmpty()) {
                this.postMixinMethodTransformers = null;
            }
        }
    }

    protected void registerPreMixinTransformer(final String targetBinaryName, final ContextMixinTransformer transformer) {
        this.verify();

        if (this.preMixinTransformers.put(targetBinaryName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a pre-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPreMixinTransformer(final String targetBinaryName, final MixinTransformer transformer) {
        this.verify();

        if (this.preMixinTransformers.put(targetBinaryName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a pre-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPostMixinTransformer(final String targetBinaryName, final ContextMixinTransformer transformer) {
        this.verify();

        if (this.postMixinTransformers.put(targetBinaryName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a post-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPostMixinTransformer(final String targetBinaryName, final MixinTransformer transformer) {
        this.verify();

        if (this.postMixinTransformers.put(targetBinaryName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a post-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    /**
     * {@code descriptor} may be {@code null} to match all methods with name {@code methodName}
     */
    protected void registerPreMixinMethodTransformer(final String targetBinaryName, final String methodName, final String descriptor, final ContextMethodTransformer transformer) {
        this.preMixinMethodTransformer(targetBinaryName, methodName, descriptor, transformer);
    }

    /**
     * {@code descriptor} may be {@code null} to match all methods with name {@code methodName}
     */
    protected void registerPreMixinMethodTransformer(final String targetBinaryName, final String methodName, final String descriptor, final ClassMethodTransformer transformer) {
        this.preMixinMethodTransformer(targetBinaryName, methodName, descriptor, transformer);

    }

    /**
     * {@code descriptor} may be {@code null} to match all methods with name {@code methodName}
     */
    protected void registerPreMixinMethodTransformer(final String targetBinaryName, final String methodName, final String descriptor, final MethodTransformer transformer) {
        this.preMixinMethodTransformer(targetBinaryName, methodName, descriptor, transformer);
    }

    /**
     * {@code descriptor} may be {@code null} to match all methods with name {@code methodName}
     */
    protected void registerPostMixinMethodTransformer(final String targetBinaryName, final String methodName, final String descriptor, final ContextMethodTransformer transformer) {
        this.postMixinMethodTransformer(targetBinaryName, methodName, descriptor, transformer);
    }

    /**
     * {@code descriptor} may be {@code null} to match all methods with name {@code methodName}
     */
    protected void registerPostMixinMethodTransformer(final String targetBinaryName, final String methodName, final String descriptor, final ClassMethodTransformer transformer) {
        this.postMixinMethodTransformer(targetBinaryName, methodName, descriptor, transformer);
    }

    /**
     * {@code descriptor} may be {@code null} to match all methods with name {@code methodName}
     */
    protected void registerPostMixinMethodTransformer(final String targetBinaryName, final String methodName, final String descriptor, final MethodTransformer transformer) {
        this.postMixinMethodTransformer(targetBinaryName, methodName, descriptor, transformer);
    }

    private void preMixinMethodTransformer(final String klass, String method, final String descriptor, final ContextMethodTransformer transformer) {
        this.verify();

        final ObjectArrayList<MethodTransformerEntry> entries = this.postMixinMethodTransformers.computeIfAbsent(klass, mapFunction);
        final MethodTransformerEntry[] entryArray = entries.elements();
        final int entryCount = entries.size();
        final MethodTransformerEntry entry = new MethodTransformerEntry(method, descriptor, transformer);

        for (int i = 0; i != entryCount; i++) {
            if (entryArray[i].name.equals(entry.name) && Objects.equals(entryArray[i].descriptor, entry.descriptor)) {
                if (descriptor != null) {
                    method += descriptor;
                }

                throw new IllegalArgumentException(String.format("a pre-Mixin transformer for method %s in class %s was already registered by this plugin.", method, klass));
            }
        }

        entries.add(entry);
    }

    private void postMixinMethodTransformer(final String klass, String method, final String descriptor, final ContextMethodTransformer transformer) {
        this.verify();

        final ObjectArrayList<MethodTransformerEntry> entries = this.postMixinMethodTransformers.computeIfAbsent(klass, mapFunction);
        final MethodTransformerEntry[] entryArray = entries.elements();
        final int entryCount = entries.size();
        final MethodTransformerEntry entry = new MethodTransformerEntry(method, descriptor, transformer);

        for (int i = 0; i != entryCount; i++) {
            if (entryArray[i].name.equals(entry.name) && Objects.equals(entryArray[i].descriptor, entry.descriptor)) {
                if (descriptor != null) {
                    method += descriptor;
                }

                throw new IllegalArgumentException(String.format("a post-Mixin transformer for method %s in class %s was already registered by this plugin.", method, klass));
            }
        }

        entries.add(entry);
    }

    private void verify() {
        if (!this.canRegister) {
            throw new IllegalStateException("getMixins() was already called for this plugin.");
        }
    }
}
