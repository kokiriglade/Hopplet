package au.lupine.hopplet.filter.context;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public final class HopperInventoryTransferContext implements InventoryTransferContext {

    private final @NonNull ItemStack stack;
    private final @NonNull Inventory source;
    private final @NonNull Inventory destination;

    public HopperInventoryTransferContext(@NonNull ItemStack stack, @NonNull Inventory source, @NonNull Inventory destination) {
        this.stack = stack;
        this.source = source;
        this.destination = destination;
    }

    @Override
    public @NonNull ItemStack stack() {
        return stack;
    }

    @Override
    public @NonNull Inventory source() {
        return source;
    }

    @Override
    public @NonNull Inventory destination() {
        return destination;
    }
}
