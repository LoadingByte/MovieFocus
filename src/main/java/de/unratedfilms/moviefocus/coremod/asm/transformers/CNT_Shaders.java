
package de.unratedfilms.moviefocus.coremod.asm.transformers;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.tree.AbstractInsnNode.LDC_INSN;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import de.unratedfilms.moviefocus.coremod.asm.ClassNodeTransformer;
import de.unratedfilms.moviefocus.coremod.asm.util.AsmUtils;
import de.unratedfilms.moviefocus.fmlmod.shader.ShaderUniforms;
import de.unratedfilms.moviefocus.shared.Consts;

public class CNT_Shaders implements ClassNodeTransformer {

    private static final Method METHOD_SET_PROGRAM_UNIFORM_1I = Method.getMethod("void setProgramUniform1i (java.lang.String, int)");
    private static final Method METHOD_SET_PROGRAM_UNIFORM_1F = Method.getMethod("void setProgramUniform1f (java.lang.String, float)");

    @Override
    public void transform(ClassNode classNode) {

        // Find method to inject into
        MethodNode method = AsmUtils.findMethod(classNode, Method.getMethod("void useProgram (int)"));

        // Find the injection point inside that method; code should be injected after the second last line (before checkGLError)
        AbstractInsnNode insertBeforeInsn = null;
        for (int index = method.instructions.size() - 1; index >= 0; index--) {
            AbstractInsnNode instruction = method.instructions.get(index);
            // This instruction marks the begin of the last line
            if (instruction.getType() == LDC_INSN && ((LdcInsnNode) instruction).cst.equals("useProgram ")) {
                insertBeforeInsn = instruction;
                break;
            }
        }

        // Generate the instructions that should be injected
        InsnList toInjectInsns = new InsnList();
        generatePushVariableInt(classNode, toInjectInsns, "enabled");
        generatePushVariableFloat(classNode, toInjectInsns, "focalDepth");

        // Actually inject the new instructions
        method.instructions.insertBefore(insertBeforeInsn, toInjectInsns);
    }

    private void generatePushVariableInt(ClassNode classNode, InsnList target, String variableName) {

        // Push the uniform name argument
        target.add(new LdcInsnNode(Consts.MOD_ID + "_" + variableName));

        // Push the value argument
        target.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(ShaderUniforms.class), variableName, "I"));

        // Invoke the method
        target.add(new MethodInsnNode(INVOKESTATIC, classNode.name, METHOD_SET_PROGRAM_UNIFORM_1I.getName(), METHOD_SET_PROGRAM_UNIFORM_1I.getDescriptor(), false));
    }

    private void generatePushVariableFloat(ClassNode classNode, InsnList target, String variableName) {

        // Push the uniform name argument
        target.add(new LdcInsnNode(Consts.MOD_ID + "_" + variableName));

        // Push the value argument
        target.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(ShaderUniforms.class), variableName, "F"));

        // Invoke the method
        target.add(new MethodInsnNode(INVOKESTATIC, classNode.name, METHOD_SET_PROGRAM_UNIFORM_1F.getName(), METHOD_SET_PROGRAM_UNIFORM_1F.getDescriptor(), false));
    }

}
