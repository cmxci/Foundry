package com.github.cmxci.foundry.data;

import com.github.cmxci.foundry.Foundry;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import net.minecraft.util.Identifier;

public class FoundryCodecRegistry<T> implements FoundryRequirableRegistry {

    private final BiMap<Identifier, Codec<? extends T>> codecs = HashBiMap.create();
    private final FoundryCodecRegistryFallback<T> codecFallback;
    private final Identifier name;

    public FoundryCodecRegistry(Identifier name, FoundryCodecRegistryFallback<T> codecFallback) {
        this.name = name;
        this.codecFallback = codecFallback;
    }

    public <U extends T> void register(Identifier id, Codec<U> codec) {
        codecs.put(id, codec);
        Foundry.checkPostInitialization();
    }

    public Codec<? extends T> codec(Identifier id) {
        return codecs.get(id) == null ? codecFallback.get(this, id) : codecs.get(id);
    }

    public Identifier of(Codec<? extends T> codec) {
        return codecs.inverse().get(codec);
    }

    public Identifier name() {
        return name;
    }

    @Override
    public boolean contains(Identifier id) {
        return codecs.containsKey(id);
    }
}
