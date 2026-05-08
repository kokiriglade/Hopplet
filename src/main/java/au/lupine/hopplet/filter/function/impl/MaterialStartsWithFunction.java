package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class MaterialStartsWithFunction implements Matcher<String> {

    @Override
    public @NonNull String name() {
        return "material_starts_with";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("type_starts_with");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.material_starts_with.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull String parse(@NonNull String argument) throws FilterCompileException {
        return argument;
    }

    @Override
    public boolean matches(@NonNull FilterContext context, @NonNull String argument) {
        String name = context.stack().getType().getKey().getKey();
        return name.startsWith(argument);
    }
}