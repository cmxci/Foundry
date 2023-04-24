package com.github.cmxci.foundry.material;

import com.github.cmxci.foundry.Foundry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.EnumMap;

public class FoundryArmorMaterial extends FoundryMaterial implements ArmorMaterial, FoundryArmorMaterialConvertible {

    private final int durabilityMultiplier;
    private static final EnumMap<EquipmentSlot, Integer> BASE_DURABILITY = Util.make(new EnumMap<EquipmentSlot, Integer>(EquipmentSlot.class), map -> {
        map.put(EquipmentSlot.FEET, 13);
        map.put(EquipmentSlot.LEGS, 15);
        map.put(EquipmentSlot.CHEST, 16);
        map.put(EquipmentSlot.HEAD, 11);
    });
    private final EnumMap<EquipmentSlot, Integer> protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Item repairItem;

    public static final Codec<FoundryArmorMaterial> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter(FoundryArmorMaterial::getName),
                    Codec.INT.fieldOf("durability_multiplier").forGetter(FoundryArmorMaterial::getDurabilityMultiplier),
                    Codec.INT.fieldOf("protection_helmet").forGetter((FoundryArmorMaterial m) -> m.getProtectionAmount(EquipmentSlot.HEAD)),
                    Codec.INT.fieldOf("protection_chestplate").forGetter((FoundryArmorMaterial m) -> m.getProtectionAmount(EquipmentSlot.CHEST)),
                    Codec.INT.fieldOf("protection_leggings").forGetter((FoundryArmorMaterial m) -> m.getProtectionAmount(EquipmentSlot.LEGS)),
                    Codec.INT.fieldOf("protection_boots").forGetter((FoundryArmorMaterial m) -> m.getProtectionAmount(EquipmentSlot.FEET)),
                    Codec.INT.fieldOf("enchantability").forGetter(FoundryArmorMaterial::getEnchantability),
                    Codec.FLOAT.fieldOf("toughness").forGetter(FoundryArmorMaterial::getToughness),
                    Codec.FLOAT.fieldOf("knockback_resistance").forGetter(FoundryArmorMaterial::getKnockbackResistance),
                    Codec.STRING.fieldOf("repair_ingredient").forGetter((FoundryArmorMaterial m) -> m.getRepairIngredient().toString()),
                    Codec.STRING.fieldOf("equip_sound").forGetter((FoundryArmorMaterial m) -> m.getEquipSound().getId().toString())
            ).apply(instance, (String name, Integer durabilityMultiplier, Integer helmetProtection, Integer chestplateProtection, Integer leggingsProtection, Integer bootsProtection, Integer enchantability, Float toughness, Float knockbackResistance, String repairIngredient, String equipSound) -> new FoundryArmorMaterial(
                    new Identifier(name),
                    durabilityMultiplier,
                    Util.make(new EnumMap<EquipmentSlot, Integer>(EquipmentSlot.class), map -> {
                        map.put(EquipmentSlot.FEET, bootsProtection);
                        map.put(EquipmentSlot.LEGS, leggingsProtection);
                        map.put(EquipmentSlot.CHEST, chestplateProtection);
                        map.put(EquipmentSlot.HEAD, helmetProtection);
                    }),
                    enchantability,
                    new SoundEvent(new Identifier(equipSound)),
                    toughness,
                    knockbackResistance,
                    Registry.ITEM.get(new Identifier(repairIngredient))
            ))
    );

    public FoundryArmorMaterial(Identifier name, int durabilityMultiplier, EnumMap<EquipmentSlot, Integer> protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Item repairItem) {
        super(name);
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairItem = repairItem;
    }

    public Item getRepairItem() {
        return this.repairItem;
    }

    public int getDurabilityMultiplier() {
        return this.durabilityMultiplier;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY.get(slot) * durabilityMultiplier;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return this.protectionAmounts.get(slot);
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(this.repairItem);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public static Identifier type() {
        return Foundry.of("armor_material");
    }

    @Override
    public FoundryArmorMaterial armorMaterial() {
        return this;
    }
}
