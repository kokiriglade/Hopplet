package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import au.lupine.hopplet.util.Comparator;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class PotionEffectFunction implements Matcher<PotionEffectFunction.Argument> {

    private static final Registry<PotionEffectType> POTION_EFFECT_TYPE_REGISTRY = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT);

    @Override
    public @NonNull String name() {
        return "potion_effect";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "effect",
            "pot_eff",
            "eff"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.potion_effect.description");
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

            PotionEffectType effect = effect(normalised.substring(0, index), argument);
            int level = amplifier(normalised.substring(index + symbol.length()), argument);

            if (level < 1) {
                throw new FilterCompileException(
                    Component.translatable(
                        "hopplet.filter.function.potion_effect.compilation.exception.amplifier_must_be_positive",
                        net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                    )
                );
            }

            return new Argument(effect, comparator, level);
        }

        PotionEffectType effect = effect(normalised, argument);
        return new Argument(effect, null, null);
    }

    public record Argument(@NonNull PotionEffectType effect, @Nullable Comparator comparator, @Nullable Integer amplifier) {}

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
            if (effect.getType() != argument.effect) continue;

            if (argument.comparator == null || argument.amplifier == null) return true;

            if (argument.comparator.compare(effect.getAmplifier() + 1, argument.amplifier)) return true;
        }

        return false;
    }

    private static @NonNull PotionEffectType effect(@NonNull String name, @NonNull String argument) {
        if (name.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_effect.compilation.exception.no_potion_effect_specified",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        NamespacedKey key = NamespacedKey.fromString(name);
        PotionEffectType effect = key == null ? null : POTION_EFFECT_TYPE_REGISTRY.get(key);
        if (effect == null) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_effect.compilation.exception.unknown_potion_effect",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        return effect;
    }

    private static int amplifier(@NonNull String text, @NonNull String argument) {
        if (text.isEmpty()) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_effect.compilation.exception.no_amplifier_specified",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_effect.compilation.exception.invalid_amplifier",
                    net.kyori.adventure.text.minimessage.translation.Argument.string("input", argument)
                )
            );
        }
    }
}
