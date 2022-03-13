package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.recipe;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.item.CreatableItemStack;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CustomRecipe {

    private final NamespacedKey key;
    private final String[] shapes;
    private final Map<Character, RecipeIngredient> shapesIngredient;
    private final CreatableItemStack result;


    private CustomRecipe(Builder builder) {
        key = builder.namespacedKey;
        shapes = builder.shapes;
        shapesIngredient = builder.shapesIngredient;
        result = builder.result;
    }


    public static Builder builder(NamespacedKey namespacedKey) {
        return new Builder(namespacedKey);
    }

    public NamespacedKey getKey() {
        return key;
    }

    public String[] getShapes() {
        return shapes;
    }

    public Map<Character, RecipeIngredient> shapesIngredient() {
        return shapesIngredient;
    }

    public CreatableItemStack getResult() {
        return result;
    }


    public final static class Builder {
        private final NamespacedKey namespacedKey;
        private String[] shapes;
        private final Map<Character, RecipeIngredient> shapesIngredient = Maps.newHashMap();
        private CreatableItemStack result;

        public Builder(NamespacedKey namespacedKey) {
            this.namespacedKey = namespacedKey;
        }

        public CustomRecipe create() {
            return new CustomRecipe(this);
        }


        public Builder shape(@NotNull final String... shape) {
            shapes = shape;
            return this;
        }

        public Builder setIngredient(char key, RecipeIngredient ingredient) {
            shapesIngredient.put(key, ingredient);
            return this;
        }

        public Builder setIngredient(char key, Material material) {
            return setIngredient(key, ingredientBy(material));
        }

        public Builder setResult(CreatableItemStack itemStack) {
            result = itemStack;
            return this;
        }

        public Builder setResult(Material material) {
            result = new CreatableItemStack() {
                @Override
                public @NotNull ItemStack createItemStack() {
                    return new ItemStack(material);
                }

                @Override
                public @NotNull Material getMaterial() {
                    return material;
                }
            };
            return this;
        }


        private RecipeIngredient ingredientBy(Material material) {
            return new RecipeIngredient() {
                public boolean equalsItemType(ItemStack itemStack) { return true; }
                public Material getMaterial() { return material; }
            };
        }


    }



    public static final CreatableItemStack RESULT_THIS = new CreatableItemStack() {
        public @NotNull ItemStack createItemStack() { return new ItemStack(getMaterial()); }
        public @NotNull Material getMaterial() { return Material.AIR; }
    };
    public static final RecipeIngredient INGREDIENT_THIS = new RecipeIngredient() {
        public boolean equalsItemType(ItemStack itemStack) { return false; }
        public Material getMaterial() { return null; }
    };

}
