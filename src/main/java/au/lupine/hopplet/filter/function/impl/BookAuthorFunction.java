package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class BookAuthorFunction implements Matcher<String> {

    @Override
    public @NonNull String name() {
        return "book_author";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("author");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.book_author.description");
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
        ItemStack stack = context.stack();
        if (!(stack.getItemMeta() instanceof BookMeta meta)) return false;

        String author = meta.getAuthor();
        if (author == null) return false;

        return argument.equals(author);
    }
}
