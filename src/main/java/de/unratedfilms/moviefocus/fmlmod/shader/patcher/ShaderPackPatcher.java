
package de.unratedfilms.moviefocus.fmlmod.shader.patcher;

import java.io.IOException;
import java.io.InputStream;
import shadersmod.client.IShaderPack;

public interface ShaderPackPatcher {

    public boolean isShaderPackSupported(IShaderPack shaderPack) throws IOException;

    public InputStream patchResourceStream(String resourcePath, InputStream resourceStream) throws IOException;

}
