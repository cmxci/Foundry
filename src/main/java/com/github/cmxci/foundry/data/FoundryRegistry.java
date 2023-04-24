package com.github.cmxci.foundry.data;

import com.github.cmxci.foundry.Foundry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FoundryRegistry<T> implements FoundryRequirableRegistry {

    private final Map<Identifier, T> map = new HashMap<Identifier, T>();
    private final FoundryRegistryFallback<T> fallback;
    private final Identifier name;

    public FoundryRegistry(Identifier name, FoundryRegistryFallback<T> fallback) {
        this.name = name;
        this.fallback = fallback;
    }

    public <U extends T> void register(Identifier id, U obj) {
        map.put(id, obj);
        Foundry.checkPostInitialization();
    }

    public <U extends T> void register(Identifier id, Function<Identifier, U> f) {
        U u = f.apply(id);
        map.put(id, u);
        Foundry.checkPostInitialization();
    }

    public T get(Identifier id) {
        return map.get(id) == null ? fallback.get(this, id) : map.get(id);
    }

    public Identifier name() {
        return name;
    }

    @Override
    public boolean contains(Identifier id) {
        return map.containsKey(id);
    }
}
