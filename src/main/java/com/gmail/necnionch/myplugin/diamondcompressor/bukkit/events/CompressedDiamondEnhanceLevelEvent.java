package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.events;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.CompressedDiamond;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.jetbrains.annotations.NotNull;

public class CompressedDiamondEnhanceLevelEvent extends Event {
    public static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final SmithingInventory inventory;
    private final ItemStack source;
    private final CompressedDiamond compressedDiamond;
    private final Enchantment enchantment;
    private final int oldLevel;
    private final int newLevel;
    private final boolean result;

    public CompressedDiamondEnhanceLevelEvent(Player player, SmithingInventory inventory, ItemStack source, CompressedDiamond compressed, Enchantment enchantment, int oldLevel, int newLevel, boolean result) {
        this.player = player;
        this.inventory = inventory;
        this.source = source;
        compressedDiamond = compressed;
        this.enchantment = enchantment;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
        this.result = result;
    }


    public Player getPlayer() {
        return player;
    }

    public SmithingInventory getInventory() {
        return inventory;
    }

    public ItemStack getSourceItemStack() {
        return source;
    }

    public CompressedDiamond getCompressedDiamond() {
        return compressedDiamond;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public boolean isSuccess() {
        return result;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
