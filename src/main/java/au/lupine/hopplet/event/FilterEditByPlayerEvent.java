package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.Filter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class FilterEditByPlayerEvent extends Event {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private final @NonNull Player player;
    private final @NonNull String raw;
    private final @Nullable Filter filter;

    public FilterEditByPlayerEvent(@NonNull Player player, @NonNull String raw, @Nullable Filter filter) {
        this.player = player;
        this.raw = raw;
        this.filter = filter;
    }

    public @NonNull Player player() {
        return player;
    }

    public @NonNull String raw() {
        return raw;
    }

    public @Nullable Filter filter() {
        return filter;
    }

    public static @NonNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
