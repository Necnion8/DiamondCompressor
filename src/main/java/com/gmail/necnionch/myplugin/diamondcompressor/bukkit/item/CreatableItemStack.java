package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CreatableItemStack {
    @NotNull ItemStack createItemStack();
    @NotNull Material getMaterial();
}
