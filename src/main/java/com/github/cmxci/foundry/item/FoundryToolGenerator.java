package com.github.cmxci.foundry.item;

import com.github.cmxci.foundry.Foundry;
import com.github.cmxci.foundry.material.FoundryMaterial;
import com.github.cmxci.foundry.material.FoundryToolMaterial;
import com.github.cmxci.foundry.material.FoundryToolMaterialConvertible;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;

public abstract class FoundryToolGenerator extends FoundryItemGeneratorBase implements FoundryItemGenerator {
    protected FoundryToolGenerator(String suffix) {
        super(suffix);
    }

    @Override
    public Item create(FoundryMaterial material) {
        if (!(material instanceof FoundryToolMaterialConvertible m)) {
            Foundry.LOGGER.error("Cannot convert material \"{}\" to a tool material", material.getName());
            return new Item(new Item.Settings());
        }
        return Registry.register(Registry.ITEM, this.itemName(m.toolMaterial()), this.getItem(m.toolMaterial()));
    }

    protected abstract Item getItem(FoundryToolMaterial material);

    public static class Sword extends FoundryToolGenerator {

        public Sword() {
            super("sword");
        }

        public Sword(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryToolMaterial material) {
            return new SwordItem(material, 3, -2.4f, new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));
        }
    }

    public static class Pickaxe extends FoundryToolGenerator {

        public Pickaxe() {
            super("pickaxe");
        }

        public Pickaxe(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryToolMaterial material) {
            return new PickaxeItem(material, 1, -2.8F, new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
        }
    }

    public static class Axe extends FoundryToolGenerator {

        public Axe() {
            super("axe");
        }

        public Axe(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryToolMaterial material) {
            return new AxeItem(material, 5 + material.getAxeDamageModifier(), material.getAxeDamageModifier() > 1 ? -3.2F : 3.0F, new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
        }
    }

    public static class Shovel extends FoundryToolGenerator {

        public Shovel() {
            super("shovel");
        }

        public Shovel(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryToolMaterial material) {
            return new ShovelItem(material, 1.5F, -3.0F, new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
        }
    }

    public static class Hoe extends FoundryToolGenerator {

        public Hoe() {
            super("hoe");
        }

        public Hoe(String suffix) {
            super(suffix);
        }

        @Override
        protected Item getItem(FoundryToolMaterial material) {
            return new HoeItem(material, (int)-material.getAttackDamage(), Math.min(-(material.getAttackDamage() - 3), 0.0F), new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
        }
    }
}
