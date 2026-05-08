package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class IsStackableFunction implements Predicate {

    @Override
    public @NonNull String name() {
        return "is_stackable";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "stackable",
            "can_stack"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.is_stackable.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public boolean test(@NonNull Context context, @NonNull Void compiled) {
        return context.stack().getType().getMaxStackSize() > 1;
    }
}
