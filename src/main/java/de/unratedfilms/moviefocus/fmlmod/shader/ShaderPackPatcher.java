
package de.unratedfilms.moviefocus.fmlmod.shader;

import java.io.IOException;
import java.io.InputStream;
import com.google.common.collect.ImmutableList;
import de.unratedfilms.moviefocus.fmlmod.shader.patchers.SPP_OnlyDof;
import shadersmodcore.client.IShaderPack;

public interface ShaderPackPatcher {

    public static final ImmutableList<ShaderPackPatcher> AVAILABLE_PATCHERS = ImmutableList.<ShaderPackPatcher> builder()
            .add(new SPP_OnlyDof())
            .build();

    public boolean isShaderPackSupported(IShaderPack shaderPack) throws IOException;

    public InputStream patchResourceStream(String resourcePath, InputStream resourceStream) throws IOException;

}
