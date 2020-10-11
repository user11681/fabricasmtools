package user11681.fabricasmtools;

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import java.util.List;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public abstract class TransformerPlugin extends Mapper implements MixinConfigPlugin {
    protected transient Object2ReferenceOpenHashMap<String, MixinTransformer> registeredPreMixinTransformers = new Object2ReferenceOpenHashMap<>();
    protected transient Object2ReferenceOpenHashMap<String, MixinTransformer> registeredPostMixinTransformers = new Object2ReferenceOpenHashMap<>();
//    protected transient ReferenceArrayList<ClassNode> generatedMixins = ReferenceArrayList.wrap(new ClassNode[1], 0);

    protected boolean canRegister = true;

    protected String internalPackageName;

    @Override
    public void onLoad(String mixinPackage) {
        if (mixinPackage == null) {
            throw new NullPointerException("Mixin package must be specified in Mixin configuration");
        }

        this.internalPackageName = mixinPackage.replace('.', '/');
    }

    @Override
    public List<String> getMixins() {
        this.canRegister = false;

        return null;
    }

    @Override
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
        final MixinTransformer transformer = this.registeredPreMixinTransformers.remove(targetClassName);

        if (transformer != null) {
            transformer.transform(targetClassName, targetClass, mixinClassName, mixinInfo);

            if (this.registeredPreMixinTransformers.size() == 0) {
                this.registeredPreMixinTransformers = null;
            }
        }
    }

    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
        final MixinTransformer transformer = this.registeredPostMixinTransformers.remove(targetClassName);

        if (transformer != null) {
            transformer.transform(targetClassName, targetClass, mixinClassName, mixinInfo);

            if (this.registeredPostMixinTransformers.size() == 0) {
                this.registeredPostMixinTransformers = null;
            }
        }
    }

    protected /*ClassNode*/ void registerPreMixinTransformer(final String targetBinaryName, final MixinTransformer transformer/*, final boolean generateMixin*/) {
        if (this.canRegister) {
            if (this.registeredPreMixinTransformers.put(targetBinaryName, transformer) != null) {
                throw new IllegalArgumentException(String.format("a pre-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
            }
        } else {
            throw new IllegalStateException("getMixins() was already called for this plugin.");
        }

//        if (generateMixin) {
//            return generateMixin(targetBinaryName);
//        }
//
//        return null;
    }

    protected /*ClassNode*/ void registerPostMixinTransformer(final String targetBinaryName, final MixinTransformer transformer/*, final boolean generateMixin*/) {
        if (this.canRegister) {
            if (this.registeredPostMixinTransformers.put(targetBinaryName, transformer) != null) {
                throw new IllegalArgumentException(String.format("a post-Mixin transformer for class %s was already registered by this plugin.", targetBinaryName));
            }
        } else {
            throw new IllegalStateException("getMixins() was already called for this plugin.");
        }

//        if (generateMixin) {
//            return generateMixin(targetBinaryName);
//        }
//
//        return null;
    }

/*
    @SuppressWarnings("ConstantConditions")
    protected ClassNode generateMixin(final String targetBinaryName) {
        try {
            ClassNode klass = new ClassNode();
            new ClassReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(targetBinaryName.replace('.', '/') + ".class")).accept(klass, ClassReader.SKIP_CODE);

            final int type = klass.access & (Opcodes.ACC_INTERFACE | Opcodes.ACC_ENUM);
            final String internalName = targetBinaryName.replace('.', '/');

            klass = new ClassNode();
            klass.visit(Opcodes.V1_8, type + Opcodes.ACC_SUPER, this.internalPackageName + "/" + internalName + "SyntheticMixin", null, "java/lang/Object", null);
            klass.visitAnnotation("Lorg/spongepowered/asm/mixin/Mixin;", false).visit("value", ReferenceArrayList.wrap(new Type[]{Type.getType("L" + internalName + ";")}));

            this.generatedMixins.add(klass);

            return klass;
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }
*/

    // concern
/*
        try {
            final ReferenceArrayList<String> names = ReferenceArrayList.wrap(new String[this.generatedMixins.size()], 0);
            final String source = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

            for (final ClassNode mixin : this.generatedMixins) {
                final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                final File classFile = new File(source, mixin.name + ".class");

                classFile.getParentFile().mkdirs();

                mixin.accept(writer);
                names.add(mixin.name.replace(this.internalPackageName + '/', "").replace('/', '.'));

                final String fileString = classFile.toString();

                if (source.endsWith(".jar")) {
                    final JarOutputStream output = new JarOutputStream(new FileOutputStream(source, true));

                    output.putNextEntry(new ZipEntry(mixin.name + ".class"));
                    output.write(writer.toByteArray());
                    output.closeEntry();
                    output.close();
                } else {
                    IOUtils.write(writer.toByteArray(), new FileOutputStream(fileString));
                }
            }

            this.generatedMixins = null;

            return names;
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
*/
}
