package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class HasArmorTrimFunction implements Predicate {

    @Override
    public @NonNull String name() {
        return "has_armor_trim";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "is_trimmed",
            "trimmed"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.has_armor_trim.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public boolean test(@NonNull FilterContext context, @NonNull Void compiled) {
        return context.stack().getItemMeta() instanceof ArmorMeta meta && meta.hasTrim();
    }
}
