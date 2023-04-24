package com.github.cmxci.foundry.material;

import com.github.cmxci.foundry.Foundry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public abstract class FoundryMaterial {

    private final Identifier name;
    public static final Codec<Codec<? extends FoundryMaterial>> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("type").forGetter((Codec<? extends FoundryMaterial> c) -> Foundry.MATERIAL_TYPES.of(c).toString())
            ).apply(instance, (String type) -> Foundry.MATERIAL_TYPES.codec(new Identifier(type)))
    );

    public FoundryMaterial(Identifier name) {
        this.name = name;
    }

    public Identifier getIdentifier() {
        return this.name;
    }

    public String getName() {
        return name.toString();
    }
}
