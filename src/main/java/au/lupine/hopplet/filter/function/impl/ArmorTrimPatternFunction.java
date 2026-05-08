package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Function;
import au.lupine.hopplet.filter.function.Matcher;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class ArmorTrimPatternFunction implements Matcher<TrimPattern> {

    private static final Registry<TrimPattern> TRIM_PATTERN_REGISTRY = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN);

    @Override
    public @NonNull String name() {
        return "armor_trim_pattern";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("trim_pattern");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.armor_trim_pattern.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull TrimPattern parse(@NonNull String argument) throws FilterCompileException {
        NamespacedKey key = NamespacedKey.fromString(argument.toLowerCase());

        Function.checkKey(key, argument);

        TrimPattern pattern = TRIM_PATTERN_REGISTRY.get(key);
        if (pattern == null) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.armor_trim_pattern.compilation.exception.unknown_trim_pattern",
                    Argument.string("input", argument)
                )
            );
        }

        return pattern;
    }

    @Override
    public boolean matches(@NonNull FilterContext context, @NonNull TrimPattern pattern) {
        if (!(context.stack().getItemMeta() instanceof ArmorMeta meta)) return false;

        ArmorTrim trim = meta.getTrim();
        if (trim == null) return false;

        return trim.getPattern().equals(pattern);
    }
}
