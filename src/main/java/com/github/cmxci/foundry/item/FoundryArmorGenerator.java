package com.github.cmxci.foundry.item;

import com.github.cmxci.foundry.Foundry;
import com.github.cmxci.foundry.material.FoundryArmorMaterial;
import com.github.cmxci.foundry.material.FoundryArmorMaterialConvertible;
import com.github.cmxci.foundry.material.FoundryMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public abstract class FoundryArmorGenerator extends FoundryItemGeneratorBase implements FoundryItemGenerator {

    protected FoundryArmorGenerator(String suffix) {
        super(suffix);
    }

    @Override
    public Item create(FoundryMaterial material) {
        if (!(material instanceof FoundryArmorMaterialConvertible m)) {
            Foundry.LOGGER.error("Cannot convert material \"{}\" to an armor material", material.getName());
            return new Item(new Item.Settings());
        }
        return Registry.register(Registry.ITEM, this.itemName(m.armorMaterial()), this.getItem(m.armorMaterial()));
    }

    protected abstract Item getItem(FoundryArmorMaterial material);

    public static class Helmet extends FoundryArmorGenerator {

        public Helmet() {
            super("helmet");
        }

        public Helmet(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryArmorMaterial material) {
            return new ArmorItem(material, EquipmentSlot.HEAD, new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));
        }
    }

    public static class Chestplate extends FoundryArmorGenerator {

        public Chestplate() {
            super("chestplate");
        }

        public Chestplate(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryArmorMaterial material) {
            return new ArmorItem(material, EquipmentSlot.CHEST, new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));
        }
    }

    public static class Leggings extends FoundryArmorGenerator {

        public Leggings() {
            super("leggings");
        }

        public Leggings(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryArmorMaterial material) {
            return new ArmorItem(material, EquipmentSlot.LEGS, new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));
        }
    }

    public static class Boots extends FoundryArmorGenerator {

        public Boots() {
            super("boots");
        }

        public Boots(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryArmorMaterial material) {
            return new ArmorItem(material, EquipmentSlot.FEET, new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));
        }
    }
}
