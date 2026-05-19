package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.function.Function;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class FunctionRegisteredEvent extends Event {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private final @NonNull Function<?> function;

    public FunctionRegisteredEvent(@NonNull Function<?> function) {
        this.function = function;
    }

    public @NonNull Function<?> function() {
        return function;
    }

    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
