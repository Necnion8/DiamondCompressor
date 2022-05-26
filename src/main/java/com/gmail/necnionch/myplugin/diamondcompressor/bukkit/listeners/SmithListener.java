package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.listeners;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.CompressedDiamond;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.DiamondCompressorPlugin;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.events.CompressedDiamondEnhanceLevelEvent;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.events.MetaCraftPrepareSmithEvent;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.events.SlotItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SmithListener implements Listener {
    private final DiamondCompressorPlugin plugin;

    public SmithListener(DiamondCompressorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepare(MetaCraftPrepareSmithEvent event) {
        if (!Event.Result.DEFAULT.equals(event.getAction()))
            return;

        SlotItem[] slots = event.getSlots();
        ItemStack itemStack = preCraft(slots);
        if (itemStack != null) {
            event.setResult(itemStack);
            event.setAction(Event.Result.ALLOW);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilComplete(InventoryClickEvent event) {
        if (event.isCancelled())
            return;

        try {
            craft(event);

        } catch (Throwable e) {
            if (!(e instanceof NoSuchAction)) {
                plugin.getLogger().log(Level.SEVERE, "Failed to smithing event", e);
            }
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }

    }

    private ItemStack preCraft(SlotItem[] slots) {
        ItemStack source = slots[0].getItemStack();
        if (source == null || !Material.ENCHANTED_BOOK.equals(source.getType()) || !(source.getItemMeta() instanceof EnchantmentStorageMeta))
            return null;
        if (!(slots[1].getCustomItem() instanceof CompressedDiamond))
            return null;

        CompressedDiamond compressed = (CompressedDiamond) slots[1].getCustomItem();
        if (compressed == null)
            return null;

        EnchantmentStorageMeta enchantBook = (EnchantmentStorageMeta) source.getItemMeta();
        if (!enchantBook.hasStoredEnchants())
            return null;

        if (enchantBook.getStoredEnchants().entrySet().stream()
                .filter(e -> e.getKey().getMaxLevel() <= e.getValue())  // 最大レベルorそれ以上か
                .noneMatch(e -> plugin.getPluginConfig().isAllowEnchantLevelUp(e.getKey(), e.getValue()))  // 上限レベルではないか
        ) return null;

        PersistentDataContainer data = enchantBook.getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, "apply_anvil"), PersistentDataType.STRING, compressed.getItemId());  // todo: lore tuika
        source = source.clone();
        source.setItemMeta(enchantBook);
        return source;
    }

    private void craft(InventoryClickEvent event) {
        HumanEntity clicker = event.getWhoClicked();
        if (!(clicker instanceof Player))
            return;

        if (!(event.getInventory() instanceof SmithingInventory))
            return;

        if (event.getRawSlot() != event.getView().convertSlot(event.getRawSlot()) || event.getRawSlot() != 2)
            return;

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getItemMeta() == null)
            return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        NamespacedKey dataKey = new NamespacedKey(plugin, "apply_anvil");
        String compressedCustomId = data.get(dataKey, PersistentDataType.STRING);
        data.remove(dataKey);

        CompressedDiamond compressed = CompressedDiamond.from(compressedCustomId);
        if (compressed != null) {
            // apply compressed
            float rate = compressed.getConfig().getOverEnchantRate();

            if (itemMeta instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta enchants = (EnchantmentStorageMeta) itemMeta;
                // 最大レベルor超えているenchをまとめる
                List<Enchantment> entries = enchants.getStoredEnchants().entrySet().stream()
                        .filter(e -> e.getKey().getMaxLevel() <= e.getValue())
                        .filter(e -> plugin.getPluginConfig().isAllowEnchantLevelUp(e.getKey(), e.getValue()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                if (!entries.isEmpty()) {
                    // どれか一つ選ぶ
                    Enchantment target = entries.get(new Random().nextInt(entries.size()));
                    int level = enchants.getStoredEnchantLevel(target);
                    int newLevel = level;
                    boolean result;
//                    System.out.println("SEL " + target.getKey());

                    if (new Random().nextFloat() <= rate) {
                        newLevel++;
                        result = true;
                    } else {
                        newLevel = 1;
                        result = false;
                    }
                    enchants.addStoredEnchant(target, newLevel, true);

                    plugin.getServer().getPluginManager().callEvent(new CompressedDiamondEnhanceLevelEvent(
                            ((Player) clicker).getPlayer(),
                            ((SmithingInventory) event.getInventory()),
                            itemStack, compressed, target, level, newLevel, result
                    ));

                } else {
                    throw new NoSuchAction();  // cancelling smith
                }

            }
        }
        itemStack.setItemMeta(itemMeta);
        event.setResult(Event.Result.ALLOW);
        event.setCursor(itemStack);
        event.setCurrentItem(null);
        ItemStack item = event.getInventory().getItem(0);
        if (item != null)
            item.setAmount(item.getAmount() - 1);
        item = event.getInventory().getItem(1);
        if (item != null)
            item.setAmount(item.getAmount() - 1);
    }


    private static class NoSuchAction extends Error {}

}
