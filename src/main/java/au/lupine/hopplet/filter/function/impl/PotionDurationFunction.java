package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import au.lupine.hopplet.util.Comparator;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class PotionDurationFunction implements Matcher<PotionDurationFunction.Argument> {

    @Override
    public @NonNull String name() {
        return "potion_duration";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "duration",
            "pot_dur"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.potion_duration.description");
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

        if (normalised.equals("infinite") || normalised.equals("unlimited"))
            return new Argument(Comparator.EQUAL_TO, PotionEffect.INFINITE_DURATION);

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

        int ticks = ticks(value, argument);
        return new Argument(comparator, ticks);
    }

    public record Argument(@NonNull Comparator comparator, int ticks) {}

    @Override
    public boolean matches(@NonNull Context context, @NonNull Argument argument) {
        ItemStack stack = context.stack();

        if (!(stack.getItemMeta() instanceof PotionMeta meta)) return false;

        List<PotionEffect> effects = new ArrayList<>();

        PotionType base = meta.getBasePotionType();
        if (base != null) effects.addAll(base.getPotionEffects());

        effects.addAll(meta.getCustomEffects());
        if (effects.isEmpty()) return false;

        for (PotionEffect effect : effects) {
            int duration = effect.getDuration();

            if (duration == PotionEffect.INFINITE_DURATION) {
                if (argument.ticks == PotionEffect.INFINITE_DURATION) return true;
                if (argument.comparator == Comparator.GREATER_THAN || argument.comparator == Comparator.GREATER_THAN_OR_EQUAL_TO) return true;

                continue;
            }

            if (argument.ticks == PotionEffect.INFINITE_DURATION) continue;

            if (argument.comparator.compare(duration, argument.ticks)) return true;
        }

        return false;
    }

    private static int ticks(@NonNull String text, @NonNull String argument) {
        if (text.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_duration.compilation.exception.no_duration_specified",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        int ticks;
        try {
            ticks = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_duration.compilation.exception.invalid_duration",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        if (ticks < -1) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_duration.compilation.exception.less_than_negative_one",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        return ticks;
    }
}
