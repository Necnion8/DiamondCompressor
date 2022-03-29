package com.gmail.necnionch.myplugin.diamondcompressor.bukkit;

import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class CompressedDiamond extends CustomItem {
    private final String itemId;
    private final int diamondTotalAmount;
    private final String name;

    public static final CompressedDiamond X4 = new CompressedDiamond("compressed_diamond_x4", 4, "4倍圧縮ダイヤ");
    public static final CompressedDiamond X16 = new CompressedDiamond("compressed_diamond_x16", 16, "16倍圧縮ダイヤ");
    public static final CompressedDiamond X64 = new CompressedDiamond("compressed_diamond_x64", 64, "64倍圧縮ダイヤ");
    public static final CompressedDiamond X256 = new CompressedDiamond("compressed_diamond_x256", 256, "256倍圧縮ダイヤ");
    public static final CompressedDiamond X1024 = new CompressedDiamond("compressed_diamond_x1024", 1024, "1024倍圧縮ダイヤ");

    public CompressedDiamond(String itemId, int diamondTotalAmount, String name) {
        this.itemId = itemId;
        this.diamondTotalAmount = diamondTotalAmount;
        this.name = name;
    }


    @NotNull
    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public @NotNull Material getRecipeMaterial() {
        return Material.DIAMOND;
    }

    public int getDiamondTotalAmount() {
        return diamondTotalAmount;
    }

    @Override
    public @NotNull ItemStack create() {
        ItemStack itemStack = new ItemStack(getRecipeMaterial());
        ItemMeta meta = itemStack.getItemMeta();

//        meta.setCustomModelData(customModel);
        meta.setDisplayName(ChatColor.AQUA + name);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
