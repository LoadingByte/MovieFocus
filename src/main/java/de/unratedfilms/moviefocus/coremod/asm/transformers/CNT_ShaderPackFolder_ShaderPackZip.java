
package de.unratedfilms.moviefocus.coremod.asm.transformers;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import de.unratedfilms.moviefocus.coremod.asm.ClassNodeTransformer;
import de.unratedfilms.moviefocus.coremod.asm.util.AsmUtils;
import de.unratedfilms.moviefocus.fmlmod.hooks.ShaderPackHooks;
import de.unratedfilms.moviefocus.fmlmod.shader.patcher.ShaderPackPatcher;
import de.unratedfilms.moviefocus.shared.Consts;

public class CNT_ShaderPackFolder_ShaderPackZip implements ClassNodeTransformer {

    private static final String PATCHER_CLASS_DESC         = Type.getDescriptor(ShaderPackPatcher.class);
    private static final String HOOKS_CLASS_NAME           = Type.getInternalName(ShaderPackHooks.class);

    private static final String PATCHER_FIELD              = Consts.MOD_ID + "patcher";

    private static final Method FIND_PATCHER_HOOK          = Method.getMethod(ShaderPackPatcher.class.getName() + " findPatcher (shadersmodcore.client.IShaderPack)");
    private static final Method PATCH_RESOURCE_STREAM_HOOK = Method.getMethod("java.io.InputStream patchResourceStreamIfPatcherExists (java.io.InputStream, java.lang.String, " + ShaderPackPatcher.class.getName() + ")");

    @Override
    public void transform(ClassNode classNode) {

        addPatcherField(classNode);
        transformConstructor(classNode);
        transformGetResourceAsStream(classNode);
    }

    private void addPatcherField(ClassNode classNode) {

        classNode.fields.add(new FieldNode(ACC_PROTECTED, PATCHER_FIELD, PATCHER_CLASS_DESC, null, null));
    }

    private void transformConstructor(ClassNode classNode) {

        // Find constructor to inject into
        MethodNode constructor = AsmUtils.findMethod(classNode, Method.getMethod("void <init> (java.lang.String, java.io.File)"));

        // Find the injection point inside that constructor; code should be injected directly before the return instruction
        AbstractInsnNode insertBeforeInsn = null;
        for (int index = 0; index < constructor.instructions.size(); index++) {
            AbstractInsnNode instruction = constructor.instructions.get(index);
            if (instruction.getOpcode() == RETURN) {
                insertBeforeInsn = instruction;
                break;
            }
        }

        // Generate the call to ShaderPackHooks.findPatcher()
        InsnList toInjectInsns = new InsnList();
        toInjectInsns.add(new VarInsnNode(ALOAD, 0)); // so that PUTFIELD down below knows it should write into a field from this very object
        toInjectInsns.add(new VarInsnNode(ALOAD, 0)); // the new shader pack is provided to the findPatcher() method as its only argument
        toInjectInsns.add(new MethodInsnNode(INVOKESTATIC, HOOKS_CLASS_NAME, FIND_PATCHER_HOOK.getName(), FIND_PATCHER_HOOK.getDescriptor(), false));
        toInjectInsns.add(new FieldInsnNode(PUTFIELD, classNode.name, PATCHER_FIELD, PATCHER_CLASS_DESC)); // put the returned patcher into the patcher field so it can be used later on

        // Actually inject the new instructions
        constructor.instructions.insertBefore(insertBeforeInsn, toInjectInsns);
    }

    private void transformGetResourceAsStream(ClassNode classNode) {

        // Find method to inject into
        MethodNode method = AsmUtils.findMethod(classNode, Method.getMethod("java.io.InputStream getResourceAsStream (java.lang.String)"));

        // Find the injection point inside that method; code should be injected directly before the first return instruction
        AbstractInsnNode insertBeforeInsn = null;
        for (int index = 0; index < method.instructions.size(); index++) {
            AbstractInsnNode instruction = method.instructions.get(index);
            // We want to patch the input stream, so we add our call to the patcher right before the stream is returned
            if (instruction.getOpcode() == ARETURN) {
                insertBeforeInsn = instruction;
                break;
            }
        }

        // Generate the call to the patcher
        InsnList toInjectInsns = new InsnList();
        toInjectInsns.add(new VarInsnNode(ALOAD, 1)); // the resource path should be the second argument
        toInjectInsns.add(new VarInsnNode(ALOAD, 0)); // so that GETFIELD down below knows it should load from this very object
        toInjectInsns.add(new FieldInsnNode(GETFIELD, classNode.name, PATCHER_FIELD, PATCHER_CLASS_DESC)); // the patcher should be the third argument
        toInjectInsns.add(new MethodInsnNode(INVOKESTATIC, HOOKS_CLASS_NAME, PATCH_RESOURCE_STREAM_HOOK.getName(), PATCH_RESOURCE_STREAM_HOOK.getDescriptor(), false));

        // Actually inject the new instructions
        method.instructions.insertBefore(insertBeforeInsn, toInjectInsns);
    }

}
