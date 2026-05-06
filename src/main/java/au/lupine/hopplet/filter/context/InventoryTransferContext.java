package au.lupine.hopplet.filter.context;

import au.lupine.hopplet.filter.Filter;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface InventoryTransferContext extends Filter.Context {

    @Nullable Inventory source();

    @NonNull Inventory destination();
}
