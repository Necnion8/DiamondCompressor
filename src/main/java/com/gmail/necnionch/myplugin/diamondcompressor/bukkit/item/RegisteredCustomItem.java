package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe.CustomRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RegisteredCustomItem {

    private final CustomItem customItem;
    private final Map<CustomRecipe, ShapedRecipe> recipes;
    private final ItemStack sampleItemStack;

    public RegisteredCustomItem(CustomItem customItem, @Nullable Map<CustomRecipe, ShapedRecipe> recipes, ItemStack sampleItemStack) {
        this.customItem = customItem;
        this.recipes = recipes;
        this.sampleItemStack = sampleItemStack;
    }

    public CustomItem getCustomItem() {
        return customItem;
    }

    @Nullable
    public Map<CustomRecipe, ShapedRecipe> recipes() {
        return recipes;
    }

    public ItemStack getSampleItemStack() {
        return sampleItemStack.clone();
    }

    public CustomRecipe findRecipeByKey(NamespacedKey recipeKey) {
        if (recipes != null) {
            for (Map.Entry<CustomRecipe, ShapedRecipe> e : recipes.entrySet()) {
                if (e.getValue().getKey().equals(recipeKey))
                    return e.getKey();
            }
        }
        return null;
    }


}
