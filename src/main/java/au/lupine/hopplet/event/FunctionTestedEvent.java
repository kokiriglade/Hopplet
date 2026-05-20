package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.function.Function;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class FunctionTestedEvent<ArgumentType> extends Event {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private final @NonNull Function<?> function;
    private final @NonNull Context context;
    private final @NonNull ArgumentType argument;
    private final boolean result;

    public FunctionTestedEvent(@NonNull Function<?> function, @NonNull Context context, @NonNull ArgumentType argument, boolean result) {
        this.function = function;
        this.context = context;
        this.argument = argument;
        this.result = result;
    }

    public @NonNull Function<?> function() {
        return function;
    }

    public @NonNull Context context() {
        return context;
    }

    public @NonNull ArgumentType argument() {
        return argument;
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
