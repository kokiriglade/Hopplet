package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class BookGenerationFunction implements Matcher<BookMeta.Generation> {

    @Override
    public @NonNull String name() {
        return "book_generation";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("generation");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.book_generation.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public BookMeta.@NonNull Generation parse(@NonNull String argument) throws FilterCompileException {
        try {
            return BookMeta.Generation.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.book_generation.compilation.exception.unknown_book_generation",
                    Argument.string("input", argument)
                )
            );
        }
    }

    @Override
    public boolean matches(@NonNull Context context, BookMeta.@NonNull Generation argument) {
        ItemStack item = context.stack();
        if (!(item.getItemMeta() instanceof BookMeta meta)) return false;

        BookMeta.Generation generation = meta.getGeneration();
        if (generation == null) generation = BookMeta.Generation.ORIGINAL;

        return generation.equals(argument);
    }
}
