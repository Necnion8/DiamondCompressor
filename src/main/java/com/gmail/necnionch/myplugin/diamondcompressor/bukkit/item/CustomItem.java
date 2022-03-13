package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.util.WrappedPersistentDataContainer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;


public abstract class CustomItem implements CreatableItemStack {

    @NotNull
    public abstract String getId();

    @NotNull
    public abstract ItemStack createItemStack();

    public abstract boolean equalsItemType(@Nullable ItemStack itemStack);


    public static void applyPersistentData(boolean update, ItemStack itemStack, Consumer<WrappedPersistentDataContainer> consumer) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        consumer.accept(WrappedPersistentDataContainer.of(meta.getPersistentDataContainer()));
        if (update)
            itemStack.setItemMeta(meta);
    }

    public static void applyPersistentData(ItemStack itemStack, Consumer<WrappedPersistentDataContainer> consumer) {
        ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());
        consumer.accept(WrappedPersistentDataContainer.of(meta.getPersistentDataContainer()));
    }

    public static void applyMetaData(boolean update, ItemStack itemStack, Consumer<ItemMeta> consumer) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            consumer.accept(meta);
            if (update)
                itemStack.setItemMeta(meta);
        }
    }

}
