package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.FilterContext;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public final class MaterialFunction implements Matcher<Material> {

    @Override
    public @NonNull String name() {
        return "material";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("type");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.material.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull Material parse(@NonNull String argument) throws FilterCompileException {
        NamespacedKey key = NamespacedKey.fromString(argument.toLowerCase());
        Material material = key == null ? null : Registry.MATERIAL.get(key);

        if (material == null) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.material.compilation.exception.unknown_material",
                    Argument.string("input", argument)
                )
            );
        }

        return material;
    }

    @Override
    public boolean matches(@NonNull FilterContext context, @NonNull Material material) {
        return context.stack().getType().equals(material);
    }
}
