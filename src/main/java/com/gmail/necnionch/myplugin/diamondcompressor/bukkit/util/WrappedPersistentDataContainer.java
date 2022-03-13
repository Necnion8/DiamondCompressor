package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.util;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.DiamondCompressorPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrappedPersistentDataContainer {
    private final PersistentDataContainer container;

    private WrappedPersistentDataContainer(PersistentDataContainer container) {
        this.container = container;
    }

    public PersistentDataContainer getContainer() {
        return container;
    }

    private NamespacedKey keyOf(String key) {
        return new NamespacedKey(DiamondCompressorPlugin.getInstance(), key);
    }

    public static WrappedPersistentDataContainer of(PersistentDataContainer container) {
        return new WrappedPersistentDataContainer(container);
    }

    public static WrappedPersistentDataContainer of(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItemMeta() == null)
            return null;
        return new WrappedPersistentDataContainer(itemStack.getItemMeta().getPersistentDataContainer());
    }


    public <T, Z> void set(@NotNull String key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
        container.set(keyOf(key), type, value);
    }

    public <T, Z> boolean has(@NotNull String key, @NotNull PersistentDataType<T, Z> type) {
        return container.has(keyOf(key), type);
    }

    @Nullable
    public <T, Z> Z get(@NotNull String key, @NotNull PersistentDataType<T, Z> type) {
        return container.get(keyOf(key), type);
    }

    @NotNull
    public <T, Z> Z getOrDefault(@NotNull String key, @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue) {
        return container.getOrDefault(keyOf(key), type, defaultValue);
    }

    public void remove(@NotNull String key) {
        container.remove(keyOf(key));
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }

    @NotNull
    public PersistentDataAdapterContext getAdapterContext() {
        return container.getAdapterContext();
    }

}
