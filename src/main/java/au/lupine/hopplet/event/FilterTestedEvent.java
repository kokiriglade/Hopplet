package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.Filter;
import au.lupine.hopplet.filter.context.Context;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class FilterTestedEvent extends Event {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private final @NonNull Filter filter;
    private final @NonNull Context context;
    private final boolean result;

    public FilterTestedEvent(@NonNull Filter filter, @NonNull Context context, boolean result) {
        this.filter = filter;
        this.context = context;
        this.result = result;
    }

    public @NonNull Filter filter() {
        return filter;
    }

    public @NonNull Context context() {
        return context;
    }

    public boolean result() {
        return result;
    }

    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
