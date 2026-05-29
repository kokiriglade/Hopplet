package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class PotionTypeFunction implements Matcher<PotionType> {

    @Override
    public @NonNull String name() {
        return "potion_type";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "pot_type"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.potion_type.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull PotionType parse(@NonNull String argument) throws FilterCompileException {
        NamespacedKey key = NamespacedKey.fromString(argument.toLowerCase());
        PotionType type = key == null ? null : Registry.POTION.get(key);

        if (type == null) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.potion_type.compilation.exception.unknown_potion_type",
                    Argument.string("input", argument)
                )
            );
        }

        return type;
    }

    @Override
    public boolean matches(@NonNull Context context, @NonNull PotionType type) {
        ItemStack stack = context.stack();

        if (!(stack.getItemMeta() instanceof PotionMeta meta)) return false;

        return type.equals(meta.getBasePotionType());
    }
}
