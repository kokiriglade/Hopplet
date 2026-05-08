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
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class ArmorTrimMaterialFunction implements Matcher<TrimMaterial> {

    private static final Registry<TrimMaterial> TRIM_MATERIAL_REGISTRY = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL);

    @Override
    public @NonNull String name() {
        return "armor_trim_material";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("trim_material");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.armor_trim_material.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull TrimMaterial parse(@NonNull String argument) throws FilterCompileException {
        NamespacedKey key = NamespacedKey.fromString(argument.toLowerCase());

        Function.checkKey(key, argument);

        TrimMaterial material = TRIM_MATERIAL_REGISTRY.get(key);
        if (material == null) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.armor_trim_material.compilation.exception.unknown_trim_material",
                    Argument.string("input", argument)
                )
            );
        }

        return material;
    }

    @Override
    public boolean matches(@NonNull FilterContext context, @NonNull TrimMaterial material) {
        if (!(context.stack().getItemMeta() instanceof ArmorMeta meta)) return false;

        ArmorTrim trim = meta.getTrim();
        if (trim == null) return false;

        return trim.getMaterial().equals(material);
    }
}
