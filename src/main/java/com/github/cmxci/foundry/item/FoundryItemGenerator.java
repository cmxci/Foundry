package com.github.cmxci.foundry.item;

import com.github.cmxci.foundry.material.FoundryMaterial;
import net.minecraft.item.Item;

public interface FoundryItemGenerator {

    Item create(FoundryMaterial material);
}
