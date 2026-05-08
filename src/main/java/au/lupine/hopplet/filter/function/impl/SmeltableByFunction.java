package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class SmeltableByFunction implements Matcher<InventoryType> {

    private static final @NonNull Set<InventoryType> FURNACE_TYPES = Set.of(
        InventoryType.BLAST_FURNACE,
        InventoryType.FURNACE,
        InventoryType.SMOKER
    );

    private static final @NonNull Set<FurnaceInventory> FURNACE_INVENTORIES = Set.of(
        (FurnaceInventory) Bukkit.createInventory(null, InventoryType.BLAST_FURNACE),
        (FurnaceInventory) Bukkit.createInventory(null, InventoryType.FURNACE),
        (FurnaceInventory) Bukkit.createInventory(null, InventoryType.SMOKER)
    );

    @Override
    public @NonNull String name() {
        return "smeltable_by";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of(
            "smeltable",
            "is_smeltable"
        );
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.smeltable_by.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull InventoryType parse(@NonNull String argument) throws FilterCompileException {
        try {
            InventoryType type = InventoryType.valueOf(argument.toUpperCase());
            if (!FURNACE_TYPES.contains(type)) throw new IllegalArgumentException();

            return type;
        } catch (IllegalArgumentException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.smeltable_by.compilation.exception.unknown_furnace_type",
                    Argument.string("input", argument)
                )
            );
        }
    }

    @Override
    public boolean matches(@NonNull FilterContext context, @NonNull InventoryType argument) {
        ItemStack stack = context.stack();

        for (FurnaceInventory inventory : FURNACE_INVENTORIES) {
            if (inventory.getType() != argument) continue;

            if (inventory.canSmelt(stack)) return true;
        }

        return false;
    }
}
