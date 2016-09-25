
package de.unratedfilms.moviefocus.coremod.asm.util;

import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class AsmUtils {

    /*
     * Works just like ReflectionHelper.findMethod(), but for ASM method nodes.
     */
    public static MethodNode findMethod(ClassNode classNode, Method... methodDefs) {

        for (Method methodDef : methodDefs) {
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals(methodDef.getName()) && methodNode.desc.equals(methodDef.getDescriptor())) {
                    return methodNode;
                }
            }
        }

        return null;
    }

    private AsmUtils() {

    }

}
