package com.github.cmxci.foundry.item;

import com.github.cmxci.foundry.material.FoundryMaterial;
import net.minecraft.util.Identifier;

public abstract class FoundryItemGeneratorBase {
    private final String suffix;

    protected FoundryItemGeneratorBase(String suffix) {
        this.suffix = suffix;
    }

    protected Identifier itemName(FoundryMaterial material) {
        return new Identifier(material.getIdentifier().getNamespace(), String.format("%s_%s", material.getIdentifier().getPath(), suffix));
    }
}
