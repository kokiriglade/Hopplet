package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.function.Function;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;

public final class PreFunctionTestEvent<ArgumentType> extends Event implements Cancellable {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    private final @NonNull Function<?> function;
    private final @NonNull Context context;
    private final @NonNull ArgumentType argument;

    public PreFunctionTestEvent(@NonNull Function<?> function, @NonNull Context context, @NonNull ArgumentType argument) {
        this.function = function;
        this.context = context;
        this.argument = argument;
    }

    public @NonNull Function<?> filter() {
        return function;
    }

    public @NonNull Context context() {
        return context;
    }

    public @NonNull ArgumentType argument() {
        return argument;
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
