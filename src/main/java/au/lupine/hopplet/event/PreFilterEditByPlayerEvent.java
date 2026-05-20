package au.lupine.hopplet.event;

import au.lupine.hopplet.filter.Filter;
import au.lupine.hopplet.filter.edit.EditTarget;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class PreFilterEditByPlayerEvent extends Event implements Cancellable {

    private static final @NonNull HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;

    private final @NonNull Player player;
    private final @NonNull EditTarget target;
    private final @NonNull String raw;
    private final @Nullable Filter filter;

    private @NonNull Component message = Component.translatable("hopplet.filter.compilation.exception.edit_cancelled");

    public PreFilterEditByPlayerEvent(@NonNull Player player, @NonNull EditTarget target, @NonNull String raw, @Nullable Filter filter) {
        this.player = player;
        this.target = target;
        this.raw = raw;
        this.filter = filter;
    }

    public @NonNull Player player() {
        return player;
    }

    public @NonNull EditTarget target() {
        return target;
    }

    public @NonNull String raw() {
        return raw;
    }

    /// Filter is null if {@link #raw()} is blank, or if it did not successfully compile.
    /// Hopplet is designed to change the name of an edit target even if it did not successfully compile to avoid losing edits.
    public @Nullable Filter filter() {
        return filter;
    }

    public void message(@NonNull Component message) {
        this.message = message;
    }

    public @NonNull Component message() {
        return message;
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
