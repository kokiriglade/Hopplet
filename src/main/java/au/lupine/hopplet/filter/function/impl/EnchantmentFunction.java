package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import au.lupine.hopplet.util.Comparator;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public final class EnchantmentFunction implements Matcher<EnchantmentFunction.Argument> {

    private static final Registry<Enchantment> ENCHANTMENT_REGISTRY = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);

    @Override
    public @NonNull String name() {
        return "enchantment";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "ench",
            "enchant"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.enchantment.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull MatchStrategy<Argument> strategy() {
        return MatchStrategy.all();
    }

    @Override
    public @NonNull Argument parse(@NonNull String argument) throws FilterCompileException {
        String normalised = argument.toLowerCase().replaceAll("\\s", "");

        for (Comparator comparator : Comparator.values()) {
            String symbol = comparator.symbol();
            int index = normalised.indexOf(symbol);
            if (index < 0) continue;

            Enchantment enchantment = enchantment(normalised.substring(0, index), argument);
            int level = level(normalised.substring(index + symbol.length()), argument);

            if (level < 1) {
                throw new FilterCompileException(
                    Component.translatable(
                        "hopplet.filter.function.enchantment.compilation.exception.level_must_be_positive",
                        net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                    )
                );
            }

            return new Argument(enchantment, comparator, level);
        }

        Enchantment enchantment = enchantment(normalised, argument);
        return new Argument(enchantment, null, null);
    }

    public record Argument(@NonNull Enchantment enchantment, @Nullable Comparator comparator, @Nullable Integer level) {}

    @Override
    public boolean matches(@NonNull FilterContext context, @NonNull Argument argument) {
        ItemStack stack = context.stack();

        Map<Enchantment, Integer> enchantments = stack.getItemMeta() instanceof EnchantmentStorageMeta meta
            ? meta.getStoredEnchants()
            : stack.getEnchantments();

        if (enchantments.isEmpty()) return false;

        Integer level = enchantments.get(argument.enchantment);
        if (level == null) return false;

        if (argument.comparator == null || argument.level == null) return true;

        return argument.comparator.compare(level, argument.level);
    }

    private static @NonNull Enchantment enchantment(@NonNull String name, @NonNull String argument) {
        if (name.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.enchantment.compilation.exception.no_enchantment_specified",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        NamespacedKey key = NamespacedKey.fromString(name);
        Enchantment enchantment = key == null ? null : ENCHANTMENT_REGISTRY.get(key);
        if (enchantment == null) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.enchantment.compilation.exception.unknown_enchantment",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        return enchantment;
    }

    private static int level(@NonNull String text, @NonNull String argument) {
        if (text.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.enchantment.compilation.exception.no_level_specified",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.enchantment.compilation.exception.invalid_level",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }
    }
}
