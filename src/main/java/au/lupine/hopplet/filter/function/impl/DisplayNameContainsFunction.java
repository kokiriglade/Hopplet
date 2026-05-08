package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class DisplayNameContainsFunction implements Matcher<String> {

    @Override
    public @NonNull String name() {
        return "display_name_contains";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("name_contains");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.display_name_contains.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull MatchStrategy<String> strategy() {
        return MatchStrategy.all();
    }

    @Override
    public @NonNull String parse(@NonNull String argument) throws FilterCompileException {
        return argument;
    }

    @Override
    public boolean matches(@NonNull Context context, @NonNull String argument) {
        String name = PlainTextComponentSerializer.plainText().serialize(context.stack().effectiveName());
        return name.contains(argument);
    }
}
