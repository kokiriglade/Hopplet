package au.lupine.hopplet.listener;

import au.lupine.hopplet.filter.edit.EditDialog;
import au.lupine.hopplet.filter.edit.HopperEditTarget;
import au.lupine.hopplet.filter.edit.HopperMinecartEditTarget;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

public final class FilterEditListener implements Listener {

    @EventHandler
    public void on(@NonNull PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("hopplet.dialog.edit_filter.use")) return;

        if (!event.getAction().isRightClick()) return;

        if (!player.isSneaking()) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        if (!(block.getState(false) instanceof Hopper hopper)) return;

        BlockBreakEvent bbe = new BlockBreakEvent(block, player);
        if (!bbe.callEvent()) return; // Player does not have permission to edit this hopper

        event.setCancelled(true);
        EditDialog.open(player, new HopperEditTarget(hopper));
    }

    @EventHandler
    public void on(@NonNull PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("hopplet.dialog.edit_filter.use")) return;

        if (!(event.getRightClicked() instanceof HopperMinecart hopper)) return;

        if (!player.isSneaking()) return;

        BlockBreakEvent bbe = new BlockBreakEvent(hopper.getLocation().getBlock(), player);
        if (!bbe.callEvent()) return;

        event.setCancelled(true);
        EditDialog.open(player, new HopperMinecartEditTarget(hopper));
    }

    @EventHandler
    public void on(@NonNull PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        ItemStack result = inventory.getResult();
        if (result == null) return;

        if (result.getType() != Material.HOPPER_MINECART) return;

        ItemStack hopper = Arrays.stream(inventory.getMatrix())
            .filter(item -> item != null && item.getType() == Material.HOPPER)
            .findFirst().orElse(null);

        if (hopper == null) return;

        Component name = hopper.effectiveName();

        ItemMeta meta = result.getItemMeta();

        // We could check if it has a filter, but its finnicky since it may not compile correctly.
        // And using the magic style doesn't work as item names are italicised, annoyingly.
        meta.customName(name);
        result.setItemMeta(meta);

        inventory.setResult(result);
    }
}
