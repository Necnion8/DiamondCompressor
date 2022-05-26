package com.gmail.necnionch.myplugin.diamondcompressor.bukkit.listeners;

import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.CompressedDiamond;
import com.gmail.necnionch.myplugin.diamondcompressor.bukkit.DiamondCompressorPlugin;
import com.gmail.necnionch.myplugin.metacraftingapi.bukkit.events.MetaCraftPrepareItemCraftEvent;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CraftListener implements Listener {
    private final DiamondCompressorPlugin plugin;

    public CraftListener(DiamondCompressorPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(MetaCraftPrepareItemCraftEvent event) {
        CompressedDiamond compressed = CompressedDiamond.from(event.getResult());
        if (compressed != null && !compressed.getConfig().isCraftable()) {
            event.setAction(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

}
