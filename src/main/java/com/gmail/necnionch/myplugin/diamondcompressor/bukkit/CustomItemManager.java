package com.gmail.necnionch.myplugin.diamondcompressor.bukkit;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item.CustomItem;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item.RegisteredCustomItem;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe.Craftable;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe.CustomRecipe;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.util.WrappedPersistentDataContainer;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class CustomItemManager implements Listener {
    private final DiamondCompressorPlugin owner;
    private final Map<Class<?>, RegisteredCustomItem> itemByTypes = Maps.newHashMap();
    private final Map<String, RegisteredCustomItem> itemByIds = Maps.newHashMap();
    private final Map<NamespacedKey, RegisteredCustomItem> itemByRecipeKey = Maps.newHashMap();


    public CustomItemManager(DiamondCompressorPlugin owner) {
        this.owner = owner;
    }

    private Logger getLogger() {
        return owner.getLogger();
    }


    public void addItem(CustomItem item) {
        String itemId = item.getId().toLowerCase(Locale.ROOT);
        Class<? extends CustomItem> itemClass = item.getClass();

        if (itemByIds.containsKey(itemId))
            throw new IllegalArgumentException("already registered item id");
        if (itemByTypes.containsKey(itemClass))
            throw new IllegalArgumentException("already registered custom item class");

        ItemStack itemStack = item.createItemStack();
        Map<CustomRecipe, ShapedRecipe> shapedRecipes = null;

        if (item instanceof Craftable) {
            shapedRecipes = Maps.newHashMap();

            for (CustomRecipe recipe : ((Craftable) item).getRecipes()) {
                NamespacedKey key = recipe.getKey();
                ShapedRecipe shapedRecipe = new ShapedRecipe(key, itemStack);

                shapedRecipe.shape(recipe.getShapes());
                recipe.shapesIngredient().forEach((k, i) -> {
                    if (CustomRecipe.INGREDIENT_THIS.equals(i)) {
                        shapedRecipe.setIngredient(k, item.getMaterial());
                    } else {
                        shapedRecipe.setIngredient(k, i.getMaterial());
                    }
                });
                shapedRecipes.put(recipe, shapedRecipe);
            }
        }


        RegisteredCustomItem registered = new RegisteredCustomItem(item, shapedRecipes, itemStack);

        if (shapedRecipes != null) {
            Set<NamespacedKey> registeredRecipes = Sets.newHashSet();

            shapedRecipes.forEach((customRecipe, recipe) -> {
                NamespacedKey key = customRecipe.getKey();
                try {
                    Bukkit.addRecipe(recipe);
                } catch (IllegalStateException ex) {
                    unregisterRecipes(registeredRecipes);
                    throw ex;
                }
                registeredRecipes.add(key);
            });

            itemByRecipeKey.putAll(shapedRecipes.entrySet().stream()
                    .collect(Collectors.toMap(i -> i.getKey().getKey(), i -> registered)));
        }

        itemByTypes.put(itemClass, registered);
        itemByIds.put(itemId, registered);
    }

    public void removeItem(CustomItem item) {}


    @Nullable
    public <CI extends CustomItem> CI getItem(Class<CI> itemClazz) {
        RegisteredCustomItem registered = itemByTypes.get(itemClazz);
        //noinspection unchecked
        return (registered != null) ? ((CI) registered.getCustomItem()) : null;
    }

    @Nullable
    public RegisteredCustomItem getItem(@Nullable ItemStack itemStack) {
        WrappedPersistentDataContainer container = WrappedPersistentDataContainer.of(itemStack);
        if (container != null && !container.isEmpty()) {
            String itemId = container.get("id", PersistentDataType.STRING);
            if (itemId != null)
                return itemByIds.get(itemId.toLowerCase(Locale.ROOT));
        }
        return null;
    }


    public void unregisterAll() {

    }

    public void unregisterRecipes(Set<NamespacedKey> recipes) {
        Recipe recipe;
        for (Iterator<Recipe> it = Bukkit.recipeIterator(); it.hasNext(); ) {
            recipe = it.next();
            if (recipe instanceof Keyed && recipes.remove(((Keyed) recipe).getKey())) {
                it.remove();
            }
        }
    }


    // events

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        NamespacedKey recipeKey = (recipe instanceof Keyed) ? ((Keyed) recipe).getKey() : null;
        CraftingInventory crafting = event.getInventory();
        ItemStack result = crafting.getResult();
        ItemStack[] matrix = crafting.getMatrix();

        RegisteredCustomItem customItem = itemByRecipeKey.get(recipeKey);
        if (customItem != null) {  // custom item
            CustomRecipe customRecipe = customItem.findRecipeByKey(recipeKey);

            customRecipe  // todo: current work


        } else {  // others
            for (ItemStack item : matrix) {
                customItem = getItem(item);
                if (customItem != null) {
                    crafting.setResult(null);
                    break;
                }
            }

        }
    }



}
