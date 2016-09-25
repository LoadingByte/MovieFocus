
package de.unratedfilms.moviefocus.coremod.asm;

import org.objectweb.asm.tree.ClassNode;

public interface ClassNodeTransformer {

    public void transform(ClassNode classNode);

}
