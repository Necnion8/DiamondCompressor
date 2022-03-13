package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public interface RecipeIngredient {
    boolean equalsItemType(ItemStack itemStack);
    Material getMaterial();

}
