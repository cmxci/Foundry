package com.github.cmxci.foundry.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

@FunctionalInterface
public interface FoundryCodecRegistryFallback<T> {

    Codec<T> get(FoundryCodecRegistry<T> registry, Identifier identifier);
}
