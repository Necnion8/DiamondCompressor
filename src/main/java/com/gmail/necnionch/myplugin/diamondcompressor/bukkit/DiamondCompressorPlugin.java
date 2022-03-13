package com.gmail.necnionch.myplugin.diamondcompressor.bukkit;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.items.CompressedDiamondX4;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.Set;

public final class DiamondCompressorPlugin extends JavaPlugin implements Listener {
    private static DiamondCompressorPlugin instance;
    private final CustomItemManager customItemManager = new CustomItemManager(this);

    private final Set<NamespacedKey> customRecipes = Sets.newHashSet();

    @Override
    public void onLoad() {
        instance = this;
    }

    public void test() {

        CompressedDiamondX4 item = customItemManager.getItem(CompressedDiamondX4.class);

        if (item != null)
            item.hello();

        CompressedDiamondX4 dia4x = new CompressedDiamondX4(null);

        customItemManager.addItem(dia4x);

    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(customItemManager, this);
        getServer().getPluginManager().registerEvents(this, this);

        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("This is a Diamond");

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(new NamespacedKey(this, "id"), PersistentDataType.STRING, "this");

        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "example_diamond"), item);
        recipe.shape("dd", "dd");
        recipe.setIngredient('d', Material.DIAMOND);
        Bukkit.addRecipe(recipe);
        customRecipes.add(recipe.getKey());

    }

    @Override
    public void onDisable() {
        Recipe recipe;
        for (Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            recipe = it.next();
            if (recipe instanceof Keyed && customRecipes.remove(((Keyed) recipe).getKey())) {
                it.remove();
            }
        }

        customItemManager.unregisterAll();
    }

    public static DiamondCompressorPlugin getInstance() {
        return instance;
    }

    public CustomItemManager getItems() {
        return customItemManager;
    }


    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        getLogger().info("PrepareItemCraftEvent");

        Recipe recipe = event.getRecipe();
        InventoryView view = event.getView();
        CraftingInventory crafting = event.getInventory();
        ItemStack result = crafting.getResult();
        ItemStack[] contents = crafting.getContents();

//        if (recipe instanceof Keyed && customRecipes.contains(((Keyed) recipe).getKey()))
//            return;  // creatable

        for (ItemStack item : crafting.getMatrix()) {
            if (item != null && item.getItemMeta() != null) {
                getLogger().info("item : " + item.getType().name());
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                if (data.has(new NamespacedKey(this, "id"), PersistentDataType.STRING)) {
                    crafting.setResult(null);
                    getLogger().info("cancelled");
                    return;
                }
            }
        }
    }

}
