package com.github.cmxci.foundry.item;

import com.github.cmxci.foundry.material.FoundryMaterial;
import net.minecraft.item.Item;

public class EmptyItemGenerator implements FoundryItemGenerator {

    @Override
    public Item create(FoundryMaterial material) {
        return new Item(new Item.Settings());
    }
}
