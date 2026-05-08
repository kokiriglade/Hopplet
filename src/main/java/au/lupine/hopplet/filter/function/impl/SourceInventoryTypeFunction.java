package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.context.InventoryTransferContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class SourceInventoryTypeFunction implements Matcher<InventoryType> {

    @Override
    public @NonNull String name() {
        return "source_inventory_type";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "source_type",
            "source"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.source_inventory_type.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull InventoryType parse(@NonNull String argument) throws FilterCompileException {
        try {
            return InventoryType.valueOf(argument.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.source_inventory_type.compilation.exception.unknown_inventory_type",
                    Argument.string("input", argument)
                )
            );
        }
    }

    @Override
    public boolean matches(@NonNull Context context, @NonNull InventoryType argument) {
        if (!(context instanceof InventoryTransferContext ctx)) return false;

        Inventory source = ctx.source();
        if (source == null) return false;

        return source.getType() == argument;
    }
}
