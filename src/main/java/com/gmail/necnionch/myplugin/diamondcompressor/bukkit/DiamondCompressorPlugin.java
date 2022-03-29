package com.gmail.necnionch.myplugin.diamondcompressor.bukkit;

import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.MetaCraftingAPI;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiamondCompressorPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        registerItems();
    }

    @Override
    public void onDisable() {
        MetaCraftingAPI.unregisterBy(this);
    }


    private void registerItems() {
        MetaCraftingAPI.registerCustomItem(this, CompressedDiamond.X4);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d4_to_d_comped4")
                .shape("dd", "dd")
                .setIngredient('d', Material.DIAMOND)
                .setResult(CompressedDiamond.X4, 1)
                .create());
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped4_to_d4")
                .shape("c")
                .setIngredient('c', CompressedDiamond.X4)
                .setResult(Material.DIAMOND, 4)
                .create());

        MetaCraftingAPI.registerCustomItem(this, CompressedDiamond.X16);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped4_to_d_comped16")
                .shape("dd", "dd")
                .setIngredient('d', CompressedDiamond.X4)
                .setResult(CompressedDiamond.X16, 1)
                .create());
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped16_to_d_comped4")
                .shape("c")
                .setIngredient('c', CompressedDiamond.X16)
                .setResult(CompressedDiamond.X4, 4)
                .create());

        MetaCraftingAPI.registerCustomItem(this, CompressedDiamond.X64);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped16_to_d_comped64")
                .shape("dd", "dd")
                .setIngredient('d', CompressedDiamond.X16)
                .setResult(CompressedDiamond.X64, 1)
                .create());
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped64_to_d_comped16")
                .shape("c")
                .setIngredient('c', CompressedDiamond.X64)
                .setResult(CompressedDiamond.X16, 4)
                .create());

        MetaCraftingAPI.registerCustomItem(this, CompressedDiamond.X256);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped64_to_d_comped256")
                .shape("dd", "dd")
                .setIngredient('d', CompressedDiamond.X64)
                .setResult(CompressedDiamond.X256, 1)
                .create());
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped256_to_d_comped64")
                .shape("c")
                .setIngredient('c', CompressedDiamond.X256)
                .setResult(CompressedDiamond.X64, 4)
                .create());

        MetaCraftingAPI.registerCustomItem(this, CompressedDiamond.X1024);
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped256_to_d_comped1024")
                .shape("dd", "dd")
                .setIngredient('d', CompressedDiamond.X256)
                .setResult(CompressedDiamond.X1024, 1)
                .create());
        MetaCraftingAPI.registerCustomRecipe(this, makeRecipe("d_comped1024_to_d_comped256")
                .shape("c")
                .setIngredient('c', CompressedDiamond.X1024)
                .setResult(CompressedDiamond.X256, 4)
                .create());
    }

    private CustomRecipe.Builder makeRecipe(String key) {
        return CustomRecipe.builder(new NamespacedKey(this, key));
    }

}
