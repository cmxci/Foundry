package com.github.cmxci.foundry.data;

import net.minecraft.util.Identifier;

@FunctionalInterface
public interface FoundryRegistryFallback<T> {
    T get(FoundryRegistry<T> registry, Identifier identifier);
}
