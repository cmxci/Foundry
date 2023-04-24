package com.github.cmxci.foundry;

import com.github.cmxci.foundry.data.*;
import com.github.cmxci.foundry.item.EmptyItemGenerator;
import com.github.cmxci.foundry.item.FoundryArmorGenerator;
import com.github.cmxci.foundry.item.FoundryItemGenerator;
import com.github.cmxci.foundry.item.FoundryToolGenerator;
import com.github.cmxci.foundry.material.FoundryArmorMaterial;
import com.github.cmxci.foundry.material.FoundryMaterial;
import com.github.cmxci.foundry.material.FoundryMergedMaterial;
import com.github.cmxci.foundry.material.FoundryToolMaterial;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Foundry implements PreLaunchEntrypoint {

    public static final String MODID = "foundry";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final FoundryRegistry<FoundryRequirableRegistry> REQUIRABLE_REGISTRIES = new FoundryRegistry<FoundryRequirableRegistry>(Foundry.of("registries"), Foundry.defaultFallback());
    public static final FoundryCodecRegistry<FoundryMaterial> MATERIAL_TYPES = new FoundryCodecRegistry<FoundryMaterial>(Foundry.of("material_types"), Foundry.defaultCodecFallback());
    public static final FoundryRegistry<FoundryMaterial> MATERIALS = new FoundryRegistry<FoundryMaterial>(Foundry.of("materials"), Foundry.defaultFallback());
    public static final FoundryRegistry<FoundryItemGenerator> ITEM_GENERATORS = new FoundryRegistry<FoundryItemGenerator>(Foundry.of("item_generators"), Foundry.warningFallback(new EmptyItemGenerator()));
    public static final Set<PostInitializationRequirements> postInitialization = new HashSet<PostInitializationRequirements>();

    @Override
    public void onPreLaunch() {
        REQUIRABLE_REGISTRIES.register(REQUIRABLE_REGISTRIES.name(), REQUIRABLE_REGISTRIES);
        REQUIRABLE_REGISTRIES.register(MATERIAL_TYPES.name(), MATERIAL_TYPES);
        REQUIRABLE_REGISTRIES.register(MATERIALS.name(), MATERIAL_TYPES);
        REQUIRABLE_REGISTRIES.register(ITEM_GENERATORS.name(), ITEM_GENERATORS);

        MATERIAL_TYPES.register(FoundryArmorMaterial.type(), FoundryArmorMaterial.CODEC);
        MATERIAL_TYPES.register(FoundryToolMaterial.type(), FoundryToolMaterial.CODEC);
        MATERIAL_TYPES.register(FoundryMergedMaterial.type(), FoundryMergedMaterial.CODEC);

        ITEM_GENERATORS.register(Foundry.of("helmet"), new FoundryArmorGenerator.Helmet());
        ITEM_GENERATORS.register(Foundry.of("chestplate"), new FoundryArmorGenerator.Chestplate());
        ITEM_GENERATORS.register(Foundry.of("leggings"), new FoundryArmorGenerator.Leggings());
        ITEM_GENERATORS.register(Foundry.of("boots"), new FoundryArmorGenerator.Boots());

        ITEM_GENERATORS.register(Foundry.of("sword"), new FoundryToolGenerator.Sword());
        ITEM_GENERATORS.register(Foundry.of("pickaxe"), new FoundryToolGenerator.Pickaxe());
        ITEM_GENERATORS.register(Foundry.of("axe"), new FoundryToolGenerator.Axe());
        ITEM_GENERATORS.register(Foundry.of("shovel"), new FoundryToolGenerator.Shovel());
        ITEM_GENERATORS.register(Foundry.of("hoe"), new FoundryToolGenerator.Hoe());
    }

    public static Identifier of(String name) {
        return new Identifier("foundry", name);
    }

    public static Set<Item> create(Identifier material, Identifier... generators) {
        Set<Item> set = new HashSet<Item>();
        for (Identifier i : generators) set.add(ITEM_GENERATORS.get(i).create(MATERIALS.get(material)));
        return set;
    }

    public static PostInitializationRequirements postInitialization(Runnable r) {
        return new PostInitializationRequirements(r);
    }

    public static void checkPostInitialization() {
        postInitialization.removeIf(PostInitializationRequirements::run);
    }

    public static final Identifier[] DEFAULT_GENERATORS = {
            Foundry.of("helmet"),
            Foundry.of("chestplate"),
            Foundry.of("leggings"),
            Foundry.of("boots"),

            Foundry.of("sword"),
            Foundry.of("pickaxe"),
            Foundry.of("axe"),
            Foundry.of("shovel"),
            Foundry.of("hoe")
    };

    public static <T> FoundryRegistryFallback<T> defaultFallback() {
        return (r, i) -> {
            LOGGER.error("Cannot locate identifier \"{}\" in registry \"{}\"", i.toString(), r.name().toString());
            return null;
        };
    }

    public static <T> FoundryRegistryFallback<T> warningFallback(T arg) {
        return (r, i) -> {
            LOGGER.warn("Cannot locate identifier \"{}\" in registry \"{}\"", i.toString(), r.name().toString());
            return arg;
        };
    }

    public static <T> FoundryCodecRegistryFallback<T> defaultCodecFallback() {
        return (r, i) -> {
            LOGGER.error("Cannot locate identifier \"{}\" in codec registry \"{}\"", i.toString(), r.name().toString());
            return null;
        };
    }

    public static class PostInitializationRequirements {
        private final Runnable runnable;
        private final Map<Identifier, Set<Identifier>> requirements = new HashMap<Identifier, Set<Identifier>>();

        public PostInitializationRequirements(Runnable runnable) {
            this.runnable = runnable;
        }

        public PostInitializationRequirements addRequirements(Identifier registry, Identifier object) {
            this.requirements.putIfAbsent(registry, new HashSet<Identifier>());
            this.requirements.get(registry).add(object);
            return this;
        }

        public void add() {
            Foundry.postInitialization.add(this);
        }

        public boolean run() {
            for (Map.Entry<Identifier, Set<Identifier>> e : this.requirements.entrySet()) {
                for (Identifier i : e.getValue()) {
                    if (!REQUIRABLE_REGISTRIES.get(e.getKey()).contains(i)) return false;
                }
            }
            runnable.run();
            return true;
        }
    }
}
