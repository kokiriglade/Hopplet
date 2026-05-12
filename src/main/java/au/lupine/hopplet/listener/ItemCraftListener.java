package au.lupine.hopplet.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

public final class ItemCraftListener implements Listener {

    @EventHandler
    public void copyNameToMinecart(@NonNull PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        ItemStack result = inventory.getResult();
        if (result == null) return;

        if (result.getType() != Material.HOPPER_MINECART) return;

        ItemStack hopper = Arrays.stream(inventory.getMatrix())
            .filter(item -> item != null && item.getType() == Material.HOPPER)
            .findFirst().orElse(null);

        if (hopper == null) return;

        ItemMeta hopperMeta = hopper.getItemMeta();
        if (!hopperMeta.hasCustomName()) return;

        ItemMeta meta = result.getItemMeta();

        // We could check if it has a filter, but its finnicky since it may not compile correctly.
        // And using the magic style doesn't work as item names are italicised, annoyingly.
        meta.customName(hopperMeta.customName());
        result.setItemMeta(meta);

        inventory.setResult(result);
    }
}
