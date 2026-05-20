package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.Filter;
import au.lupine.hopplet.filter.context.Context;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class PreFilterTestEvent extends Event implements Cancellable {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    private final @NonNull Filter filter;
    private final @NonNull Context context;

    public PreFilterTestEvent(@NonNull Filter filter, @NonNull Context context) {
        this.filter = filter;
        this.context = context;
    }

    public @NonNull Filter filter() {
        return filter;
    }

    public @NonNull Context context() {
        return context;
    }

    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
