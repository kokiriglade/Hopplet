package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.function.Function;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

public final class PreFunctionRegisterEvent extends Event implements Cancellable {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    private final @NonNull Function<?> function;

    public PreFunctionRegisterEvent(@NonNull Function<?> function) {
        this.function = function;
    }

    public @NonNull Function<?> function() {
        return function;
    }

    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NonNull HandlerList getHandlers() {
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
