package au.lupine.hopplet.listener;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.Filter;
import au.lupine.hopplet.filter.cache.Cache;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.context.HopperInventoryTransferContext;
import au.lupine.hopplet.filter.context.HopperPickupItemContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.util.HopperRouting;
import org.bukkit.block.Hopper;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;

public final class HopperInventoryListener implements Listener {

    @EventHandler
    public void on(@NonNull InventoryMoveItemEvent event) {
        Inventory destination = event.getDestination();
        if (destination.getType() != InventoryType.HOPPER) return;

        InventoryHolder holder = destination.getHolder(false);
        if (holder == null) return;

        ConfigurationNode filterNode = Hopplet.instance().config().root().node("filter");

        Filter filter;
        try {
            filter = switch (holder) {
                case Hopper hopper -> Cache.getOrCompile(hopper);
                case HopperMinecart hopper -> Cache.getOrCompile(hopper);
                default -> null;
            };
        } catch (FilterCompileException e) {
            if (filterNode.node("disable_hopper_on_compilation_error").getBoolean(false)) event.setCancelled(true);
            return;
        }

        ItemStack item = event.getItem();
        Inventory source = event.getSource();

        if (filter != null) {
            Context context = new HopperInventoryTransferContext(item, source, destination);

            if (!filter.test(context)) {
                event.setCancelled(true);

                // If the item in the first non-empty slot wasn't accepted by this filter,
                // we'll look to see if any other items might be accepted.
                if (!filterNode.node("transfer_next_applicable_item").getBoolean(true)) return;

                boolean firstItem = true;

                for (ItemStack stack : source.getStorageContents()) {
                    if (stack == null) continue;

                    if (firstItem) { // We already know that the first item was not accepted by this filter, skip it.
                        firstItem = false;
                        continue;
                    }

                    context = new HopperInventoryTransferContext(stack, source, destination);
                    if (!filter.test(context)) continue;

                    ItemStack clone = stack.clone();
                    clone.setAmount(1);

                    HashMap<Integer, ItemStack> remaining = destination.addItem(clone);

                    if (remaining.isEmpty()) {
                        stack.setAmount(stack.getAmount() - 1);
                        return;
                    }
                }
            }

            return;
        }

        // Find a more "applicable" hopper.

        if (!(holder instanceof Hopper destinationHopper)) return;

        Hopper alternative = HopperRouting.alternative(source, destinationHopper);
        if (alternative == null) return; // No other hopper connected to the source was found.

        if (!HopperRouting.enabled(alternative)) return;

        if (!HopperRouting.fits(alternative.getInventory(), item)) return;

        Filter alternativeFilter;
        try {
            alternativeFilter = Cache.getOrCompile(alternative);
        } catch (FilterCompileException e) {
            return;
        }

        if (alternativeFilter == null) return;

        if (alternativeFilter.test(new HopperInventoryTransferContext(item, source, alternative.getInventory()))) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(@NonNull InventoryPickupItemEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.HOPPER) return;

        InventoryHolder holder = inventory.getHolder(false);
        if (holder == null) return;

        Filter filter;
        try {
            filter = switch (holder) {
                case Hopper hopper -> Cache.getOrCompile(hopper);
                case HopperMinecart hopper -> Cache.getOrCompile(hopper);
                default -> null;
            };
        } catch (FilterCompileException e) {
            if (Hopplet.instance().config().root().node("filter", "disable_hopper_on_compilation_error").getBoolean(false)) event.setCancelled(true);
            return;
        }

        if (filter == null) return;

        Context context = new HopperPickupItemContext(event.getItem(), inventory);

        if (!filter.test(context)) event.setCancelled(true);
    }
}
