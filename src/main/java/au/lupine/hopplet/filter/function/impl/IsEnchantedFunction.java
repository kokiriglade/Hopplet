package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.function.Predicate;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class IsEnchantedFunction implements Predicate {

    @Override
    public @NonNull String name() {
        return "is_enchanted";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("enchanted");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.is_enchanted.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public boolean test(@NonNull Context context, @NonNull Void compiled) {
        ItemStack item = context.stack();

        if (!item.getEnchantments().isEmpty()) return true;

        return item.getItemMeta() instanceof EnchantmentStorageMeta meta && !meta.getStoredEnchants().isEmpty();
    }
}
