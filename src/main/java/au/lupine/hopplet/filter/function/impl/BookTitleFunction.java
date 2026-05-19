package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class BookTitleFunction implements Matcher<String> {

    @Override
    public @NonNull String name() {
        return "book_title";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("title");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.book_title.description");
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
    public boolean matches(@NonNull Context context, @NonNull String title) {
        if (!(context.stack().getItemMeta() instanceof BookMeta meta)) return false;

        return meta.getTitle() != null && meta.getTitle().equals(title);
    }
}
