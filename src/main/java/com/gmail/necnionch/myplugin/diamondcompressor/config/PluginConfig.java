package com.gmail.necnionch.myplugin.diamondcompressor.config;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.CompressedDiamond;
import com.gmail.necnionch.myplugin.diamondcompressor.common.BukkitConfigDriver;
import com.google.common.collect.Maps;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class PluginConfig extends BukkitConfigDriver {
    private final Map<NamespacedKey, Integer> enchantLevelLimits = Maps.newHashMap();

    public PluginConfig(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onLoaded(FileConfiguration config) {
        if (super.onLoaded(config)) {
            loadCompressedItems();
            loadEnchantLevelLimits();
            return true;
        }
        return false;
    }


    private void loadCompressedItems() {
        for (CompressedDiamond compressed : CompressedDiamond.ITEMS) {
            ConfigurationSection section = config.getConfigurationSection(String.format("compressed.x%ddiamond", compressed.getDiamondTotalAmount()));
            if (section != null) {
                compressed.getConfig().craftable = section.getBoolean("craftable", false);
                compressed.getConfig().overEnchantRate = (float) section.getDouble("overenchant-rate", 0);
            } else {
                compressed.getConfig().craftable = false;
                compressed.getConfig().overEnchantRate = 0f;
            }
        }
    }

    private void loadEnchantLevelLimits() {
        enchantLevelLimits.clear();
        ConfigurationSection section = config.getConfigurationSection("enchant-level-limits");
        if (section == null)
            return;
        for (String enchName : section.getKeys(false)) {
            NamespacedKey key = NamespacedKey.minecraft(enchName);
            int level = section.getInt(enchName);
            Enchantment enchantment = Enchantment.getByKey(key);
            if (level > 0 && enchantment != null) {
                enchantLevelLimits.put(enchantment.getKey(), level);
            } else {
                getLogger().warning("Unknown enchantment: " + enchName);
            }
        }
    }

    public int getMaxEnchantLevel(NamespacedKey enchantKey) {
        return enchantLevelLimits.getOrDefault(enchantKey, -1);
    }

    public boolean isAllowEnchantLevelUp(Enchantment enchantment, int level) {
        if (enchantment == null)
            return false;
        int maxLimit = getMaxEnchantLevel(enchantment.getKey());
        return maxLimit == -1 || level < maxLimit;
    }


    public static class Compressed {
        private boolean craftable;
        private float overEnchantRate;

        public Compressed() {}

        public Compressed(boolean craftable, float overEnchantRate) {
            this.craftable = craftable;
            this.overEnchantRate = overEnchantRate;
        }

        public boolean isCraftable() {
            return craftable;
        }

        public float getOverEnchantRate() {
            return overEnchantRate;
        }

    }

}
