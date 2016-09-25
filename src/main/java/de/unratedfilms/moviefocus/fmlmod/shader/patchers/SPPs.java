
package de.unratedfilms.moviefocus.fmlmod.shader.patchers;

import com.google.common.collect.ImmutableList;
import de.unratedfilms.moviefocus.fmlmod.shader.ShaderPackPatcher;

public class SPPs {

    public static final ImmutableList<ShaderPackPatcher> PATCHERS = ImmutableList.<ShaderPackPatcher> builder()

            .add(new SPP_OnlyDof())

            .build();

}
