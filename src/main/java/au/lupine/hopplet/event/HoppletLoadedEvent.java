package au.lupine.hopplet.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public final class HoppletLoadedEvent extends Event {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
