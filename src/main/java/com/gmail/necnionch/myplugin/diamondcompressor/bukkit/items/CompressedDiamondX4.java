package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.items;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.DiamondCompressorPlugin;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item.CreatableItemStack;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item.CustomItem;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe.Craftable;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe.CustomRecipe;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe.RecipeIngredient;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.util.WrappedPersistentDataContainer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;


public class CompressedDiamondX4 extends CustomItem implements RecipeIngredient, Craftable, CreatableItemStack {
    public static final String ITEM_KEY = "compressed_diamond_x4";
    private @Nullable final Integer customModel;
    private final CustomRecipe[] recipes;


    public CompressedDiamondX4(@Nullable Integer customModel) {
        this.customModel = customModel;
        this.recipes = new CustomRecipe[] {
                CustomRecipe.builder(new NamespacedKey(DiamondCompressorPlugin.getInstance(), "A"))
                        .shape("dd", "dd")
                        .setIngredient('d', Material.DIAMOND)
                        .setResult(CustomRecipe.RESULT_THIS)
                        .create(),
                CustomRecipe.builder(new NamespacedKey(DiamondCompressorPlugin.getInstance(), "B"))
                        .shape("i")
                        .setIngredient('i', CustomRecipe.INGREDIENT_THIS)
                        .setResult(Material.DIAMOND)
                        .create()
        };

    }


    @Override
    public @NotNull String getId() {
        return ITEM_KEY;
    }

    @Override
    public boolean equalsItemType(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null)
            return false;

        WrappedPersistentDataContainer container = WrappedPersistentDataContainer.of(itemStack);
        if (container == null || container.isEmpty())
            return false;

        return ITEM_KEY.equalsIgnoreCase(container.get("id", PersistentDataType.STRING));
    }

    @Override
    @NotNull
    public Material getMaterial() {
        return Material.DIAMOND;
    }

    @Override
    @NotNull
    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(getMaterial());

        applyMetaData(true, itemStack, meta -> {
            meta.setCustomModelData(customModel);
            // todo: lore & name
        });
        applyPersistentData(true, itemStack, data -> {
            data.set("id", PersistentDataType.STRING, ITEM_KEY);
        });

        return itemStack;
    }

    @Override
    public Iterable<CustomRecipe> getRecipes() {
        return Arrays.asList(recipes);
    }

    public void hello() {}

}
