package com.github.cmxci.foundry.material;

import com.github.cmxci.foundry.Foundry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FoundryToolMaterial extends FoundryMaterial implements ToolMaterial, FoundryToolMaterialConvertible {

    private final int durability;
    private final float miningSpeedMultiplier;
    private final float attackDamage;
    private final int axeDamageModifier;
    private final int miningLevel;
    private final int enchantability;
    private final Item repairItem;

    public static final Codec<FoundryToolMaterial> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter((FoundryToolMaterial m) -> m.getIdentifier().toString()),
                    Codec.INT.fieldOf("durability").forGetter(FoundryToolMaterial::getDurability),
                    Codec.FLOAT.fieldOf("mining_speed_multiplier").forGetter(FoundryToolMaterial::getMiningSpeedMultiplier),
                    Codec.FLOAT.fieldOf("attack_damage").forGetter(FoundryToolMaterial::getAttackDamage),
                    Codec.INT.optionalFieldOf("axe_damage_modifier", 0).forGetter(FoundryToolMaterial::getAxeDamageModifier),
                    Codec.INT.fieldOf("mining_level").forGetter(FoundryToolMaterial::getMiningLevel),
                    Codec.INT.fieldOf("enchantability").forGetter(FoundryToolMaterial::getEnchantability),
                    Codec.STRING.fieldOf("repair_ingredient").forGetter((FoundryToolMaterial m) -> m.getRepairIngredient().toString())
            ).apply(instance, (String name, Integer durability, Float miningSpeedMultiplier, Float attackDamage, Integer axeDamageModifer, Integer miningLevel, Integer enchantability, String repairIngredient) -> new FoundryToolMaterial(
                    new Identifier(name),
                    durability,
                    miningSpeedMultiplier,
                    attackDamage,
                    axeDamageModifer,
                    miningLevel,
                    enchantability,
                    Registry.ITEM.get(new Identifier(repairIngredient))
            ))
    );

    public FoundryToolMaterial(Identifier name, int durability, float miningSpeedMultiplier, float attackDamage, int axeDamageModifier, int miningLevel, int enchantability, Item repairItem) {
        super(name);
        this.durability = durability;
        this.miningSpeedMultiplier = miningSpeedMultiplier;
        this.attackDamage = attackDamage;
        this.axeDamageModifier = axeDamageModifier;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repairItem = repairItem;
    }

    public Item getRepairItem() {
        return this.repairItem;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeedMultiplier;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getAxeDamageModifier() {
        return this.axeDamageModifier;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(this.repairItem);
    }

    public static Identifier type() {
        return Foundry.of("tool_material");
    }

    @Override
    public FoundryToolMaterial toolMaterial() {
        return this;
    }
}
