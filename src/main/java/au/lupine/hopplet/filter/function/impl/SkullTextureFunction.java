package au.lupine.hopplet.filter.function.impl;

import au.lupine.hopplet.Hopplet;
import au.lupine.hopplet.filter.context.Context;
import au.lupine.hopplet.filter.exception.FilterCompileException;
import au.lupine.hopplet.filter.function.Matcher;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.net.URL;
import java.util.Base64;
import java.util.Set;

public final class SkullTextureFunction implements Matcher<String> {

    @Override
    public @NonNull String name() {
        return "skull_texture";
    }

    @Override
    public @NonNull Set<String> aliases() {
        return Set.of("head_texture");
    }

    @Override
    public @NonNull Component description() {
        return Component.translatable("hopplet.filter.function.skull_texture.description");
    }

    @Override
    public @NonNull Plugin plugin() {
        return Hopplet.instance();
    }

    @Override
    public @NonNull String parse(@NonNull String argument) throws FilterCompileException {
        try {
            Base64.getDecoder().decode(argument);
        } catch (IllegalArgumentException e) {
            throw new FilterCompileException(
                Component.translatable(
                    "hopplet.filter.function.skull_texture.compilation.exception.invalid_base64",
                    Argument.string("input", argument)
                )
            );
        }

        return argument;
    }

    @Override
    public boolean matches(@NonNull Context context, @NonNull String base64) {
        if (!(context.stack().getItemMeta() instanceof SkullMeta meta)) return false;

        PlayerProfile profile = meta.getPlayerProfile();
        if (profile == null) return false;

        URL url = profile.getTextures().getSkin();
        if (url == null) return false;

        try {
            return new String(
                Base64.getDecoder()
                    .decode(base64))
                .contains(url.toString());
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
