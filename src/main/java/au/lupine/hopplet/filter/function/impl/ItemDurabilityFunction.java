package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import au.lupine.hopplet.util.Comparator;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class ItemDurabilityFunction implements Matcher<ItemDurabilityFunction.Argument> {

    @Override
    public @NonNull String name() {
        return "item_durability";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "durability",
            "dur"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.item_durability.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull Argument parse(@NonNull String argument) throws FilterCompileException {
        String normalised = argument.toLowerCase().replaceAll("\\s", "");

        if (normalised.equals("max") || normalised.equals("undamaged")) return new Argument(Comparator.EQUAL_TO, 100);

        Comparator comparator = Comparator.EQUAL_TO;
        String value = normalised;

        for (Comparator c : Comparator.values()) {
            String symbol = c.symbol();

            if (normalised.startsWith(symbol)) {
                comparator = c;
                value = normalised.substring(symbol.length());
                break;
            }
        }

        int durability = durability(value, argument);
        return new Argument(comparator, durability);
    }

    public record Argument(@NonNull Comparator comparator, @Range(from = 0, to = 100) int durability) {}

    @Override
    public boolean matches(@NonNull Context context, @NonNull Argument argument) {
        ItemStack item = context.stack();
        if (!(item.getItemMeta() instanceof Damageable meta)) return false;

        int max = item.getType().getMaxDurability();
        if (max <= 0) return false;

        int remaining = max - meta.getDamage();
        int percentage = (int) (remaining * 100L) / max;

        return argument.comparator.compare(percentage, argument.durability);
    }

    private static int durability(@NonNull String text, @NonNull String argument) {
        if (text.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.item_durability.compilation.exception.no_durability_specified",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        int durability;
        try {
            durability = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.item_durability.compilation.exception.invalid_durability",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        if (durability < 0) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.item_durability.compilation.exception.less_than_zero",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        if (durability > 100) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.item_durability.compilation.exception.greater_than_one_hundred",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        return durability;
    }
}
