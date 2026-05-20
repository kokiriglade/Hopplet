package au.lupine.hopplet.filter.context;

import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class HopperPickUpItemContext implements ItemEntityContext, InventoryTransferContext {

    private final @NonNull Item item;
    private final @NonNull Inventory destination;

    public HopperPickUpItemContext(@NonNull Item item, @NonNull Inventory destination) {
        this.item = item;
        this.destination = destination;
    }

    @Override
    public @NonNull ItemStack stack() {
        return item.getItemStack();
    }

    @Override
    public @NonNull Item item() {
        return item;
    }

    @Override
    public @Nullable Inventory source() {
        return null;
    }

    @Override
    public @NonNull Inventory destination() {
        return destination;
    }
}
