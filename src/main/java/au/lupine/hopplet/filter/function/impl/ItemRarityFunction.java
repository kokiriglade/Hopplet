package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

// This function is broken, it seems a lot or all of rarity is handled client side?
public final class ItemRarityFunction implements Matcher<ItemRarity> {

    @Override
    public @NonNull String name() {
        return "item_rarity";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("rarity");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.item_rarity.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull ItemRarity parse(@NonNull String argument) throws FilterCompileException {
        try {
            return ItemRarity.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.item_rarity.compilation.exception.unknown_item_rarity",
                    Argument.string("input", name())
                )
            );
        }
    }

    @Override
    public boolean matches(@NonNull FilterContext context, @NonNull ItemRarity rarity) {
        ItemMeta meta = context.stack().getItemMeta();

        if (!meta.hasRarity()) return rarity.equals(ItemRarity.COMMON);
        return rarity.equals(meta.getRarity());
    }
}
