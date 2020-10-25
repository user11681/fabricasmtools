package user11681.fabricasmtools.plugin;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import java.util.List;
import java.util.function.Function;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import user11681.fabricasmtools.Mapper;
import user11681.fabricasmtools.plugin.transformer.klass.ContextMixinTransformer;
import user11681.fabricasmtools.plugin.transformer.klass.MixinTransformer;
import user11681.fabricasmtools.plugin.transformer.method.ClassMethodTransformer;
import user11681.fabricasmtools.plugin.transformer.method.ContextMethodTransformer;
import user11681.fabricasmtools.plugin.transformer.method.MethodTransformer;

public abstract class TransformerPlugin extends Mapper implements MixinConfigPlugin {
    private static final Function<String, Object2ReferenceOpenHashMap<String, ContextMethodTransformer>> mapFunction = (final String name) -> new Object2ReferenceOpenHashMap<>();

    protected transient Object2ReferenceOpenHashMap<String, ContextMixinTransformer> preMixinTransformers = new Object2ReferenceOpenHashMap<>();
    protected transient Object2ReferenceOpenHashMap<String, ContextMixinTransformer> postMixinTransformers = new Object2ReferenceOpenHashMap<>();
    protected transient Object2ReferenceOpenHashMap<String, Object2ReferenceOpenHashMap<String, ContextMethodTransformer>> preMixinMethodTransformers = new Object2ReferenceOpenHashMap<>();
    protected transient Object2ReferenceOpenHashMap<String, Object2ReferenceOpenHashMap<String, ContextMethodTransformer>> postMixinMethodTransformers = new Object2ReferenceOpenHashMap<>();

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
            final Object2ReferenceOpenHashMap<String, ContextMethodTransformer> methodTransformers = this.preMixinMethodTransformers.get(targetClassName);

            if (!methodTransformers.isEmpty()) {
                for (final MethodNode method : targetClass.methods) {
                    final ContextMethodTransformer transformer = methodTransformers.remove(method.name);

                    if (transformer != null) {
                        transformer.transform(method, targetClassName, targetClass, mixinClassName, mixinInfo);
                    }
                }
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
            final Object2ReferenceOpenHashMap<String, ContextMethodTransformer> methodTransformers = this.postMixinMethodTransformers.get(targetClassName);

            if (!methodTransformers.isEmpty()) {
                for (final MethodNode method : targetClass.methods) {
                    final ContextMethodTransformer transformer = methodTransformers.remove(method.name);

                    if (transformer != null) {
                        transformer.transform(method, targetClassName, targetClass, mixinClassName, mixinInfo);
                    }
                }
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

    protected void registerPreMixinMethodTransformer(final String targetBinaryName, final String methodName, final ContextMethodTransformer transformer) {
        this.verify();

        if (this.preMixinMethodTransformers.computeIfAbsent(targetBinaryName, mapFunction).put(methodName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a pre-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPreMixinMethodTransformer(final String targetBinaryName, final String methodName, final ClassMethodTransformer transformer) {
        this.verify();

        if (this.preMixinMethodTransformers.computeIfAbsent(targetBinaryName, mapFunction).put(methodName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a pre-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPreMixinMethodTransformer(final String targetBinaryName, final String methodName, final MethodTransformer transformer) {
        this.verify();

        if (this.preMixinMethodTransformers.computeIfAbsent(targetBinaryName, mapFunction).put(methodName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a pre-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPostMixinMethodTransformer(final String targetBinaryName, final String methodName, final ContextMethodTransformer transformer) {
        this.verify();

        if (this.postMixinMethodTransformers.computeIfAbsent(targetBinaryName, mapFunction).put(methodName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a post-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPostMixinMethodTransformer(final String targetBinaryName, final String methodName, final ClassMethodTransformer transformer) {
        this.verify();

        if (this.postMixinMethodTransformers.computeIfAbsent(targetBinaryName, mapFunction).put(methodName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a post-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    protected void registerPostMixinMethodTransformer(final String targetBinaryName, final String methodName, final MethodTransformer transformer) {
        this.verify();

        if (this.postMixinMethodTransformers.computeIfAbsent(targetBinaryName, mapFunction).put(methodName, transformer) != null) {
            throw new IllegalArgumentException(String.format("a post-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
        }
    }

    private void verify() {
        if (!this.canRegister) {
            throw new IllegalStateException("getMixins() was already called for this plugin.");
        }
    }
}
