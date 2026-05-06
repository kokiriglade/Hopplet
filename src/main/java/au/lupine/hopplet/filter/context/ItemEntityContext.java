package au.lupine.hopplet.filter.context;

import au.lupine.hopplet.filter.Filter;
import org.bukkit.entity.Item;
import org.jspecify.annotations.NonNull;

public interface ItemEntityContext extends Filter.Context {

    @NonNull Item item();
}
