package com.github.cmxci.foundry.material;

import com.github.cmxci.foundry.Foundry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.EnumMap;

public class FoundryMergedMaterial extends FoundryMaterial implements FoundryArmorMaterialConvertible, FoundryToolMaterialConvertible {

    private final FoundryArmorMaterial armorMaterial;
    private final FoundryToolMaterial toolMaterial;

    public static final Codec<FoundryMergedMaterial> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.STRING.fieldOf("name").forGetter((FoundryMergedMaterial m) -> m.toolMaterial().toString()),
                    Codec.INT.fieldOf("durability").forGetter((FoundryMergedMaterial m) -> m.toolMaterial().getDurability()),
                    Codec.FLOAT.fieldOf("mining_speed_multiplier").forGetter((FoundryMergedMaterial m) -> m.toolMaterial().getMiningSpeedMultiplier()),
                    Codec.FLOAT.fieldOf("attack_damage").forGetter((FoundryMergedMaterial m) -> m.toolMaterial().getAttackDamage()),
                    Codec.INT.fieldOf("axe_damage_modifier").forGetter((FoundryMergedMaterial m) -> m.toolMaterial().getAxeDamageModifier()),
                    Codec.INT.fieldOf("mining_level").forGetter((FoundryMergedMaterial m) -> m.toolMaterial().getMiningLevel()),
                    Codec.INT.fieldOf("durability_multiplier").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getDurabilityMultiplier()),
                    Codec.INT.fieldOf("protection_helmet").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getProtectionAmount(EquipmentSlot.HEAD)),
                    Codec.INT.fieldOf("protection_chestplate").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getProtectionAmount(EquipmentSlot.CHEST)),
                    Codec.INT.fieldOf("protection_leggings").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getProtectionAmount(EquipmentSlot.LEGS)),
                    Codec.INT.fieldOf("protection_boots").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getProtectionAmount(EquipmentSlot.FEET)),
                    Codec.INT.fieldOf("enchantability").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getEnchantability()),
                    Codec.FLOAT.fieldOf("toughness").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getToughness()),
                    Codec.FLOAT.fieldOf("knockback_resistance").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getKnockbackResistance()),
                    Codec.STRING.fieldOf("repair_ingredient").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getRepairItem().toString()),
                    Codec.STRING.fieldOf("equip_sound").forGetter((FoundryMergedMaterial m) -> m.armorMaterial().getEquipSound().getId().toString())
            ).apply(instance, (String name, Integer durability, Float miningSpeedMultiplier, Float attackDamage, Integer axeDamageModifier, Integer miningLevel, Integer durabilityMultiplier, Integer helmetProtection, Integer chestplateProtection, Integer leggingsProtection, Integer bootsProtection, Integer enchantability, Float toughness, Float knockbackResistance, String repairIngredient, String equipSound) -> new FoundryMergedMaterial(
                    new Identifier(name),
                    new FoundryArmorMaterial(
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
                    ),
                    new FoundryToolMaterial(
                            new Identifier(name),
                            durability,
                            miningSpeedMultiplier,
                            attackDamage,
                            axeDamageModifier,
                            miningLevel,
                            enchantability,
                            Registry.ITEM.get(new Identifier(repairIngredient))
                    )
            ))
    );
    
    public FoundryMergedMaterial(Identifier name, FoundryArmorMaterial armorMaterial, FoundryToolMaterial toolMaterial) {
        super(name);
        this.armorMaterial = armorMaterial;
        this.toolMaterial = toolMaterial;
    }

    @Override
    public FoundryArmorMaterial armorMaterial() {
        return armorMaterial;
    }

    @Override
    public FoundryToolMaterial toolMaterial() {
        return toolMaterial;
    }

    public static Identifier type() {
        return Foundry.of("merged_material");
    }
}
