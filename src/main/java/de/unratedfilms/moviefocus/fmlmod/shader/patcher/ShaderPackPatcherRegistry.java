
package de.unratedfilms.moviefocus.fmlmod.shader.patcher;

import com.google.common.collect.ImmutableList;

public class ShaderPackPatcherRegistry {

    private static ImmutableList<ShaderPackPatcher> patchers = ImmutableList.of();

    public static ImmutableList<ShaderPackPatcher> getAll() {

        return patchers;
    }

    public static void register(ShaderPackPatcher patcher) {

        patchers = ImmutableList.<ShaderPackPatcher> builder().addAll(patchers).add(patcher).build();
    }

    private ShaderPackPatcherRegistry() {}

}
